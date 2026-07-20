package com.mamb.hotel.room_status_history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomStatusHistoryRepository extends JpaRepository<RoomStatusHistory, Long> {

    List<RoomStatusHistory> findByRoom_Id(Long roomId);

    List<RoomStatusHistory> findByChangedByUser_Id(Long changedByUserId);
}