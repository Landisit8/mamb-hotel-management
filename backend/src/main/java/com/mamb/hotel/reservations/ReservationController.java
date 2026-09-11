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
            throw new BadRequestException("Prenotazione già esistente");
        return reservationService.create(primaryCustomerId, req);
    }

    @PostMapping("/{id}/update")
    public Reservation update(@PathVariable final Long id, @Valid @RequestBody Reservation req) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prenotazione non presente"));
        req.setId(id);
        return reservationService.update(reservation, req);
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

    //  [MAMB-03] endpoint della conferma e cancellazione di una prenotazione
    @PostMapping("/{id}/confirm")
    public Reservation confirm(@PathVariable final Long id){
        reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prenotazione non presente"));
        return reservationService.confirmReservation(id);
    }

    @PostMapping("/{id}/cancel")
    public Reservation cancel(@PathVariable final Long id){
        reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prenotazione non presente"));
        return reservationService.cancelReservation(id);
    }

    // [MAMB-04] checkIn, checkOUT e NoShow
    @PostMapping("/{id}/checkIn")
    public Reservation checkIn(@PathVariable final Long id){
        reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prenotazione non presente"));
        return reservationService.checkIn(id);
    }

    @PostMapping("/{id}/checkOut")
    public Reservation checkOut(@PathVariable final Long id){
        reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prenotazione non presente"));
        return reservationService.checkOut(id);
    }

    @PostMapping("/{id}/noShow")
    public Reservation noShow(@PathVariable final Long id){
        reservationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prenotazione non presente"));
        return reservationService.markAsNoShow(id); 
    }
}
