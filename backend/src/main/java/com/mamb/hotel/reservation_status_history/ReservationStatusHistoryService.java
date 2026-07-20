package com.mamb.hotel.reservation_status_history;

import com.mamb.hotel.reservations.Reservation;
import com.mamb.hotel.reservations.ReservationRepository;
import com.mamb.hotel.users.User;
import com.mamb.hotel.users.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationStatusHistoryService {

    private final ReservationStatusHistoryRepository reservationStatusHistoryRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public ReservationStatusHistory create(final Long reservationId, final Long changedByUserId, final ReservationStatusHistory req) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Prenotazione non presente"));
        User changedByUser = userRepository.findById(changedByUserId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non presente"));
        req.setReservation(reservation);
        req.setChangedByUser(changedByUser);
        return reservationStatusHistoryRepository.save(req);
    }

    public ReservationStatusHistory update(final ReservationStatusHistory reservationStatusHistory, final ReservationStatusHistory req) {
        reservationStatusHistory.setOldStatus(req.getOldStatus());
        reservationStatusHistory.setNewStatus(req.getNewStatus());
        reservationStatusHistory.setReason(req.getReason());
        return reservationStatusHistoryRepository.save(reservationStatusHistory);
    }

    public void delete(final ReservationStatusHistory reservationStatusHistory) {
        reservationStatusHistoryRepository.delete(reservationStatusHistory);
    }

    @Transactional(readOnly = true)
    public List<ReservationStatusHistory> list() {
        return reservationStatusHistoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<ReservationStatusHistory> listByReservation(final Long reservationId) {
        return reservationStatusHistoryRepository.findByReservation_Id(reservationId);
    }

    @Transactional(readOnly = true)
    public List<ReservationStatusHistory> listByChangedByUser(final Long changedByUserId) {
        return reservationStatusHistoryRepository.findByChangedByUser_Id(changedByUserId);
    }
}
