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

/**
 * Criteria class for the {@link fr.insy2s.domain.Material} entity. This class is used
 * in {@link fr.insy2s.web.rest.MaterialResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /materials?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MaterialCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter imageUrl;

    private StringFilter name;

    private StringFilter description;

    private DoubleFilter price;

    private IntegerFilter quantity;

    private StringFilter link;

    private LongFilter typeId;

    private LongFilter roomsId;

    public MaterialCriteria() {
    }

    public MaterialCriteria(MaterialCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.imageUrl = other.imageUrl == null ? null : other.imageUrl.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.link = other.link == null ? null : other.link.copy();
        this.typeId = other.typeId == null ? null : other.typeId.copy();
        this.roomsId = other.roomsId == null ? null : other.roomsId.copy();
    }

    @Override
    public MaterialCriteria copy() {
        return new MaterialCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public StringFilter getLink() {
        return link;
    }

    public void setLink(StringFilter link) {
        this.link = link;
    }

    public LongFilter getTypeId() {
        return typeId;
    }

    public void setTypeId(LongFilter typeId) {
        this.typeId = typeId;
    }

    public LongFilter getRoomsId() {
        return roomsId;
    }

    public void setRoomsId(LongFilter roomsId) {
        this.roomsId = roomsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MaterialCriteria that = (MaterialCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(price, that.price) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(link, that.link) &&
            Objects.equals(typeId, that.typeId) &&
            Objects.equals(roomsId, that.roomsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        imageUrl,
        name,
        description,
        price,
        quantity,
        link,
        typeId,
        roomsId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (imageUrl != null ? "imageUrl=" + imageUrl + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (price != null ? "price=" + price + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (link != null ? "link=" + link + ", " : "") +
                (typeId != null ? "typeId=" + typeId + ", " : "") +
                (roomsId != null ? "roomsId=" + roomsId + ", " : "") +
            "}";
    }

}
