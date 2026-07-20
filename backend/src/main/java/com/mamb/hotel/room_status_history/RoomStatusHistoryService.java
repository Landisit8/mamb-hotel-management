package com.mamb.hotel.room_status_history;

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
public class RoomStatusHistoryService {

    private final RoomStatusHistoryRepository roomStatusHistoryRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public RoomStatusHistory create(final Long roomId, final Long changedByUserId, final RoomStatusHistory req) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Camera non presente"));
        User changedByUser = userRepository.findById(changedByUserId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non presente"));
        validateStatus(req);
        req.setRoom(room);
        req.setChangedByUser(changedByUser);
        return roomStatusHistoryRepository.save(req);
    }

    public RoomStatusHistory update(final RoomStatusHistory roomStatusHistory, final RoomStatusHistory req) {
        validateStatus(req);
        roomStatusHistory.setOldOperationalStatus(req.getOldOperationalStatus());
        roomStatusHistory.setNewOperationalStatus(req.getNewOperationalStatus());
        roomStatusHistory.setOldCleaningStatus(req.getOldCleaningStatus());
        roomStatusHistory.setNewCleaningStatus(req.getNewCleaningStatus());
        roomStatusHistory.setReason(req.getReason());
        return roomStatusHistoryRepository.save(roomStatusHistory);
    }

    public void delete(final RoomStatusHistory roomStatusHistory) {
        roomStatusHistoryRepository.delete(roomStatusHistory);
    }

    @Transactional(readOnly = true)
    public List<RoomStatusHistory> list() {
        return roomStatusHistoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<RoomStatusHistory> listByRoom(final Long roomId) {
        return roomStatusHistoryRepository.findByRoom_Id(roomId);
    }

    @Transactional(readOnly = true)
    public List<RoomStatusHistory> listByChangedByUser(final Long changedByUserId) {
        return roomStatusHistoryRepository.findByChangedByUser_Id(changedByUserId);
    }

    private void validateStatus(final RoomStatusHistory roomStatusHistory) {
        if (roomStatusHistory.getNewOperationalStatus() == null && roomStatusHistory.getNewCleaningStatus() == null) {
            throw new IllegalArgumentException("At least one new room status is required");
        }
    }
}
