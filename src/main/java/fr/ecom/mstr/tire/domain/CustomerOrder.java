package fr.ecom.mstr.tire.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.ecom.mstr.tire.domain.enumeration.OrderStatus;
import fr.ecom.mstr.tire.domain.enumeration.PaymentMethod;
import fr.ecom.mstr.tire.domain.enumeration.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A CustomerOrder.
 */
@Entity
@Table(name = "customer_order")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @NotNull
    @Column(name = "total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    private Instant paymentDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @JsonIgnoreProperties(value = { "customerOrder" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "customerOrder")
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CustomerOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public CustomerOrder orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public CustomerOrder status(OrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public CustomerOrder totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Instant getPaymentDate() {
        return this.paymentDate;
    }

    public CustomerOrder paymentDate(Instant paymentDate) {
        this.setPaymentDate(paymentDate);
        return this;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    public CustomerOrder paymentMethod(PaymentMethod paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    public CustomerOrder paymentStatus(PaymentStatus paymentStatus) {
        this.setPaymentStatus(paymentStatus);
        return this;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        if (this.customer != null) {
            this.customer.setCustomerOrder(null);
        }
        if (customer != null) {
            customer.setCustomerOrder(this);
        }
        this.customer = customer;
    }

    public CustomerOrder customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerOrder)) {
            return false;
        }
        return getId() != null && getId().equals(((CustomerOrder) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerOrder{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            "}";
    }
}
