package com.mamb.hotel.permissions;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public Permission create(final Permission req) {
        return permissionRepository.save(req);
    }

    public Permission update(final Permission permission, final Permission req) {
        permission.setName(req.getName());
        permission.setDescription(req.getDescription());
        return permissionRepository.save(permission);
    }

    public void delete(final Permission permission) {
        permissionRepository.delete(permission);
    }

    @Transactional(readOnly = true)
    public List<Permission> list() {
        return permissionRepository.findAll();
    }
}
