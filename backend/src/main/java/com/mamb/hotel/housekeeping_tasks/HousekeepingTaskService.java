package com.mamb.hotel.housekeeping_tasks;

import com.mamb.hotel.reservations.Reservation;
import com.mamb.hotel.reservations.ReservationRepository;
import com.mamb.hotel.rooms.Room;
import com.mamb.hotel.rooms.RoomRepository;
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
public class HousekeepingTaskService {

    private final HousekeepingTaskRepository housekeepingTaskRepository;
    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public HousekeepingTask create(final Long roomId, final Long reservationId, final Long assignedUserId, final HousekeepingTask req) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Camera non presente"));

        Reservation reservation = null;

        if (reservationId != null) {
            reservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new EntityNotFoundException("Prenotazione non presente"));
        }

        User assignedUser = null;

        if (assignedUserId != null) {
            assignedUser = userRepository.findById(assignedUserId)
                    .orElseThrow(() -> new EntityNotFoundException("Utente non presente"));
        }

        validateDates(req);
        req.setRoom(room);
        req.setReservation(reservation);
        req.setAssignedUser(assignedUser);
        return housekeepingTaskRepository.save(req);
    }

    public HousekeepingTask update(final HousekeepingTask housekeepingTask, final HousekeepingTask req) {
        validateDates(req);
        housekeepingTask.setStatus(req.getStatus());
        housekeepingTask.setPriority(req.getPriority());
        housekeepingTask.setTaskType(req.getTaskType());
        housekeepingTask.setDueDate(req.getDueDate());
        housekeepingTask.setStartedAt(req.getStartedAt());
        housekeepingTask.setCompletedAt(req.getCompletedAt());
        housekeepingTask.setNotes(req.getNotes());
        return housekeepingTaskRepository.save(housekeepingTask);
    }

    public void delete(final HousekeepingTask housekeepingTask) {
        housekeepingTaskRepository.delete(housekeepingTask);
    }

    @Transactional(readOnly = true)
    public List<HousekeepingTask> list() {
        return housekeepingTaskRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<HousekeepingTask> listByRoom(final Long roomId) {
        return housekeepingTaskRepository.findByRoom_Id(roomId);
    }

    @Transactional(readOnly = true)
    public List<HousekeepingTask> listByReservation(final Long reservationId) {
        return housekeepingTaskRepository.findByReservation_Id(reservationId);
    }

    @Transactional(readOnly = true)
    public List<HousekeepingTask> listByAssignedUser(final Long assignedUserId) {
        return housekeepingTaskRepository.findByAssignedUser_Id(assignedUserId);
    }

    private void validateDates(final HousekeepingTask housekeepingTask) {
        if (housekeepingTask.getStartedAt() != null
                && housekeepingTask.getCompletedAt() != null
                && housekeepingTask.getCompletedAt().isBefore(housekeepingTask.getStartedAt())) {
            throw new IllegalArgumentException("Completed at must be after started at");
        }
    }
}
