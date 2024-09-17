package fr.ecom.mstr.tire.service;

import fr.ecom.mstr.tire.domain.Customer;
import fr.ecom.mstr.tire.domain.CustomerOrder;
import fr.ecom.mstr.tire.domain.User;
import fr.ecom.mstr.tire.domain.enumeration.OrderStatus;
import fr.ecom.mstr.tire.domain.enumeration.PaymentMethod;
import fr.ecom.mstr.tire.domain.enumeration.PaymentStatus;
import fr.ecom.mstr.tire.service.dto.CustomerDTO;
import fr.ecom.mstr.tire.service.dto.CustomerOrderDTO;
import fr.ecom.mstr.tire.service.dto.OrderItemDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import tech.jhipster.config.JHipsterProperties;

/**
 * Service for sending emails asynchronously.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private static final Logger LOG = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public MailService(
        JHipsterProperties jHipsterProperties,
        JavaMailSender javaMailSender,
        MessageSource messageSource,
        SpringTemplateEngine templateEngine
    ) {
        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        this.sendEmailSync(to, subject, content, isMultipart, isHtml);
    }

    private void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        LOG.debug(
            "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart,
            isHtml,
            to,
            subject,
            content
        );

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            LOG.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            LOG.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        this.sendEmailFromTemplateSync(user, templateName, titleKey);
    }

    private void sendEmailFromTemplateSync(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            LOG.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        this.sendEmailSync(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendActivationEmail(User user) {
        LOG.debug("Sending activation email to '{}'", user.getEmail());
        CustomerOrderDTO order = new CustomerOrderDTO();

        // Valeurs fictives
        order.setId(123L);

        // Dates et montants fictifs
        order.setOrderDate(Instant.parse("2024-09-17T10:00:00Z"));
        order.setPaymentDate(Instant.parse("2024-09-17T10:30:00Z"));
        order.setTotalAmount(new BigDecimal("99.99"));

        // Enum fictifs
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        order.setPaymentStatus(PaymentStatus.PENDING);

        // Informations client fictives
        CustomerDTO customer = new CustomerDTO();
        customer.setFirstName("Jean");
        customer.setLastName("Dupont");
        customer.setEmail("jean.dupont@example.com");
        customer.setAddress("123 Rue de l'Exemple");
        customer.setCity("Paris");
        customer.setZipCode("75001");
        customer.setCountry("France");
        customer.setPhoneNumber("0123456789");
        order.setCustomer(customer);


        List<OrderItemDTO> orderItems = new ArrayList<>();
        // Création de trois éléments OrderItemDTO avec des valeurs non-nulles
        OrderItemDTO item1 = new OrderItemDTO();
        item1.setId(1L);
        item1.setQuantity(4);
        item1.setPrice(new BigDecimal("29.99"));

        OrderItemDTO item2 = new OrderItemDTO();
        item2.setId(2L);
        item2.setQuantity(2);
        item2.setPrice(new BigDecimal("49.99"));

        OrderItemDTO item3 = new OrderItemDTO();
        item3.setId(3L);
        item3.setQuantity(1);
        item3.setPrice(new BigDecimal("89.99"));

        // Ajouter les éléments à la liste
        item1.setCustomerOrder(order);
        item2.setCustomerOrder(order);
        item3.setCustomerOrder(order);
        orderItems.add(item1);
        orderItems.add(item2);
        orderItems.add(item3);

        this.sendInvoicingEmail(orderItems,order);
        //this.sendEmailFromTemplateSync(user, "mail/activationEmail", "email.activation.title");
    }

    private void sendInvoicingEmail(List<OrderItemDTO> items, CustomerOrderDTO customerOrder){
        Locale locale = Locale.forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable("customerOrder", customerOrder);
        context.setVariable("items", items);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process("mail/Factour", context);
        String subject = messageSource.getMessage("Factour", null, locale);
        this.sendEmailSync(customerOrder.getCustomer().getEmail(), subject, content, false, true);

    }

    @Async
    public void sendCreationEmail(User user) {
        LOG.debug("Sending creation email to '{}'", user.getEmail());
        this.sendEmailFromTemplateSync(user, "mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        LOG.debug("Sending password reset email to '{}'", user.getEmail());
        this.sendEmailFromTemplateSync(user, "mail/passwordResetEmail", "email.reset.title");
    }
}
