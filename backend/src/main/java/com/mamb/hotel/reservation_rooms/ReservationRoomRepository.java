package com.mamb.hotel.reservation_rooms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRoomRepository extends JpaRepository<ReservationRoom, Long> {}