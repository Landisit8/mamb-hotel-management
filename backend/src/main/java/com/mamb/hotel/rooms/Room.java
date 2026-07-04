package com.mamb.hotel.rooms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.mamb.hotel.room_types.RoomType;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "rooms")
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_type_id", nullable = false)
    private RoomType roomType;

    @NotBlank(message = "Room number is required")
    @Size(max = 20, message = "Room number must be at most 20 characters")
    @Column(name = "room_number", nullable = false, unique = true, length = 20)
    private String roomNumber;

    @Column(name = "floor")
    private Integer floor;

    @NotNull(message = "Operational status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "operational_status", nullable = false, length = 50)
    private OperationalStatus operationalStatus = OperationalStatus.ACTIVE;

    @NotNull(message = "Cleaning status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "cleaning_status", nullable = false, length = 50)
    private CleaningStatus cleaningStatus = CleaningStatus.CLEAN;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @JsonProperty("roomTypeId")
    public Long getRoomTypeId() {
        return roomType != null ? roomType.getId() : null;
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.operationalStatus == null) {
            this.operationalStatus = OperationalStatus.ACTIVE;
        }

        if (this.cleaningStatus == null) {
            this.cleaningStatus = CleaningStatus.CLEAN;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}