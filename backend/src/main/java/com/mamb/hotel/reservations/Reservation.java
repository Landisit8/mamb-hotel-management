package com.mamb.hotel.reservations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mamb.hotel.customer.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "reservations")
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //@NotBlank(message = "Reservation code is required")
    @Size(max = 50, message = "Reservation code must be at most 50 characters")
    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "primary_customer_id", nullable = false)
    private Customer primaryCustomer;

    @NotNull(message = "Check in date is required")
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @NotNull(message = "Check out date is required")
    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "actual_check_in_at")
    private LocalDateTime actualCheckInAt;

    @Column(name = "actual_check_out_at")
    private LocalDateTime actualCheckOutAt;

    @NotNull(message = "Adults is required")
    @Min(value = 1, message = "Adults must be at least 1")
    @Column(name = "adults", nullable = false)
    private Integer adults;

    @NotNull(message = "Children is required")
    @Min(value = 0, message = "Children cannot be negative")
    @Column(name = "children", nullable = false)
    private Integer children = 0;

    @NotNull(message = "Reservation status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private ReservationStatus status = ReservationStatus.PENDING;

    @NotNull(message = "Reservation source is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "source", nullable = false, length = 50)
    private ReservationSource source = ReservationSource.DIRECT;

    @Column(name = "special_requests")
    private String specialRequests;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.00", message = "Total amount cannot be negative")
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @JsonProperty("primaryCustomerId")
    public Long getPrimaryCustomerId() {
        return primaryCustomer != null ? primaryCustomer.getId() : null;
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.children == null) {
            this.children = 0;
        }

        if (this.status == null) {
            this.status = ReservationStatus.PENDING;
        }

        if (this.source == null) {
            this.source = ReservationSource.DIRECT;
        }

        if (this.totalAmount == null) {
            this.totalAmount = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
