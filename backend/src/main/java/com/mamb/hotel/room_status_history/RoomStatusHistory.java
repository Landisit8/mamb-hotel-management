package com.mamb.hotel.room_status_history;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mamb.hotel.rooms.CleaningStatus;
import com.mamb.hotel.rooms.OperationalStatus;
import com.mamb.hotel.rooms.Room;
import com.mamb.hotel.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "room_status_history")
@NoArgsConstructor
@AllArgsConstructor
public class RoomStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_operational_status", length = 50)
    private OperationalStatus oldOperationalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_operational_status", length = 50)
    private OperationalStatus newOperationalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_cleaning_status", length = 50)
    private CleaningStatus oldCleaningStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_cleaning_status", length = 50)
    private CleaningStatus newCleaningStatus;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "changed_by_user_id", nullable = false)
    private User changedByUser;

    @Column(name = "reason")
    private String reason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @JsonProperty("roomId")
    public Long getRoomId() {
        return room != null ? room.getId() : null;
    }

    @JsonProperty("changedByUserId")
    public Long getChangedByUserId() {
        return changedByUser != null ? changedByUser.getId() : null;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}