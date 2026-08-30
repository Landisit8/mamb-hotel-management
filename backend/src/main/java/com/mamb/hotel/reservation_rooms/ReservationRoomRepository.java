package com.mamb.hotel.reservation_rooms;

import com.mamb.hotel.reservations.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRoomRepository extends JpaRepository<ReservationRoom, Long> {

    List<ReservationRoom> findByReservation_Id(Long reservationId);

    // [MAMB-01] Query JPQL per verificare l'occupazione di una camera in un periodo
    // restituisce true se trova almeno una ReservationRoom per quella stanza,
    // con date sovrapposte e associata a una prenotazione con uno degli stati indicati.
    @Query("""
            select count(reservationRoom) > 0
            from ReservationRoom reservationRoom
            where reservationRoom.room.id = :roomId
            and (:reservationRoomId is null or reservationRoom.id <> :reservationRoomId)
            and reservationRoom.reservation.status in :statuses
            and reservationRoom.startDate < :endDate
            and reservationRoom.endDate > :startDate
            """)
    boolean existsOverlappingRoomAssignment(
            @Param("roomId")Long roomId,
            @Param("startDate")LocalDate startDate,
            @Param("endDate")LocalDate endDate,
            @Param("reservationRoomId")Long reservationRoomId,
            @Param("statuses")List<ReservationStatus> statuses
    );
}