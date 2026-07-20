package com.mamb.hotel.maintenance_tickets;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenanceTickets")
@RequiredArgsConstructor
public class MaintenanceTicketController {

    private final MaintenanceTicketRepository maintenanceTicketRepository;
    private final MaintenanceTicketService maintenanceTicketService;

    @PostMapping(value = "/room/{roomId}/reportedByUser/{reportedByUserId}/create")
    public MaintenanceTicket create(@PathVariable final Long roomId, @PathVariable final Long reportedByUserId, @Valid @RequestBody final MaintenanceTicket req) {
        if (req.getId() != null && maintenanceTicketRepository.existsById(req.getId()))
            throw new BadRequestException("Ticket manutenzione già esistente");
        return maintenanceTicketService.create(roomId, reportedByUserId, null, req);
    }

    @PostMapping(value = "/room/{roomId}/reportedByUser/{reportedByUserId}/assignedUser/{assignedUserId}/create")
    public MaintenanceTicket createWithAssignedUser(@PathVariable final Long roomId, @PathVariable final Long reportedByUserId, @PathVariable final Long assignedUserId, @Valid @RequestBody final MaintenanceTicket req) {
        if (req.getId() != null && maintenanceTicketRepository.existsById(req.getId()))
            throw new BadRequestException("Ticket manutenzione già esistente");
        return maintenanceTicketService.create(roomId, reportedByUserId, assignedUserId, req);
    }

    @PostMapping("/{id}/update")
    public MaintenanceTicket update(@PathVariable final Long id, @Valid @RequestBody MaintenanceTicket req) {
        MaintenanceTicket maintenanceTicket = maintenanceTicketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket manutenzione non presente"));
        req.setId(id);
        return maintenanceTicketService.update(maintenanceTicket, req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        MaintenanceTicket maintenanceTicket = maintenanceTicketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket manutenzione non presente"));
        maintenanceTicketService.delete(maintenanceTicket);
    }

    @GetMapping(value = "/")
    public List<MaintenanceTicket> list() {
        return maintenanceTicketService.list();
    }

    @GetMapping(value = "/room/{roomId}")
    public List<MaintenanceTicket> listByRoom(@PathVariable final Long roomId) {
        return maintenanceTicketService.listByRoom(roomId);
    }

    @GetMapping(value = "/reportedByUser/{reportedByUserId}")
    public List<MaintenanceTicket> listByReportedByUser(@PathVariable final Long reportedByUserId) {
        return maintenanceTicketService.listByReportedByUser(reportedByUserId);
    }

    @GetMapping(value = "/assignedUser/{assignedUserId}")
    public List<MaintenanceTicket> listByAssignedUser(@PathVariable final Long assignedUserId) {
        return maintenanceTicketService.listByAssignedUser(assignedUserId);
    }

}
