package com.mamb.hotel.reservation_guests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationGuestRepository extends JpaRepository<ReservationGuest, Long> {

    boolean existsByReservation_IdAndCustomer_Id(Long reservationId, Long customerId);

    // nuovo metodo per limitare il numero di ospiti principali.
    boolean existsByReservation_IdAndRole(Long reservation_Id, ReservationGuestRole role);

    List<ReservationGuest> findAllByReservation_Id(Long reservationId);
}