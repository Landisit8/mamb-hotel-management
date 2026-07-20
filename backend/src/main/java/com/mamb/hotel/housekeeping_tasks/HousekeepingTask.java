package com.mamb.hotel.housekeeping_tasks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mamb.hotel.reservations.Reservation;
import com.mamb.hotel.rooms.Room;
import com.mamb.hotel.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "housekeeping_tasks")
@NoArgsConstructor
@AllArgsConstructor
public class HousekeepingTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @NotNull(message = "Housekeeping task status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private HousekeepingTaskStatus status = HousekeepingTaskStatus.TODO;

    @NotNull(message = "Housekeeping task priority is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 50)
    private HousekeepingTaskPriority priority = HousekeepingTaskPriority.NORMAL;

    @NotNull(message = "Housekeeping task type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false, length = 50)
    private HousekeepingTaskType taskType = HousekeepingTaskType.CLEANING;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @JsonProperty("roomId")
    public Long getRoomId() {
        return room != null ? room.getId() : null;
    }

    @JsonProperty("reservationId")
    public Long getReservationId() {
        return reservation != null ? reservation.getId() : null;
    }

    @JsonProperty("assignedUserId")
    public Long getAssignedUserId() {
        return assignedUser != null ? assignedUser.getId() : null;
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null) {
            this.status = HousekeepingTaskStatus.TODO;
        }

        if (this.priority == null) {
            this.priority = HousekeepingTaskPriority.NORMAL;
        }

        if (this.taskType == null) {
            this.taskType = HousekeepingTaskType.CLEANING;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}