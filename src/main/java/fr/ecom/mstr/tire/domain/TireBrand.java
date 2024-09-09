package fr.ecom.mstr.tire.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A TireBrand.
 */
@Entity
@Table(name = "tire_brand")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TireBrand implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "logo_url", nullable = false)
    private String logoUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tireBrand")
    @JsonIgnoreProperties(value = { "itemListLocks", "tireBrand" }, allowSetters = true)
    private Set<Tire> tires = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TireBrand id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TireBrand name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return this.logoUrl;
    }

    public TireBrand logoUrl(String logoUrl) {
        this.setLogoUrl(logoUrl);
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Set<Tire> getTires() {
        return this.tires;
    }

    public void setTires(Set<Tire> tires) {
        if (this.tires != null) {
            this.tires.forEach(i -> i.setTireBrand(null));
        }
        if (tires != null) {
            tires.forEach(i -> i.setTireBrand(this));
        }
        this.tires = tires;
    }

    public TireBrand tires(Set<Tire> tires) {
        this.setTires(tires);
        return this;
    }

    public TireBrand addTires(Tire tire) {
        this.tires.add(tire);
        tire.setTireBrand(this);
        return this;
    }

    public TireBrand removeTires(Tire tire) {
        this.tires.remove(tire);
        tire.setTireBrand(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TireBrand)) {
            return false;
        }
        return getId() != null && getId().equals(((TireBrand) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TireBrand{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", logoUrl='" + getLogoUrl() + "'" +
            "}";
    }
}
