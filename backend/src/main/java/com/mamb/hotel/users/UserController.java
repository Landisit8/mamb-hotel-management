package com.mamb.hotel.users;

import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping(value = "/create")
    public User create(@Valid @RequestBody final User req) {
        if (req.getId() != null && userRepository.existsById(req.getId()))
            throw new BadRequestException("Utente già esistente");
        return userService.create(req);
    }

    @PostMapping("/{id}/update")
    public User update(@PathVariable final Long id, @Valid @RequestBody User req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utente non presente"));
        req.setId(id);
        return userService.update(user, req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utente non presente"));
        userService.delete(user);
    }

    @GetMapping(value = "/")
    public List<User> list() {
        return userService.list();
    }

}
