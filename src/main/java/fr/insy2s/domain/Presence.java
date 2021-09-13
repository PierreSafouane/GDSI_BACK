package fr.insy2s.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Presence.
 */
@Entity
@Table(name = "presence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Presence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "is_attending")
    private Boolean isAttending;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "presences", allowSetters = true)
    private User appUser;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "presences", allowSetters = true)
    private Booking booking;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsAttending() {
        return isAttending;
    }

    public Presence isAttending(Boolean isAttending) {
        this.isAttending = isAttending;
        return this;
    }

    public void setIsAttending(Boolean isAttending) {
        this.isAttending = isAttending;
    }

    public User getAppUser() {
        return appUser;
    }

    public Presence appUser(User user) {
        this.appUser = user;
        return this;
    }

    public void setAppUser(User user) {
        this.appUser = user;
    }

    public Booking getBooking() {
        return booking;
    }

    public Presence booking(Booking booking) {
        this.booking = booking;
        return this;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Presence)) {
            return false;
        }
        return id != null && id.equals(((Presence) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Presence{" +
            "id=" + getId() +
            ", isAttending='" + isIsAttending() + "'" +
            "}";
    }
}
