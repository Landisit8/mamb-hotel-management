package com.mamb.hotel.reservation_guests;

import com.mamb.hotel.customer.Customer;
import com.mamb.hotel.customer.CustomerRepository;
import com.mamb.hotel.reservations.Reservation;
import com.mamb.hotel.reservations.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationGuestService {

    private final ReservationGuestRepository reservationGuestRepository;
    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;

    public ReservationGuest create(final Long reservationId, final Long customerId, final ReservationGuest req) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non presente"));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente non presente"));
        // Impedisce di inserire lo stesso cliente due volte nella stessa prenotazione
        if (reservationGuestRepository.existsByReservation_IdAndCustomer_Id(reservationId, customerId)) {
            throw new IllegalStateException("Cliente gia' presente nella prenotazione");
        }
        // Garantire un solo ospite principale
        if (req.getRole() == ReservationGuestRole.PRIMARY &&
            reservationGuestRepository.existsByReservation_IdAndRole(reservationId, ReservationGuestRole.PRIMARY)) {
            throw new IllegalStateException("Esiste gia' un ospite principale per questa prenotazione ");
        }

        req.setReservation(reservation);
        req.setCustomer(customer);
        return reservationGuestRepository.save(req);
    }

    public ReservationGuest update(final ReservationGuest reservationGuest, final ReservationGuest req) {
        reservationGuest.setRole(req.getRole());
        return reservationGuestRepository.save(reservationGuest);
    }

    public void delete(final ReservationGuest reservationGuest) {
        reservationGuestRepository.delete(reservationGuest);
    }

    @Transactional(readOnly = true)
    public List<ReservationGuest> list() {
        return reservationGuestRepository.findAll();
    }
}
