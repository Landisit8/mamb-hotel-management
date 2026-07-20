package com.mamb.hotel.reservation_status_history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationStatusHistoryRepository extends JpaRepository<ReservationStatusHistory, Long> {

    List<ReservationStatusHistory> findByReservation_Id(Long reservationId);

    List<ReservationStatusHistory> findByChangedByUser_Id(Long changedByUserId);
}