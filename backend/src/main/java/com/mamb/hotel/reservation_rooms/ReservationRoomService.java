package com.mamb.hotel.reservation_rooms;

import com.mamb.hotel.reservations.Reservation;
import com.mamb.hotel.reservations.ReservationRepository;
import com.mamb.hotel.reservations.ReservationStatus;
import com.mamb.hotel.room_blocks.RoomBlockRepository;
import com.mamb.hotel.rooms.OperationalStatus;
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
    private final RoomBlockRepository roomBlockRepository;

    public ReservationRoom create(final Long reservationId, final Long roomId, final ReservationRoom req) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non presente"));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Camera non presente"));
        // [MAMB-01] Validazione delle date inserite per l'assegnazione
        validateDates(req);
        // [MAMB-01] Controllo disponibilita' camera (overbooking e manutenzione)
        validateRoom(room, req.getStartDate(), req.getEndDate(), null);
        req.setReservation(reservation);
        req.setRoom(room);
        return reservationRoomRepository.save(req);
    }

    public ReservationRoom update(final ReservationRoom reservationRoom, final ReservationRoom req) {
        // [MAMB-01] Validazione date in fase di aggiornamento
        validateDates(req);
        // [MAMB-01] Controllo disponibilita' escludendo l'assegnazione corrente
        // Passiamo l'id estraendolo dell'entita' originale.
        validateRoom(reservationRoom.getRoom(), req.getStartDate(), req.getEndDate(), reservationRoom.getId());
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

    // [MAMB-01] regola: la data di fine assegnazione deve essere successiva all'inizio
    private void validateDates(final ReservationRoom reservationRoom) {
        LocalDate startDate = reservationRoom.getStartDate();
        LocalDate endDate = reservationRoom.getEndDate();

        if (startDate != null && endDate != null && !endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }

    // [MAMB-01] motore base di controllo disponibilita' per l'assegnazione
    private void validateRoom(final Room room, final LocalDate startDate, final LocalDate endDate, final Long reservationRoomId) {
        if (room.getOperationalStatus() != OperationalStatus.ACTIVE) {
            throw new IllegalArgumentException("Camera non prenotabile (Stato: " + room.getOperationalStatus() + ")");
        }
        if (roomBlockRepository.existsOverlappingRoomBlock(room.getId(), startDate, endDate)) {
            throw new IllegalArgumentException("Camera bloccata nel periodo richiesto");
        }
        boolean alreadyAssigned = reservationRoomRepository.existsOverlappingRoomAssignment(room.getId(), startDate, endDate, reservationRoomId, List.of(ReservationStatus.CONFIRMED, ReservationStatus.CHECKED_IN));
        if (alreadyAssigned) {
            throw new IllegalArgumentException("Camera gia' assegnata nel periodo richiesto");
        }
    }
}
