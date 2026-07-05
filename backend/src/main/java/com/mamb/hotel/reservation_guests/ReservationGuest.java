package com.mamb.hotel.reservation_guests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mamb.hotel.customer.Customer;
import com.mamb.hotel.reservations.Reservation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "reservation_guests")
@NoArgsConstructor
@AllArgsConstructor
public class ReservationGuest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotNull(message = "Guest role is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private ReservationGuestRole role = ReservationGuestRole.GUEST;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @JsonProperty("reservationId")
    public Long getReservationId() {
        return reservation != null ? reservation.getId() : null;
    }

    @JsonProperty("customerId")
    public Long getCustomerId() {
        return customer != null ? customer.getId() : null;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();

        if (this.role == null) {
            this.role = ReservationGuestRole.GUEST;
        }
    }
}