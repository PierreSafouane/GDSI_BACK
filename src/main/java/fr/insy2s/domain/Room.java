package fr.insy2s.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Room.
 */
@Entity
@Table(name = "room")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @OneToMany(mappedBy = "room")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<RoomImage> images = new HashSet<>();

    @ManyToMany(mappedBy = "rooms")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Material> materials = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Room name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public Room maxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
        return this;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Set<RoomImage> getImages() {
        return images;
    }

    public Room images(Set<RoomImage> roomImages) {
        this.images = roomImages;
        return this;
    }

    public Room addImages(RoomImage roomImage) {
        this.images.add(roomImage);
        roomImage.setRoom(this);
        return this;
    }

    public Room removeImages(RoomImage roomImage) {
        this.images.remove(roomImage);
        roomImage.setRoom(null);
        return this;
    }

    public void setImages(Set<RoomImage> roomImages) {
        this.images = roomImages;
    }

    public Set<Material> getMaterials() {
        return materials;
    }

    public Room materials(Set<Material> materials) {
        this.materials = materials;
        return this;
    }

    public Room addMaterials(Material material) {
        this.materials.add(material);
        material.getRooms().add(this);
        return this;
    }

    public Room removeMaterials(Material material) {
        this.materials.remove(material);
        material.getRooms().remove(this);
        return this;
    }

    public void setMaterials(Set<Material> materials) {
        this.materials = materials;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        return id != null && id.equals(((Room) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Room{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", maxCapacity=" + getMaxCapacity() +
            "}";
    }
}
