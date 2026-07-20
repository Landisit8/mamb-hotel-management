package com.mamb.hotel.audit_logs;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditLogs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogService auditLogService;

    @PostMapping(value = "/create")
    public AuditLog create(@Valid @RequestBody final AuditLog req) {
        if (req.getId() != null && auditLogRepository.existsById(req.getId()))
            throw new BadRequestException("Audit log già esistente");
        return auditLogService.create(null, req);
    }

    @PostMapping(value = "/user/{userId}/create")
    public AuditLog createWithUser(@PathVariable final Long userId, @Valid @RequestBody final AuditLog req) {
        if (req.getId() != null && auditLogRepository.existsById(req.getId()))
            throw new BadRequestException("Audit log già esistente");
        return auditLogService.create(userId, req);
    }

    @PostMapping("/{id}/update")
    public AuditLog update(@PathVariable final Long id, @Valid @RequestBody AuditLog req) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Audit log non presente"));
        req.setId(id);
        return auditLogService.update(auditLog, req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Audit log non presente"));
        auditLogService.delete(auditLog);
    }

    @GetMapping(value = "/")
    public List<AuditLog> list() {
        return auditLogService.list();
    }

    @GetMapping(value = "/user/{userId}")
    public List<AuditLog> listByUser(@PathVariable final Long userId) {
        return auditLogService.listByUser(userId);
    }

    @GetMapping(value = "/entityName/{entityName}")
    public List<AuditLog> listByEntityName(@PathVariable final String entityName) {
        return auditLogService.listByEntityName(entityName);
    }

    @GetMapping(value = "/entityName/{entityName}/entityId/{entityId}")
    public List<AuditLog> listByEntityNameAndEntityId(@PathVariable final String entityName, @PathVariable final Long entityId) {
        return auditLogService.listByEntityNameAndEntityId(entityName, entityId);
    }

}
