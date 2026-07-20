package com.mamb.hotel.permissions;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;
    private final PermissionRepository permissionRepository;

    @PostMapping(value = "/create")
    public Permission create(@Valid @RequestBody final Permission req) {
        if (req.getId() != null && permissionRepository.existsById(req.getId()))
            throw new BadRequestException("Permesso già esistente");
        return permissionService.create(req);
    }

    @PostMapping("/{id}/update")
    public Permission update(@PathVariable final Long id, @Valid @RequestBody Permission req) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Permesso non presente"));
        req.setId(id);
        return permissionService.update(permission, req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Permesso non presente"));
        permissionService.delete(permission);
    }

    @GetMapping(value = "/")
    public List<Permission> list() {
        return permissionService.list();
    }

}
