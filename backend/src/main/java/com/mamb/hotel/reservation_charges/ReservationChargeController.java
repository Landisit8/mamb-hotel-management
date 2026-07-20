package com.mamb.hotel.reservation_charges;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservationCharges")
@RequiredArgsConstructor
public class ReservationChargeController {

    private final ReservationChargeRepository reservationChargeRepository;
    private final ReservationChargeService reservationChargeService;

    @PostMapping(value = "/reservation/{reservationId}/create")
    public ReservationCharge create(@PathVariable final Long reservationId, @Valid @RequestBody final ReservationCharge req) {
        if (req.getId() != null && reservationChargeRepository.existsById(req.getId()))
            throw new BadRequestException("Addebito prenotazione già esistente");
        return reservationChargeService.create(reservationId, null, req);
    }

    @PostMapping(value = "/reservation/{reservationId}/service/{serviceId}/create")
    public ReservationCharge createWithService(@PathVariable final Long reservationId, @PathVariable final Long serviceId, @Valid @RequestBody final ReservationCharge req) {
        if (req.getId() != null && reservationChargeRepository.existsById(req.getId()))
            throw new BadRequestException("Addebito prenotazione già esistente");
        return reservationChargeService.create(reservationId, serviceId, req);
    }

    @PostMapping("/{id}/update")
    public ReservationCharge update(@PathVariable final Long id, @Valid @RequestBody ReservationCharge req) {
        ReservationCharge reservationCharge = reservationChargeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Addebito prenotazione non presente"));
        req.setId(id);
        return reservationChargeService.update(reservationCharge, req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        ReservationCharge reservationCharge = reservationChargeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Addebito prenotazione non presente"));
        reservationChargeService.delete(reservationCharge);
    }

    @GetMapping(value = "/")
    public List<ReservationCharge> list() {
        return reservationChargeService.list();
    }

    @GetMapping(value = "/reservation/{reservationId}")
    public List<ReservationCharge> listByReservation(@PathVariable final Long reservationId) {
        return reservationChargeService.listByReservation(reservationId);
    }

}
