package com.mamb.hotel.customer_document;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerDocumentController {

    private final CustomerDocumentService customerDocumentService;

    @PostMapping("/customers/{customerId}/documents")
    public ResponseEntity<CustomerDocument> create(
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerDocument request
    ) {
        CustomerDocument createdDocument = customerDocumentService.create(customerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDocument);
    }

    @GetMapping("/customer-documents")
    public ResponseEntity<List<CustomerDocument>> findAll() {
        return ResponseEntity.ok(customerDocumentService.findAll());
    }

    @GetMapping("/customers/{customerId}/documents")
    public ResponseEntity<List<CustomerDocument>> findByCustomerId(
            @PathVariable Long customerId
    ) {
        return ResponseEntity.ok(customerDocumentService.findByCustomerId(customerId));
    }

    @GetMapping("/customer-documents/{id}")
    public ResponseEntity<CustomerDocument> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(customerDocumentService.findById(id));
    }

    @PutMapping("/customer-documents/{id}")
    public ResponseEntity<CustomerDocument> update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerDocument request
    ) {
        return ResponseEntity.ok(customerDocumentService.update(id, request));
    }

    @DeleteMapping("/customer-documents/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        customerDocumentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}