package com.mamb.hotel.housekeeping_tasks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HousekeepingTaskRepository extends JpaRepository<HousekeepingTask, Long> {

    List<HousekeepingTask> findByRoom_Id(Long roomId);

    List<HousekeepingTask> findByReservation_Id(Long reservationId);

    List<HousekeepingTask> findByAssignedUser_Id(Long assignedUserId);
}