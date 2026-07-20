package com.mamb.hotel.reservation_guests;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservationGuests")
@RequiredArgsConstructor
public class ReservationGuestController {

    private final ReservationGuestRepository reservationGuestRepository;
    private final ReservationGuestService reservationGuestService;

    @PostMapping(value = "/reservation/{reservationId}/customer/{customerId}/create")
    public ReservationGuest create(@PathVariable final Long reservationId, @PathVariable final Long customerId, @Valid @RequestBody final ReservationGuest req) {
        if (req.getId() != null && reservationGuestRepository.existsById(req.getId()))
            throw new BadRequestException("Ospite prenotazione già esistente");
        return reservationGuestService.create(reservationId, customerId, req);
    }

    @PostMapping("/{id}/update")
    public ReservationGuest update(@PathVariable final Long id, @Valid @RequestBody ReservationGuest req) {
        ReservationGuest reservationGuest = reservationGuestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ospite prenotazione non presente"));
        req.setId(id);
        return reservationGuestService.update(reservationGuest, req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        ReservationGuest reservationGuest = reservationGuestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ospite prenotazione non presente"));
        reservationGuestService.delete(reservationGuest);
    }

    @GetMapping(value = "/")
    public List<ReservationGuest> list() {
        return reservationGuestService.list();
    }
}
