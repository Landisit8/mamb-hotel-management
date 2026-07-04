package com.mamb.hotel.rooms;


import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RoomRepository roomRepository;

    @PostMapping("/room-type/{roomTypeId}/create")
    public Room create(@PathVariable final Long roomTypeId, @Valid @RequestBody final Room req) {
        if (req.getId() != null && roomRepository.existsById(req.getId()))
            throw new BadRequestException("Camera gia' esistente");
        return roomService.create(roomTypeId, req);
    }

    @PostMapping("/{id}/update")
    public Room update(@PathVariable final Long id, @Valid @RequestBody Room req) {
        roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Camera non presente"));
        req.setId(id);
        return roomService.update(id, req);
    }

    @DeleteMapping("/{id}/delete")
    public void delete(@PathVariable final Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Camera non presente"));
        roomService.delete(room);
    }

    @GetMapping(value = "/")
    public List<Room> list() {
        return roomService.list();
    }

}
