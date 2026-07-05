package com.mamb.hotel.reservations;

import com.mamb.hotel.customer.Customer;
import com.mamb.hotel.customer.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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
        req.setPrimaryCustomer(customer);
        return reservationRepository.save(req);
    }

    public Reservation update(final Long id, final Reservation req) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non presente"));
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

    private void validateDates(final Reservation reservation) {
        LocalDate checkInDate = reservation.getCheckInDate();
        LocalDate checkOutDate = reservation.getCheckOutDate();

        if (checkInDate != null && checkOutDate != null && !checkOutDate.isAfter(checkInDate)) {
            throw new IllegalArgumentException("Check out date must be after check in date");
        }
    }
}
