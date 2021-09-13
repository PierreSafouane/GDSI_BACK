package fr.insy2s.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link fr.insy2s.domain.RoomImage} entity.
 */
public class RoomImageDTO implements Serializable {
    
    private Long id;

    @Lob
    private byte[] imageRoom;

    private String imageRoomContentType;
    private String imagePath;


    private Long roomId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImageRoom() {
        return imageRoom;
    }

    public void setImageRoom(byte[] imageRoom) {
        this.imageRoom = imageRoom;
    }

    public String getImageRoomContentType() {
        return imageRoomContentType;
    }

    public void setImageRoomContentType(String imageRoomContentType) {
        this.imageRoomContentType = imageRoomContentType;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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
        if (!(o instanceof RoomImageDTO)) {
            return false;
        }

        return id != null && id.equals(((RoomImageDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomImageDTO{" +
            "id=" + getId() +
            ", imageRoom='" + getImageRoom() + "'" +
            ", imagePath='" + getImagePath() + "'" +
            ", roomId=" + getRoomId() +
            "}";
    }
}
