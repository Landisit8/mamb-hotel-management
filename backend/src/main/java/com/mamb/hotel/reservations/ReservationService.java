package com.mamb.hotel.reservations;

import com.mamb.hotel.customer.Customer;
import com.mamb.hotel.customer.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;

    public Reservation create(final Long primaryCustomerId, final Reservation req) {
        Customer customer = customerRepository.findById(primaryCustomerId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente non presente"));
        validateDates(req);
        req.setCode(generateUniqueCode());
        req.setPrimaryCustomer(customer);
        return reservationRepository.save(req);
    }

    public Reservation update(final Reservation reservation, final Reservation req) {
        validateDates(req);
        reservation.setCode(req.getCode());
        reservation.setCheckInDate(req.getCheckInDate());
        reservation.setCheckOutDate(req.getCheckOutDate());
        reservation.setActualCheckInAt(req.getActualCheckInAt());
        reservation.setActualCheckOutAt(req.getActualCheckOutAt());
        reservation.setAdults(req.getAdults());
        reservation.setChildren(req.getChildren());
        reservation.setStatus(req.getStatus());
        reservation.setSource(req.getSource());
        reservation.setSpecialRequests(req.getSpecialRequests());
        reservation.setTotalAmount(req.getTotalAmount());
        return reservationRepository.save(reservation);
    }

    public void delete(final Reservation reservation) { reservationRepository.delete(reservation);}

    @Transactional(readOnly = true)
    public List<Reservation> list() {
        return reservationRepository.findAll();
    }

    //  [MAMB-03] Macchina a stati
    public Reservation confirmReservation(final Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        // Solo le prenotazioni PENDING possono essere confermate
        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("Solo le prenotazioni in stato PENDING possono essere confermate. Stato attuale: " + reservation.getStatus());
        }
        reservation.setStatus(ReservationStatus.CONFIRMED);
        return reservationRepository.save(reservation);
    }

    public Reservation cancelReservation(final Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Prenotazione non presente "));
        // Impossibile cancellare la prenotazione se il cliente ha gia' fatto il  check-in o check-out
        if (reservation.getStatus() == ReservationStatus.CHECKED_IN || reservation.getStatus() == ReservationStatus.CHECKED_OUT) {
            throw new IllegalArgumentException("Impossibile cancellare una prenotazione gia' iniziata o conclusa. Stato attuale: " + reservation.getStatus());
        }
        // le camere non vengono eliminate fisicamente, passano automaticamente tra le disponibili grazie al motore A
        reservation.setStatus(ReservationStatus.CANCELLED);
        return reservationRepository.save(reservation);
    }
    // end [MAMB-03]

    private void validateDates(final Reservation reservation) {
        LocalDate checkInDate = reservation.getCheckInDate();
        LocalDate checkOutDate = reservation.getCheckOutDate();

        if (checkInDate != null && checkOutDate != null && !checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("Check out date must be after check in date");
        }
    }

    private String generateUniqueCode() {
        String code;
        int currentYear = LocalDate.now().getYear();
        do{
            //  Genera una stringa casuale di 6 caratteri alfanumerici
            String randomString = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            code = "MB-" + currentYear + "-" + randomString;
        //  Ripetere il ciclo se per caso il codice esiste gia'
        } while (reservationRepository.existsByCode(code));

        return code;
    }

    // [MAMB-04]
    public Reservation checkIn (final Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non presente"));
        // Il check-in si fa solo e solo se la prenotazione e' stata prima confermata
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalArgumentException("Impossibile effettuare il check-in su una prenotazione non confermata. Stato attuale: " + reservation.getStatus());
        }

        reservation.setStatus(ReservationStatus.CHECKED_IN);
        reservation.setActualCheckInAt(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    public Reservation checkOut (final Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non presente"));
        if (reservation.getStatus() != ReservationStatus.CHECKED_IN) {
            throw new IllegalArgumentException("Impossibile effettuare il check-out su una prenotazione non ancora check-in. Stato attuale: " + reservation.getStatus());
        }
        reservation.setStatus(ReservationStatus.CHECKED_OUT);
        reservation.setActualCheckOutAt(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    public Reservation markAsNoShow(final Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non presente"));
        // il cliente che non si presenta deve avere  una prenotazione confermata
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Solo le prenotazione confermate possono essere assegnate come no-show. stato attuale: " + reservation.getStatus());
        }

        reservation.setStatus(ReservationStatus.NO_SHOW);
        return reservationRepository.save(reservation);
    }
    // end [MAMB-04]
}
