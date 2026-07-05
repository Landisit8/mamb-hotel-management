package com.mamb.hotel.reservation_rooms;

import com.mamb.hotel.reservations.Reservation;
import com.mamb.hotel.reservations.ReservationRepository;
import com.mamb.hotel.rooms.Room;
import com.mamb.hotel.rooms.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationRoomService {

    private final ReservationRoomRepository reservationRoomRepository;
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public ReservationRoom create(final Long reservationId, final Long roomId, final ReservationRoom req) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non presente"));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Camera non presente"));
        validateDates(req);
        req.setReservation(reservation);
        req.setRoom(room);
        return reservationRoomRepository.save(req);
    }

    public ReservationRoom update(final Long id, final ReservationRoom req) {
        ReservationRoom reservationRoom = reservationRoomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Camera prenotazione non presente"));
        validateDates(req);
        reservationRoom.setStartDate(req.getStartDate());
        reservationRoom.setEndDate(req.getEndDate());
        reservationRoom.setPricePerNight(req.getPricePerNight());
        return reservationRoomRepository.save(reservationRoom);
    }

    public void delete(final ReservationRoom reservationRoom) { reservationRoomRepository.delete(reservationRoom);}

    @Transactional(readOnly = true)
    public List<ReservationRoom> list() {
        return reservationRoomRepository.findAll();
    }

    private void validateDates(final ReservationRoom reservationRoom) {
        LocalDate startDate = reservationRoom.getStartDate();
        LocalDate endDate = reservationRoom.getEndDate();

        if (startDate != null && endDate != null && !endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }
}
