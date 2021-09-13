package fr.insy2s.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link fr.insy2s.domain.Presence} entity.
 */
public class PresenceDTO implements Serializable {
    
    private Long id;

    private Boolean isAttending;


    private Long appUserId;

    private Long bookingId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsAttending() {
        return isAttending;
    }

    public void setIsAttending(Boolean isAttending) {
        this.isAttending = isAttending;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long userId) {
        this.appUserId = userId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PresenceDTO)) {
            return false;
        }

        return id != null && id.equals(((PresenceDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PresenceDTO{" +
            "id=" + getId() +
            ", isAttending='" + isIsAttending() + "'" +
            ", appUserId=" + getAppUserId() +
            ", bookingId=" + getBookingId() +
            "}";
    }
}
