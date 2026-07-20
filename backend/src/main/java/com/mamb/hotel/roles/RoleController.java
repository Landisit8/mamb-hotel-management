package com.mamb.hotel.roles;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final RoleRepository roleRepository;

    @PostMapping(value = "/create")
    public Role create(@Valid @RequestBody final Role req) {
        if (req.getId() != null && roleRepository.existsById(req.getId()))
            throw new BadRequestException("Ruolo già esistente");
        return roleService.create(req);
    }

    @PostMapping("/{id}/update")
    public Role update(@PathVariable final Long id, @Valid @RequestBody Role req) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ruolo non presente"));
        req.setId(id);
        return roleService.update(role, req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ruolo non presente"));
        roleService.delete(role);
    }

    @GetMapping(value = "/")
    public List<Role> list() {
        return roleService.list();
    }

}
