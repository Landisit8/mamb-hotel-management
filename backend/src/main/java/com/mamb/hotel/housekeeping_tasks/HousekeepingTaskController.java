package com.mamb.hotel.housekeeping_tasks;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/housekeepingTasks")
@RequiredArgsConstructor
public class HousekeepingTaskController {

    private final HousekeepingTaskRepository housekeepingTaskRepository;
    private final HousekeepingTaskService housekeepingTaskService;

    @PostMapping(value = "/room/{roomId}/create")
    public HousekeepingTask create(@PathVariable final Long roomId, @Valid @RequestBody final HousekeepingTask req) {
        if (req.getId() != null && housekeepingTaskRepository.existsById(req.getId()))
            throw new BadRequestException("Task housekeeping già esistente");
        return housekeepingTaskService.create(roomId, null, null, req);
    }

    @PostMapping(value = "/room/{roomId}/reservation/{reservationId}/create")
    public HousekeepingTask createWithReservation(@PathVariable final Long roomId, @PathVariable final Long reservationId, @Valid @RequestBody final HousekeepingTask req) {
        if (req.getId() != null && housekeepingTaskRepository.existsById(req.getId()))
            throw new BadRequestException("Task housekeeping già esistente");
        return housekeepingTaskService.create(roomId, reservationId, null, req);
    }

    @PostMapping(value = "/room/{roomId}/assignedUser/{assignedUserId}/create")
    public HousekeepingTask createWithAssignedUser(@PathVariable final Long roomId, @PathVariable final Long assignedUserId, @Valid @RequestBody final HousekeepingTask req) {
        if (req.getId() != null && housekeepingTaskRepository.existsById(req.getId()))
            throw new BadRequestException("Task housekeeping già esistente");
        return housekeepingTaskService.create(roomId, null, assignedUserId, req);
    }

    @PostMapping(value = "/room/{roomId}/reservation/{reservationId}/assignedUser/{assignedUserId}/create")
    public HousekeepingTask createComplete(@PathVariable final Long roomId, @PathVariable final Long reservationId, @PathVariable final Long assignedUserId, @Valid @RequestBody final HousekeepingTask req) {
        if (req.getId() != null && housekeepingTaskRepository.existsById(req.getId()))
            throw new BadRequestException("Task housekeeping già esistente");
        return housekeepingTaskService.create(roomId, reservationId, assignedUserId, req);
    }

    @PostMapping("/{id}/update")
    public HousekeepingTask update(@PathVariable final Long id, @Valid @RequestBody HousekeepingTask req) {
        HousekeepingTask housekeepingTask = housekeepingTaskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task housekeeping non presente"));
        req.setId(id);
        return housekeepingTaskService.update(housekeepingTask, req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        HousekeepingTask housekeepingTask = housekeepingTaskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task housekeeping non presente"));
        housekeepingTaskService.delete(housekeepingTask);
    }

    @GetMapping(value = "/")
    public List<HousekeepingTask> list() {
        return housekeepingTaskService.list();
    }

    @GetMapping(value = "/room/{roomId}")
    public List<HousekeepingTask> listByRoom(@PathVariable final Long roomId) {
        return housekeepingTaskService.listByRoom(roomId);
    }

    @GetMapping(value = "/reservation/{reservationId}")
    public List<HousekeepingTask> listByReservation(@PathVariable final Long reservationId) {
        return housekeepingTaskService.listByReservation(reservationId);
    }

    @GetMapping(value = "/assignedUser/{assignedUserId}")
    public List<HousekeepingTask> listByAssignedUser(@PathVariable final Long assignedUserId) {
        return housekeepingTaskService.listByAssignedUser(assignedUserId);
    }

}
