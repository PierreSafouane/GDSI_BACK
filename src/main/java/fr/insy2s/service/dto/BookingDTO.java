package fr.insy2s.service.dto;

import io.swagger.annotations.ApiModel;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link fr.insy2s.domain.Booking} entity.
 */
@ApiModel(description = "Extends AbstractAuditingEntity")
public class BookingDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String title;

    @Size(max = 1000)
    private String description;

    @NotNull
    private Instant startAt;

    @NotNull
    private Instant finishAt;


    private Long roomId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(Instant finishAt) {
        this.finishAt = finishAt;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingDTO)) {
            return false;
        }

        return id != null && id.equals(((BookingDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", startAt='" + getStartAt() + "'" +
            ", finishAt='" + getFinishAt() + "'" +
            ", roomId=" + getRoomId() +
            "}";
    }
}
