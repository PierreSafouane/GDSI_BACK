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
 * Criteria class for the {@link fr.insy2s.domain.Booking} entity. This class is used
 * in {@link fr.insy2s.web.rest.BookingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bookings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BookingCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private InstantFilter startAt;

    private InstantFilter finishAt;

    private LongFilter presencesId;

    private LongFilter roomId;

    public BookingCriteria() {
    }

    public BookingCriteria(BookingCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.startAt = other.startAt == null ? null : other.startAt.copy();
        this.finishAt = other.finishAt == null ? null : other.finishAt.copy();
        this.presencesId = other.presencesId == null ? null : other.presencesId.copy();
        this.roomId = other.roomId == null ? null : other.roomId.copy();
    }

    @Override
    public BookingCriteria copy() {
        return new BookingCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public InstantFilter getStartAt() {
        return startAt;
    }

    public void setStartAt(InstantFilter startAt) {
        this.startAt = startAt;
    }

    public InstantFilter getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(InstantFilter finishAt) {
        this.finishAt = finishAt;
    }

    public LongFilter getPresencesId() {
        return presencesId;
    }

    public void setPresencesId(LongFilter presencesId) {
        this.presencesId = presencesId;
    }

    public LongFilter getRoomId() {
        return roomId;
    }

    public void setRoomId(LongFilter roomId) {
        this.roomId = roomId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BookingCriteria that = (BookingCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(startAt, that.startAt) &&
            Objects.equals(finishAt, that.finishAt) &&
            Objects.equals(presencesId, that.presencesId) &&
            Objects.equals(roomId, that.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        title,
        description,
        startAt,
        finishAt,
        presencesId,
        roomId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (startAt != null ? "startAt=" + startAt + ", " : "") +
                (finishAt != null ? "finishAt=" + finishAt + ", " : "") +
                (presencesId != null ? "presencesId=" + presencesId + ", " : "") +
                (roomId != null ? "roomId=" + roomId + ", " : "") +
            "}";
    }

}
