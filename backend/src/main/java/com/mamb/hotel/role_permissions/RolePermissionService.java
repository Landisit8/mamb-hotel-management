package com.mamb.hotel.role_permissions;

import com.mamb.hotel.permissions.Permission;
import com.mamb.hotel.permissions.PermissionRepository;
import com.mamb.hotel.roles.Role;
import com.mamb.hotel.roles.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RolePermission create(final Long roleId, final Long permissionId, final RolePermission req) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Ruolo non presente"));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new EntityNotFoundException("Permesso non presente"));

        if (rolePermissionRepository.existsByRole_IdAndPermission_Id(roleId, permissionId)) {
            throw new IllegalArgumentException("Permesso già assegnato al ruolo");
        }

        req.setRole(role);
        req.setPermission(permission);
        return rolePermissionRepository.save(req);
    }

    public RolePermission update(final RolePermission rolePermission, final Long roleId, final Long permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Ruolo non presente"));
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new EntityNotFoundException("Permesso non presente"));

        if ((!rolePermission.getRole().getId().equals(roleId) || !rolePermission.getPermission().getId().equals(permissionId))
                && rolePermissionRepository.existsByRole_IdAndPermission_Id(roleId, permissionId)) {
            throw new IllegalArgumentException("Permesso già assegnato al ruolo");
        }

        rolePermission.setRole(role);
        rolePermission.setPermission(permission);
        return rolePermissionRepository.save(rolePermission);
    }

    public void delete(final RolePermission rolePermission) {
        rolePermissionRepository.delete(rolePermission);
    }

    @Transactional(readOnly = true)
    public List<RolePermission> list() {
        return rolePermissionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<RolePermission> listByRole(final Long roleId) {
        return rolePermissionRepository.findByRole_Id(roleId);
    }

    @Transactional(readOnly = true)
    public List<RolePermission> listByPermission(final Long permissionId) {
        return rolePermissionRepository.findByPermission_Id(permissionId);
    }
}
