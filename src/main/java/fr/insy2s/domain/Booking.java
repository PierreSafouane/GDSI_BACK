package fr.insy2s.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Extends AbstractAuditingEntity
 */
@Entity
@Table(name = "booking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Booking extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @NotNull
    @Column(name = "start_at", nullable = false)
    private Instant startAt;

    @NotNull
    @Column(name = "finish_at", nullable = false)
    private Instant finishAt;

    @OneToMany(mappedBy = "booking")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Presence> presences = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "bookings", allowSetters = true)
    private Room room;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Booking title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Booking description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public Booking startAt(Instant startAt) {
        this.startAt = startAt;
        return this;
    }

    public void setStartAt(Instant startAt) {
        this.startAt = startAt;
    }

    public Instant getFinishAt() {
        return finishAt;
    }

    public Booking finishAt(Instant finishAt) {
        this.finishAt = finishAt;
        return this;
    }

    public void setFinishAt(Instant finishAt) {
        this.finishAt = finishAt;
    }

    public Set<Presence> getPresences() {
        return presences;
    }

    public Booking presences(Set<Presence> presences) {
        this.presences = presences;
        return this;
    }

    public Booking addPresences(Presence presence) {
        this.presences.add(presence);
        presence.setBooking(this);
        return this;
    }

    public Booking removePresences(Presence presence) {
        this.presences.remove(presence);
        presence.setBooking(null);
        return this;
    }

    public void setPresences(Set<Presence> presences) {
        this.presences = presences;
    }

    public Room getRoom() {
        return room;
    }

    public Booking room(Room room) {
        this.room = room;
        return this;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Booking)) {
            return false;
        }
        return id != null && id.equals(((Booking) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Booking{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", startAt='" + getStartAt() + "'" +
            ", finishAt='" + getFinishAt() + "'" +
            "}";
    }
}
