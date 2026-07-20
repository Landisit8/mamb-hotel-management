package com.mamb.hotel.user_roles;

import com.mamb.hotel.roles.Role;
import com.mamb.hotel.roles.RoleRepository;
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
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserRole create(final Long userId, final Long roleId, final UserRole req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non presente"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Ruolo non presente"));

        if (userRoleRepository.existsByUser_IdAndRole_Id(userId, roleId)) {
            throw new IllegalArgumentException("Ruolo già assegnato all'utente");
        }

        req.setUser(user);
        req.setRole(role);
        return userRoleRepository.save(req);
    }

    public UserRole update(final UserRole userRole, final Long userId, final Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non presente"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Ruolo non presente"));

        if ((!userRole.getUser().getId().equals(userId) || !userRole.getRole().getId().equals(roleId))
                && userRoleRepository.existsByUser_IdAndRole_Id(userId, roleId)) {
            throw new IllegalArgumentException("Ruolo già assegnato all'utente");
        }

        userRole.setUser(user);
        userRole.setRole(role);
        return userRoleRepository.save(userRole);
    }

    public void delete(final UserRole userRole) {
        userRoleRepository.delete(userRole);
    }

    @Transactional(readOnly = true)
    public List<UserRole> list() {
        return userRoleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<UserRole> listByUser(final Long userId) {
        return userRoleRepository.findByUser_Id(userId);
    }

    @Transactional(readOnly = true)
    public List<UserRole> listByRole(final Long roleId) {
        return userRoleRepository.findByRole_Id(roleId);
    }
}
