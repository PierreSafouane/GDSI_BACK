package fr.insy2s.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link fr.insy2s.domain.MaterialRequest} entity.
 */
public class MaterialRequestDTO implements Serializable {
    
    private Long id;

    @NotNull
    private Instant dateRequest;

    private Boolean validated;

    @NotNull
    @Min(value = 1)
    private Integer quantityRequested;


    private Long userId;

    private Long materialId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(Instant dateRequest) {
        this.dateRequest = dateRequest;
    }

    public Boolean isValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public Integer getQuantityRequested() {
        return quantityRequested;
    }

    public void setQuantityRequested(Integer quantityRequested) {
        this.quantityRequested = quantityRequested;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialRequestDTO)) {
            return false;
        }

        return id != null && id.equals(((MaterialRequestDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialRequestDTO{" +
            "id=" + getId() +
            ", dateRequest='" + getDateRequest() + "'" +
            ", validated='" + isValidated() + "'" +
            ", quantityRequested=" + getQuantityRequested() +
            ", userId=" + getUserId() +
            ", materialId=" + getMaterialId() +
            "}";
    }
}
