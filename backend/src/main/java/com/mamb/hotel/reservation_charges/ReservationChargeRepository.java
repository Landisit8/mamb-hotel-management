package com.mamb.hotel.reservation_charges;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationChargeRepository extends JpaRepository<ReservationCharge, Long> {

    List<ReservationCharge> findByReservation_Id(Long reservationId);
}