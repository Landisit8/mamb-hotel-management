package com.mamb.hotel.availability;


import com.mamb.hotel.reservations.ReservationStatus;
import com.mamb.hotel.rooms.Room;
import com.mamb.hotel.rooms.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/*
- Servizio dedicato al calcolo della disponibilità delle camere
- Motore indipendente interrogabile da Dashboard, Prenotazioni e Booking Engine
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AvailabilityService {
    private final RoomRepository roomRepository;

    // Definiamo quali stati di prenotazione occupano fisicamente la camera
    private static final List<ReservationStatus> BLOCKING_STATUES = List.of(
            ReservationStatus.PENDING,
            ReservationStatus.CONFIRMED,
            ReservationStatus.CHECKED_IN
            //  Stati CANCELLED, NO_SHOW e CHECKED_OUT non bloccano la disponibilita'
    );

    // [MAMB-02] - Metodo principale per la ricerca camere disponibili
    public List<Room> getAvailableRooms(final LocalDate startDate, final LocalDate endDate) {
        if (startDate == null || endDate == null || !endDate.isAfter(startDate)) {
            throw new IllegalArgumentException("Le date fornite per la ricerca non sono valide. Il check-out deve essere successivo al check-in.");
        }
        return roomRepository.findAvailableRooms(startDate, endDate, BLOCKING_STATUES);
    }
}
