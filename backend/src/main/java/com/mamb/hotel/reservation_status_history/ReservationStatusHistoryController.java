package com.mamb.hotel.reservation_status_history;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservationStatusHistory")
@RequiredArgsConstructor
public class ReservationStatusHistoryController {

    private final ReservationStatusHistoryRepository reservationStatusHistoryRepository;
    private final ReservationStatusHistoryService reservationStatusHistoryService;

    @PostMapping(value = "/reservation/{reservationId}/changedByUser/{changedByUserId}/create")
    public ReservationStatusHistory create(@PathVariable final Long reservationId, @PathVariable final Long changedByUserId, @Valid @RequestBody final ReservationStatusHistory req) {
        if (req.getId() != null && reservationStatusHistoryRepository.existsById(req.getId()))
            throw new BadRequestException("Storico stato prenotazione già esistente");
        return reservationStatusHistoryService.create(reservationId, changedByUserId, req);
    }

    @PostMapping("/{id}/update")
    public ReservationStatusHistory update(@PathVariable final Long id, @Valid @RequestBody ReservationStatusHistory req) {
        ReservationStatusHistory reservationStatusHistory = reservationStatusHistoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Storico stato prenotazione non presente"));
        req.setId(id);
        return reservationStatusHistoryService.update(reservationStatusHistory, req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        ReservationStatusHistory reservationStatusHistory = reservationStatusHistoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Storico stato prenotazione non presente"));
        reservationStatusHistoryService.delete(reservationStatusHistory);
    }

    @GetMapping(value = "/")
    public List<ReservationStatusHistory> list() {
        return reservationStatusHistoryService.list();
    }

    @GetMapping(value = "/reservation/{reservationId}")
    public List<ReservationStatusHistory> listByReservation(@PathVariable final Long reservationId) {
        return reservationStatusHistoryService.listByReservation(reservationId);
    }

    @GetMapping(value = "/changedByUser/{changedByUserId}")
    public List<ReservationStatusHistory> listByChangedByUser(@PathVariable final Long changedByUserId) {
        return reservationStatusHistoryService.listByChangedByUser(changedByUserId);
    }

}
