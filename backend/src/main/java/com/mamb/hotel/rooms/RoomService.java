package com.mamb.hotel.rooms;

import com.mamb.hotel.room_types.RoomType;
import com.mamb.hotel.room_types.RoomTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;

    public Room create(final Long roomTypeId, final Room req) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Tipologia camera non presente"));
        req.setRoomType(roomType);
        return roomRepository.save(req);
    }

    public Room update(final Long id, final Room req) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Camera non presente"));
        room.setRoomNumber(req.getRoomNumber());
        room.setFloor(req.getFloor());
        room.setOperationalStatus(req.getOperationalStatus());
        room.setCleaningStatus(req.getCleaningStatus());
        room.setNotes(req.getNotes());
        return roomRepository.save(room);
    }

    public void delete(final Room room) {
        roomRepository.delete(room);
    }

    @Transactional(readOnly = true)
    public List<Room> list() {
        return roomRepository.findAll();
    }
}
