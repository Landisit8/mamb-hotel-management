package com.mamb.hotel.role_permissions;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rolePermissions")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionRepository rolePermissionRepository;
    private final RolePermissionService rolePermissionService;

    @PostMapping(value = "/role/{roleId}/permission/{permissionId}/create")
    public RolePermission create(@PathVariable final Long roleId, @PathVariable final Long permissionId, @Valid @RequestBody final RolePermission req) {
        if (req.getId() != null && rolePermissionRepository.existsById(req.getId()))
            throw new BadRequestException("Permesso ruolo già esistente");
        return rolePermissionService.create(roleId, permissionId, req);
    }

    @PostMapping("/{id}/role/{roleId}/permission/{permissionId}/update")
    public RolePermission update(@PathVariable final Long id, @PathVariable final Long roleId, @PathVariable final Long permissionId) {
        RolePermission rolePermission = rolePermissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Permesso ruolo non presente"));
        return rolePermissionService.update(rolePermission, roleId, permissionId);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        RolePermission rolePermission = rolePermissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Permesso ruolo non presente"));
        rolePermissionService.delete(rolePermission);
    }

    @GetMapping(value = "/")
    public List<RolePermission> list() {
        return rolePermissionService.list();
    }

    @GetMapping(value = "/role/{roleId}")
    public List<RolePermission> listByRole(@PathVariable final Long roleId) {
        return rolePermissionService.listByRole(roleId);
    }

    @GetMapping(value = "/permission/{permissionId}")
    public List<RolePermission> listByPermission(@PathVariable final Long permissionId) {
        return rolePermissionService.listByPermission(permissionId);
    }

}
