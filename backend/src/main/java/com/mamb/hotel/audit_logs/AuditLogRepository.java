package com.mamb.hotel.audit_logs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByUser_Id(Long userId);

    List<AuditLog> findByEntityName(String entityName);

    List<AuditLog> findByEntityNameAndEntityId(String entityName, Long entityId);
}