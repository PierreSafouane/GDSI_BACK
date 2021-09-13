package fr.insy2s.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A MaterialRequest.
 */
@Entity
@Table(name = "material_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MaterialRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "date_request", nullable = false)
    private Instant dateRequest;

    @Column(name = "validated")
    private Boolean validated;

    @NotNull
    @Min(value = 1)
    @Column(name = "quantity_requested", nullable = false)
    private Integer quantityRequested;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "materialRequests", allowSetters = true)
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "materialRequests", allowSetters = true)
    private Material material;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateRequest() {
        return dateRequest;
    }

    public MaterialRequest dateRequest(Instant dateRequest) {
        this.dateRequest = dateRequest;
        return this;
    }

    public void setDateRequest(Instant dateRequest) {
        this.dateRequest = dateRequest;
    }

    public Boolean isValidated() {
        return validated;
    }

    public MaterialRequest validated(Boolean validated) {
        this.validated = validated;
        return this;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public Integer getQuantityRequested() {
        return quantityRequested;
    }

    public MaterialRequest quantityRequested(Integer quantityRequested) {
        this.quantityRequested = quantityRequested;
        return this;
    }

    public void setQuantityRequested(Integer quantityRequested) {
        this.quantityRequested = quantityRequested;
    }

    public User getUser() {
        return user;
    }

    public MaterialRequest user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Material getMaterial() {
        return material;
    }

    public MaterialRequest material(Material material) {
        this.material = material;
        return this;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialRequest)) {
            return false;
        }
        return id != null && id.equals(((MaterialRequest) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialRequest{" +
            "id=" + getId() +
            ", dateRequest='" + getDateRequest() + "'" +
            ", validated='" + isValidated() + "'" +
            ", quantityRequested=" + getQuantityRequested() +
            "}";
    }
}
