package com.mamb.hotel.reservation_rooms;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservationRooms")
@RequiredArgsConstructor
public class ReservationRoomController {

    private final ReservationRoomRepository reservationRoomRepository;
    private final ReservationRoomService reservationRoomService;

    @PostMapping(value = "/reservation/{reservationId}/room/{roomId}/create")
    public ReservationRoom create(@PathVariable final Long reservationId, @PathVariable final Long roomId, @Valid @RequestBody final ReservationRoom req) {
        if (req.getId() != null && reservationRoomRepository.existsById(req.getId()))
            throw new BadRequestException("Camera prenotazione già esistente");
        return reservationRoomService.create(reservationId, roomId, req);
    }

    @PostMapping("/{id}/update")
    public ReservationRoom update(@PathVariable final Long id, @Valid @RequestBody ReservationRoom req) {
        ReservationRoom reservationRoom = reservationRoomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Camera prenotazione non presente"));
        req.setId(id);
        return reservationRoomService.update(reservationRoom, req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        ReservationRoom reservationRoom = reservationRoomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Camera prenotazione non presente"));
        reservationRoomService.delete(reservationRoom);
    }

    @GetMapping(value = "/")
    public List<ReservationRoom> list() {
        return reservationRoomService.list();
    }

}
