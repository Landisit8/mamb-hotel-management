package com.mamb.hotel.reservation_charges;

import com.mamb.hotel.reservations.Reservation;
import com.mamb.hotel.reservations.ReservationRepository;
import com.mamb.hotel.services.ExtraService;
import com.mamb.hotel.services.ExtraServiceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationChargeService {

    private final ReservationChargeRepository reservationChargeRepository;
    private final ReservationRepository reservationRepository;
    private final ExtraServiceRepository extraServiceRepository;

    public ReservationCharge create(final Long reservationId, final Long serviceId, final ReservationCharge req) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non presente"));

        ExtraService extraService = null;

        if (serviceId != null) {
            extraService = extraServiceRepository.findById(serviceId)
                    .orElseThrow(() -> new EntityNotFoundException("Servizio non presente"));
        }

        req.setReservation(reservation);
        req.setExtraService(extraService);
        calculateTotalPrice(req);

        return reservationChargeRepository.save(req);
    }

    public ReservationCharge update(final ReservationCharge reservationCharge, final ReservationCharge req) {
        calculateTotalPrice(req);
        reservationCharge.setDescription(req.getDescription());
        reservationCharge.setChargeType(req.getChargeType());
        reservationCharge.setQuantity(req.getQuantity());
        reservationCharge.setUnitPrice(req.getUnitPrice());
        reservationCharge.setTotalPrice(req.getTotalPrice());
        reservationCharge.setChargedAt(req.getChargedAt());
        reservationCharge.setCreatedByUserId(req.getCreatedByUserId());
        return reservationChargeRepository.save(reservationCharge);
    }

    public void delete(final ReservationCharge reservationCharge) {
        reservationChargeRepository.delete(reservationCharge);
    }

    @Transactional(readOnly = true)
    public List<ReservationCharge> list() {
        return reservationChargeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ReservationCharge> listByReservation(final Long reservationId) {
        return reservationChargeRepository.findByReservation_Id(reservationId);
    }

    private void calculateTotalPrice(final ReservationCharge reservationCharge) {
        if (reservationCharge.getQuantity() != null && reservationCharge.getUnitPrice() != null) {
            reservationCharge.setTotalPrice(
                    reservationCharge.getUnitPrice().multiply(BigDecimal.valueOf(reservationCharge.getQuantity()))
            );
        }
    }
}
