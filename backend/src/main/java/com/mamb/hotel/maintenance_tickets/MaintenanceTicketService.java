package com.mamb.hotel.maintenance_tickets;

import com.mamb.hotel.rooms.Room;
import com.mamb.hotel.rooms.RoomRepository;
import com.mamb.hotel.users.User;
import com.mamb.hotel.users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceTicketService {

    private final MaintenanceTicketRepository maintenanceTicketRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public MaintenanceTicket create(final Long roomId, final Long reportedByUserId, final Long assignedUserId, final MaintenanceTicket req) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Camera non presente"));
        User reportedByUser = userRepository.findById(reportedByUserId)
                .orElseThrow(() -> new EntityNotFoundException("Utente segnalatore non presente"));

        User assignedUser = null;

        if (assignedUserId != null) {
            assignedUser = userRepository.findById(assignedUserId)
                    .orElseThrow(() -> new EntityNotFoundException("Utente assegnato non presente"));
        }

        req.setRoom(room);
        req.setReportedByUser(reportedByUser);
        req.setAssignedUser(assignedUser);
        return maintenanceTicketRepository.save(req);
    }

    public MaintenanceTicket update(final MaintenanceTicket maintenanceTicket, final MaintenanceTicket req) {
        maintenanceTicket.setTitle(req.getTitle());
        maintenanceTicket.setDescription(req.getDescription());
        maintenanceTicket.setStatus(req.getStatus());
        maintenanceTicket.setPriority(req.getPriority());
        maintenanceTicket.setResolvedAt(req.getResolvedAt());
        return maintenanceTicketRepository.save(maintenanceTicket);
    }

    public void delete(final MaintenanceTicket maintenanceTicket) {
        maintenanceTicketRepository.delete(maintenanceTicket);
    }

    @Transactional(readOnly = true)
    public List<MaintenanceTicket> list() {
        return maintenanceTicketRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<MaintenanceTicket> listByRoom(final Long roomId) {
        return maintenanceTicketRepository.findByRoom_Id(roomId);
    }

    @Transactional(readOnly = true)
    public List<MaintenanceTicket> listByReportedByUser(final Long reportedByUserId) {
        return maintenanceTicketRepository.findByReportedByUser_Id(reportedByUserId);
    }

    @Transactional(readOnly = true)
    public List<MaintenanceTicket> listByAssignedUser(final Long assignedUserId) {
        return maintenanceTicketRepository.findByAssignedUser_Id(assignedUserId);
    }
}
