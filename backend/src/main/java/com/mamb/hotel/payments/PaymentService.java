package com.mamb.hotel.payments;

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
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    public Payment create(final Long reservationId, final Payment req) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non presente"));
        req.setReservation(reservation);
        return paymentRepository.save(req);
    }

    public Payment update(final Payment payment, final Payment req) {
        payment.setAmount(req.getAmount());
        payment.setMethod(req.getMethod());
        payment.setStatus(req.getStatus());
        payment.setPaidAt(req.getPaidAt());
        payment.setTransactionReference(req.getTransactionReference());
        payment.setNotes(req.getNotes());
        payment.setCreatedByUserId(req.getCreatedByUserId());
        return paymentRepository.save(payment);
    }

    public void delete(final Payment payment) {
        paymentRepository.delete(payment);
    }

    @Transactional(readOnly = true)
    public List<Payment> list() {
        return paymentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Payment> listByReservation(final Long reservationId) {
        return paymentRepository.findByReservation_Id(reservationId);
    }
}
