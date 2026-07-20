package com.mamb.hotel.user_roles;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userRoles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleRepository userRoleRepository;
    private final UserRoleService userRoleService;

    @PostMapping(value = "/user/{userId}/role/{roleId}/create")
    public UserRole create(@PathVariable final Long userId, @PathVariable final Long roleId, @Valid @RequestBody final UserRole req) {
        if (req.getId() != null && userRoleRepository.existsById(req.getId()))
            throw new BadRequestException("Ruolo utente già esistente");
        return userRoleService.create(userId, roleId, req);
    }

    @PostMapping("/{id}/user/{userId}/role/{roleId}/update")
    public UserRole update(@PathVariable final Long id, @PathVariable final Long userId, @PathVariable final Long roleId) {
        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ruolo utente non presente"));
        return userRoleService.update(userRole, userId, roleId);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        UserRole userRole = userRoleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ruolo utente non presente"));
        userRoleService.delete(userRole);
    }

    @GetMapping(value = "/")
    public List<UserRole> list() {
        return userRoleService.list();
    }

    @GetMapping(value = "/user/{userId}")
    public List<UserRole> listByUser(@PathVariable final Long userId) {
        return userRoleService.listByUser(userId);
    }

    @GetMapping(value = "/role/{roleId}")
    public List<UserRole> listByRole(@PathVariable final Long roleId) {
        return userRoleService.listByRole(roleId);
    }

}
