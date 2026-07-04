package com.mamb.hotel.room_types;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    public RoomType create(final RoomType req) {
        return roomTypeRepository.save(req);
    }

    public RoomType update(final RoomType req) {
        return roomTypeRepository.save(req);
    }

    public void delete(final RoomType roomType) {
        roomTypeRepository.delete(roomType);
    }

    @Transactional(readOnly = true)
    public List<RoomType> list() {
        return roomTypeRepository.findAll();
    }
}
