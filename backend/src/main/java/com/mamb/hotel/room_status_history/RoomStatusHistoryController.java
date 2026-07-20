package com.mamb.hotel.room_status_history;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roomStatusHistory")
@RequiredArgsConstructor
public class RoomStatusHistoryController {

    private final RoomStatusHistoryRepository roomStatusHistoryRepository;
    private final RoomStatusHistoryService roomStatusHistoryService;

    @PostMapping(value = "/room/{roomId}/changedByUser/{changedByUserId}/create")
    public RoomStatusHistory create(@PathVariable final Long roomId, @PathVariable final Long changedByUserId, @Valid @RequestBody final RoomStatusHistory req) {
        if (req.getId() != null && roomStatusHistoryRepository.existsById(req.getId()))
            throw new BadRequestException("Storico stato camera già esistente");
        return roomStatusHistoryService.create(roomId, changedByUserId, req);
    }

    @PostMapping("/{id}/update")
    public RoomStatusHistory update(@PathVariable final Long id, @Valid @RequestBody RoomStatusHistory req) {
        RoomStatusHistory roomStatusHistory = roomStatusHistoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Storico stato camera non presente"));
        req.setId(id);
        return roomStatusHistoryService.update(roomStatusHistory, req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        RoomStatusHistory roomStatusHistory = roomStatusHistoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Storico stato camera non presente"));
        roomStatusHistoryService.delete(roomStatusHistory);
    }

    @GetMapping(value = "/")
    public List<RoomStatusHistory> list() {
        return roomStatusHistoryService.list();
    }

    @GetMapping(value = "/room/{roomId}")
    public List<RoomStatusHistory> listByRoom(@PathVariable final Long roomId) {
        return roomStatusHistoryService.listByRoom(roomId);
    }

    @GetMapping(value = "/changedByUser/{changedByUserId}")
    public List<RoomStatusHistory> listByChangedByUser(@PathVariable final Long changedByUserId) {
        return roomStatusHistoryService.listByChangedByUser(changedByUserId);
    }

}
