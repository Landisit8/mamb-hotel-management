package com.mamb.hotel.audit_logs;

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
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    public AuditLog create(final Long userId, final AuditLog req) {
        User user = null;

        if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Utente non presente"));
        }

        req.setUser(user);
        return auditLogRepository.save(req);
    }

    public AuditLog update(final AuditLog auditLog, final AuditLog req) {
        auditLog.setAction(req.getAction());
        auditLog.setEntityName(req.getEntityName());
        auditLog.setEntityId(req.getEntityId());
        auditLog.setOldValue(req.getOldValue());
        auditLog.setNewValue(req.getNewValue());
        return auditLogRepository.save(auditLog);
    }

    public void delete(final AuditLog auditLog) {
        auditLogRepository.delete(auditLog);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> list() {
        return auditLogRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<AuditLog> listByUser(final Long userId) {
        return auditLogRepository.findByUser_Id(userId);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> listByEntityName(final String entityName) {
        return auditLogRepository.findByEntityName(entityName);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> listByEntityNameAndEntityId(final String entityName, final Long entityId) {
        return auditLogRepository.findByEntityNameAndEntityId(entityName, entityId);
    }
}
