package com.mamb.hotel.customer;



import com.mamb.hotel.exception.BadRequestException;
import com.mamb.hotel.exception.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerRepository customerRepository;

    @PostMapping(value = "/create")
    public Customer create(@Valid @RequestBody final Customer req) {
        if (req.getId() != null && customerRepository.existsById(req.getId()))
            throw new BadRequestException("Utente gia' esistente");
        return customerService.create(req);
    }

    @PostMapping("/{id}/update")
    public Customer update(@PathVariable final Long id, @Valid @RequestBody Customer req) {
        customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utente non presente"));
        req.setId(id);
        return customerService.update(req);
    }

    @PostMapping(value = "/{id}/delete")
    public void delete(@PathVariable final Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utente non presente"));
        customerService.delete(customer);
    }

    @GetMapping(value = "/")
    public List<Customer> list() {
        return customerService.list();
    }

}
