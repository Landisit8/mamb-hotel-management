package com.mamb.hotel.maintenance_tickets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mamb.hotel.rooms.Room;
import com.mamb.hotel.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "maintenance_tickets")
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reported_by_user_id", nullable = false)
    private User reportedByUser;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must be at most 150 characters")
    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @NotBlank(message = "Description is required")
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull(message = "Maintenance ticket status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private MaintenanceTicketStatus status = MaintenanceTicketStatus.OPEN;

    @NotNull(message = "Maintenance ticket priority is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 50)
    private MaintenanceTicketPriority priority = MaintenanceTicketPriority.NORMAL;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @JsonProperty("roomId")
    public Long getRoomId() {
        return room != null ? room.getId() : null;
    }

    @JsonProperty("reportedByUserId")
    public Long getReportedByUserId() {
        return reportedByUser != null ? reportedByUser.getId() : null;
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
            this.status = MaintenanceTicketStatus.OPEN;
        }

        if (this.priority == null) {
            this.priority = MaintenanceTicketPriority.NORMAL;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}