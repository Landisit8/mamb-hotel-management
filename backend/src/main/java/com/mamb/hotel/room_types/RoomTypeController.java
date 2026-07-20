package com.mamb.hotel.room_types;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roomTypes")
@RequiredArgsConstructor
public class RoomTypeController {

    private final RoomTypeService roomTypeService;
    private final RoomTypeRepository roomTypeRepository;

    @PostMapping(value = "/create")
    public RoomType create(@Valid @RequestBody final RoomType req) {
        if (req.getId() != null && roomTypeRepository.existsById(req.getId()))
            throw new BadRequestException("Tipologia camera già esistente");
        return roomTypeService.create(req);
    }

    @PostMapping("/{id}/update")
    public RoomType update(@PathVariable final Long id, @Valid @RequestBody RoomType req) {
        roomTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipologia camera non presente"));
        req.setId(id);
        return roomTypeService.update(req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tipologia camera non presente"));
        roomTypeService.delete(roomType);
    }

    @GetMapping(value = "/")
    public List<RoomType> list() {
        return roomTypeService.list();
    }

}
