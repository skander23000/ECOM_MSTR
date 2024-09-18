package fr.ecom.mstr.tire.web.rest;

import fr.ecom.mstr.tire.repository.TireRepository;
import fr.ecom.mstr.tire.security.AuthoritiesConstants;
import fr.ecom.mstr.tire.service.S3Service;
import fr.ecom.mstr.tire.service.TireQueryService;
import fr.ecom.mstr.tire.service.TireService;
import fr.ecom.mstr.tire.service.criteria.TireCriteria;
import fr.ecom.mstr.tire.service.dto.TireDTO;
import fr.ecom.mstr.tire.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.ecom.mstr.tire.domain.Tire}.
 */
@RestController
@RequestMapping("/api/aws")
public class S3Resource {

    private static final Logger LOG = LoggerFactory.getLogger(S3Resource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final S3Service s3Service;

    public S3Resource(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    /**
     * {@code POST  /tires} : Upload a picture.
     *
     * @param file the picture to upload.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the url of the picture.
     */
    @PostMapping(value = "", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<String> createTire(@RequestParam("file") MultipartFile file) {
        String imageUrl = this.s3Service.saveFileToAWSS3Bucket(file);
        return ResponseEntity.ok(imageUrl);
    }
}
