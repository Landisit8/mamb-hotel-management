package com.mamb.hotel.room_blocks;

import com.mamb.hotel.rooms.Room;
import com.mamb.hotel.rooms.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomBlockService {
    private final RoomBlockRepository roomBlockRepository;
    private final RoomRepository roomRepository;

    public RoomBlock create(final Long roomId, final RoomBlock req) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Camera non presente"));
        req.setRoom(room);
        return roomBlockRepository.save(req);
    }

    public RoomBlock update(final Long id, final RoomBlock req) {
        RoomBlock roomBlock = roomBlockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Blocco camera non presente"));
        roomBlock.setStartDate(req.getStartDate());
        roomBlock.setEndDate(req.getEndDate());
        roomBlock.setBlockType(req.getBlockType());
        roomBlock.setReason(req.getReason());
        roomBlock.setCreatedByUserId(req.getCreatedByUserId());
        return roomBlockRepository.save(roomBlock);
    }

    public void delete(final RoomBlock roomBlock) {roomBlockRepository.delete(roomBlock);}

    @Transactional(readOnly = true)
    public List<RoomBlock> list() {
        return roomBlockRepository.findAll();
    }
}
