package com.mamb.hotel.maintenance_tickets;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceTicketRepository extends JpaRepository<MaintenanceTicket, Long> {

    List<MaintenanceTicket> findByRoom_Id(Long roomId);

    List<MaintenanceTicket> findByReportedByUser_Id(Long reportedByUserId);

    List<MaintenanceTicket> findByAssignedUser_Id(Long assignedUserId);
}