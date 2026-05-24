package com.mamb.hotel.customer_document;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDocumentRepository extends JpaRepository<CustomerDocument, Long> {

    List<CustomerDocument> findByCustomer_Id(Long customerId);

    boolean existsByCustomer_IdAndDocumentTypeAndDocumentNumber(
            Long customerId,
            DocumentType documentType,
            String documentNumber
    );
}
