package com.mamb.hotel.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer create(final Customer req) {
        return customerRepository.save(req);
    }

    public Customer update(final Customer req) {
        return customerRepository.save(req);
    }

    public void delete(final Customer customer) {
        customerRepository.delete(customer);
    }

    @Transactional(readOnly = true)
    public List<Customer> list() {
        return customerRepository.findAll();
    }

}
