package com.mamb.hotel.users;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User create(final User req) {
        return userRepository.save(req);
    }

    public User update(final User user, final User req) {
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPasswordHash(req.getPasswordHash());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setActive(req.getActive());
        return userRepository.save(user);
    }

    public void delete(final User user) {
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<User> list() {
        return userRepository.findAll();
    }
}
