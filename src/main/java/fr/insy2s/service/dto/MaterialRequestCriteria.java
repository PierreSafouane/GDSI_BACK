package fr.insy2s.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link fr.insy2s.domain.MaterialRequest} entity. This class is used
 * in {@link fr.insy2s.web.rest.MaterialRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /material-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MaterialRequestCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter dateRequest;

    private BooleanFilter validated;

    private IntegerFilter quantityRequested;

    private LongFilter userId;

    private LongFilter materialId;

    public MaterialRequestCriteria() {
    }

    public MaterialRequestCriteria(MaterialRequestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateRequest = other.dateRequest == null ? null : other.dateRequest.copy();
        this.validated = other.validated == null ? null : other.validated.copy();
        this.quantityRequested = other.quantityRequested == null ? null : other.quantityRequested.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.materialId = other.materialId == null ? null : other.materialId.copy();
    }

    @Override
    public MaterialRequestCriteria copy() {
        return new MaterialRequestCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getDateRequest() {
        return dateRequest;
    }

    public void setDateRequest(InstantFilter dateRequest) {
        this.dateRequest = dateRequest;
    }

    public BooleanFilter getValidated() {
        return validated;
    }

    public void setValidated(BooleanFilter validated) {
        this.validated = validated;
    }

    public IntegerFilter getQuantityRequested() {
        return quantityRequested;
    }

    public void setQuantityRequested(IntegerFilter quantityRequested) {
        this.quantityRequested = quantityRequested;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getMaterialId() {
        return materialId;
    }

    public void setMaterialId(LongFilter materialId) {
        this.materialId = materialId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MaterialRequestCriteria that = (MaterialRequestCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dateRequest, that.dateRequest) &&
            Objects.equals(validated, that.validated) &&
            Objects.equals(quantityRequested, that.quantityRequested) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(materialId, that.materialId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dateRequest,
        validated,
        quantityRequested,
        userId,
        materialId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialRequestCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dateRequest != null ? "dateRequest=" + dateRequest + ", " : "") +
                (validated != null ? "validated=" + validated + ", " : "") +
                (quantityRequested != null ? "quantityRequested=" + quantityRequested + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (materialId != null ? "materialId=" + materialId + ", " : "") +
            "}";
    }

}
