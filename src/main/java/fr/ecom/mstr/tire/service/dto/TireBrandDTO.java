package fr.ecom.mstr.tire.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.ecom.mstr.tire.domain.TireBrand} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TireBrandDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String logoUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TireBrandDTO)) {
            return false;
        }

        TireBrandDTO tireBrandDTO = (TireBrandDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tireBrandDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TireBrandDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", logoUrl='" + getLogoUrl() + "'" +
            "}";
    }
}
