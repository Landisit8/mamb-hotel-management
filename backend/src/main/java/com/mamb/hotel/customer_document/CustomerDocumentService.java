package com.mamb.hotel.customer_document;

import com.mamb.hotel.customer.Customer;
import com.mamb.hotel.customer.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerDocumentService {

    private final CustomerDocumentRepository customerDocumentRepository;
    private final CustomerRepository customerRepository;

    public CustomerDocument create(Long customerId, CustomerDocument request) {
        Customer customer = findCustomerById(customerId);

        if (customerDocumentRepository.existsByCustomer_IdAndDocumentTypeAndDocumentNumber(
                customerId,
                request.getDocumentType(),
                request.getDocumentNumber()
        )) {
            throw new IllegalArgumentException("This document already exists for this customer");
        }

        validateDates(request);

        CustomerDocument document = new CustomerDocument();
        document.setCustomer(customer);

        copyFields(request, document);

        if (document.getVerificationStatus() == null) {
            document.setVerificationStatus(DocumentVerificationStatus.NOT_VERIFIED);
        }

        return customerDocumentRepository.save(document);
    }

    @Transactional(readOnly = true)
    public List<CustomerDocument> findAll() {
        return customerDocumentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<CustomerDocument> findByCustomerId(Long customerId) {
        findCustomerById(customerId);
        return customerDocumentRepository.findByCustomer_Id(customerId);
    }

    @Transactional(readOnly = true)
    public CustomerDocument findById(Long id) {
        return customerDocumentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer document not found with id: " + id));
    }

    public CustomerDocument update(Long id, CustomerDocument request) {
        CustomerDocument document = findById(id);

        validateDates(request);

        copyFields(request, document);

        return customerDocumentRepository.save(document);
    }

    public void delete(Long id) {
        CustomerDocument document = findById(id);
        customerDocumentRepository.delete(document);
    }

    private Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + customerId));
    }

    private void copyFields(CustomerDocument source, CustomerDocument target) {
        target.setDocumentType(source.getDocumentType());
        target.setDocumentNumber(source.getDocumentNumber());
        target.setIssuingCountry(source.getIssuingCountry());
        target.setIssuingAuthority(source.getIssuingAuthority());
        target.setIssueDate(source.getIssueDate());
        target.setExpiryDate(source.getExpiryDate());
        target.setVerificationStatus(source.getVerificationStatus());
        target.setVerificationScore(source.getVerificationScore());
        target.setVerificationNotes(source.getVerificationNotes());
        target.setVerifiedAt(source.getVerifiedAt());
        target.setDocumentHash(source.getDocumentHash());
    }

    private void validateDates(CustomerDocument document) {
        LocalDate issueDate = document.getIssueDate();
        LocalDate expiryDate = document.getExpiryDate();

        if (issueDate != null && expiryDate != null && !expiryDate.isAfter(issueDate)) {
            throw new IllegalArgumentException("Expiry date must be after issue date");
        }
    }
}
