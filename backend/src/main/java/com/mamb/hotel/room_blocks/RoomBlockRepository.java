package com.mamb.hotel.room_blocks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface RoomBlockRepository extends JpaRepository<RoomBlock, Long> {

        @Query("""
            select count(roomBlock) > 0
            from RoomBlock roomBlock
            where roomBlock.room.id = :roomId
            and roomBlock.startDate < :endDate
            and roomBlock.endDate > :startDate
            """)
        boolean existsOverlappingRoomBlock(
                @Param("roomId") Long roomId,
                @Param("startDate") LocalDate startDate,
                @Param("endDate") LocalDate endDate
        );

}
