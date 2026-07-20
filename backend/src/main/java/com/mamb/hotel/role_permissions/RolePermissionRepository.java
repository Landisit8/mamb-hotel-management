package com.mamb.hotel.role_permissions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    boolean existsByRole_IdAndPermission_Id(Long roleId, Long permissionId);

    List<RolePermission> findByRole_Id(Long roleId);

    List<RolePermission> findByPermission_Id(Long permissionId);
}