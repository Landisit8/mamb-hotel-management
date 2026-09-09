package com.mamb.hotel.rooms;

import com.mamb.hotel.reservations.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    // [MAMB-02] Query p[er trovare le camere libere escludendo blocchi e prenotazioni
    @Query("""
            SELECT r FROM Room r
            WHERE r.operationalStatus = 'ACTIVE'
                   AND NOT EXISTS (
                        SELECT rb FROM RoomBlock rb
                        WHERE rb.startDate < :endDate AND rb.endDate > :startDate
                    )
                    AND NOT EXISTS (
                        SELECT rr FROM ReservationRoom rr
                         WHERE rr.room = r
                            AND rr.reservation.status IN :blockingStatuses
                            AND rr.startDate < :endDate
                            AND rr.endDate > :startDate
                    )""")
    List<Room> findAvailableRooms(@Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate,
                                  @Param("blockingStatuses") List<ReservationStatus> blockingStatuses);
}
