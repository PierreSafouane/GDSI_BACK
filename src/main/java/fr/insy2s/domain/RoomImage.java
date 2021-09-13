package fr.insy2s.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A RoomImage.
 */
@Entity
@Table(name = "room_image")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RoomImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Lob
    @Column(name = "image_room")
    private byte[] imageRoom;

    @Column(name = "image_room_content_type")
    private String imageRoomContentType;

    @Column(name = "image_path")
    private String imagePath;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = "images", allowSetters = true)
    private Room room;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImageRoom() {
        return imageRoom;
    }

    public RoomImage imageRoom(byte[] imageRoom) {
        this.imageRoom = imageRoom;
        return this;
    }

    public void setImageRoom(byte[] imageRoom) {
        this.imageRoom = imageRoom;
    }

    public String getImageRoomContentType() {
        return imageRoomContentType;
    }

    public RoomImage imageRoomContentType(String imageRoomContentType) {
        this.imageRoomContentType = imageRoomContentType;
        return this;
    }

    public void setImageRoomContentType(String imageRoomContentType) {
        this.imageRoomContentType = imageRoomContentType;
    }

    public String getImagePath() {
        return imagePath;
    }

    public RoomImage imagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Room getRoom() {
        return room;
    }

    public RoomImage room(Room room) {
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
        if (!(o instanceof RoomImage)) {
            return false;
        }
        return id != null && id.equals(((RoomImage) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomImage{" +
            "id=" + getId() +
            ", imageRoom='" + getImageRoom() + "'" +
            ", imageRoomContentType='" + getImageRoomContentType() + "'" +
            ", imagePath='" + getImagePath() + "'" +
            "}";
    }
}
