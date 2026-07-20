package com.mamb.hotel.room_blocks;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roomBlocks")
@RequiredArgsConstructor
public class RoomBlockController {

    private final RoomBlockRepository roomBlockRepository;
    private final RoomBlockService roomBlockService;

    @PostMapping(value = "/room/{roomId}/create")
    public RoomBlock create(@PathVariable final Long roomId, @Valid @RequestBody final RoomBlock req) {
        if (req.getId() != null && roomBlockRepository.existsById(req.getId()))
            throw new BadRequestException("Tipologia camera già esistente");
        return roomBlockService.create(roomId, req);
    }

    @PostMapping("/{id}/update")
    public RoomBlock update(@PathVariable final Long id, @Valid @RequestBody RoomBlock req) {
        RoomBlock roomBlock = roomBlockRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipologia camera non presente"));
        req.setId(id);
        return roomBlockService.update(roomBlock, req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        RoomBlock roomBlock = roomBlockRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipologia camera non presente"));
        roomBlockService.delete(roomBlock);
    }

    @GetMapping(value = "/")
    public List<RoomBlock> list() {
        return roomBlockService.list();
    }


}
