package com.mamb.hotel.customer_document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mamb.hotel.customer.Customer;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "customer_documents")
public class CustomerDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Relazione DB:
     * customer_documents.customer_id -> customers.id
     */
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotNull(message = "Document type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false, length = 50)
    private DocumentType documentType;

    @NotBlank(message = "Document number is required")
    @Size(max = 100, message = "Document number must be at most 100 characters")
    @Column(name = "document_number", nullable = false, length = 100)
    private String documentNumber;

    @NotBlank(message = "Issuing country is required")
    @Size(max = 100, message = "Issuing country must be at most 100 characters")
    @Column(name = "issuing_country", nullable = false, length = 100)
    private String issuingCountry;

    @Size(max = 150, message = "Issuing authority must be at most 150 characters")
    @Column(name = "issuing_authority", length = 150)
    private String issuingAuthority;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @NotNull(message = "Expiry date is required")
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @NotNull(message = "Verification status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false, length = 50)
    private DocumentVerificationStatus verificationStatus = DocumentVerificationStatus.NOT_VERIFIED;

    @DecimalMin(value = "0.00", message = "Verification score cannot be lower than 0")
    @DecimalMax(value = "100.00", message = "Verification score cannot be greater than 100")
    @Column(name = "verification_score", precision = 5, scale = 2)
    private BigDecimal verificationScore;

    @Column(name = "verification_notes")
    private String verificationNotes;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Size(max = 255, message = "Document hash must be at most 255 characters")
    @Column(name = "document_hash", length = 255)
    private String documentHash;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /*
     * Serve per far vedere customerId nel JSON di risposta,
     * senza serializzare tutto l'oggetto Customer.
     */
    @JsonProperty("customerId")
    public Long getCustomerId() {
        return customer != null ? customer.getId() : null;
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.verificationStatus == null) {
            this.verificationStatus = DocumentVerificationStatus.NOT_VERIFIED;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
