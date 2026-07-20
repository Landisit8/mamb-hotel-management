package com.mamb.hotel.roles;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    public Role create(final Role req) {
        return roleRepository.save(req);
    }

    public Role update(final Role role, final Role req) {
        role.setName(req.getName());
        role.setDescription(req.getDescription());
        return roleRepository.save(role);
    }

    public void delete(final Role role) {
        roleRepository.delete(role);
    }

    @Transactional(readOnly = true)
    public List<Role> list() {
        return roleRepository.findAll();
    }
}
