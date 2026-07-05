package com.mamb.hotel.reservations;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    @PostMapping("/customer/{primaryCustomerId}/create")
    public Reservation create(@PathVariable final Long primaryCustomerId, @Valid @RequestBody final Reservation req) {
        if (req.getId() != null && reservationRepository.existsById(req.getId()))
            throw new BadRequestException("Prenotazione gia' esistente");
        return reservationService.create(primaryCustomerId, req);
    }

    @PostMapping("/{id}/update")
    public Reservation update(@PathVariable final Long id, @Valid @RequestBody Reservation req) {
        reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prenotazione non presente"));
        req.setId(id);
        return reservationService.update(id, req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prenotazione non presente"));
        reservationService.delete(reservation);
    }

    @GetMapping(value = "/")
    public List<Reservation> list() {
        return reservationService.list();
    }

}