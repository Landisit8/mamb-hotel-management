package com.mamb.hotel.reservation_guests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationGuestRepository extends JpaRepository<ReservationGuest, Long> {}