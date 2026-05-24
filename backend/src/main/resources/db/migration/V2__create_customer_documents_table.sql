CREATE TABLE customer_documents (
                                    id BIGSERIAL PRIMARY KEY,

                                    customer_id BIGINT NOT NULL,

                                    document_type VARCHAR(50) NOT NULL,
                                    document_number VARCHAR(100) NOT NULL,

                                    issuing_country VARCHAR(100) NOT NULL,
                                    issuing_authority VARCHAR(150),

                                    issue_date DATE,
                                    expiry_date DATE NOT NULL,

                                    verification_status VARCHAR(50) NOT NULL DEFAULT 'NOT_VERIFIED',
                                    verification_score NUMERIC(5,2),
                                    verification_notes TEXT,
                                    verified_at TIMESTAMP,

                                    document_hash VARCHAR(255),

                                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                    CONSTRAINT fk_customer_documents_customer
                                        FOREIGN KEY (customer_id)
                                            REFERENCES customers(id)
                                            ON DELETE CASCADE,

                                    CONSTRAINT chk_customer_documents_document_type
                                        CHECK (document_type IN (
                                                                 'ID_CARD',
                                                                 'PASSPORT',
                                                                 'DRIVING_LICENSE',
                                                                 'OTHER'
                                            )),

                                    CONSTRAINT chk_customer_documents_verification_status
                                        CHECK (verification_status IN (
                                                                       'NOT_VERIFIED',
                                                                       'PENDING',
                                                                       'VERIFIED',
                                                                       'REJECTED',
                                                                       'EXPIRED'
                                            )),

                                    CONSTRAINT chk_customer_documents_verification_score
                                        CHECK (
                                            verification_score IS NULL
                                                OR verification_score BETWEEN 0 AND 100
                                            ),

                                    CONSTRAINT chk_customer_documents_dates
                                        CHECK (
                                            issue_date IS NULL
                                                OR expiry_date > issue_date
                                            ),

                                    CONSTRAINT uk_customer_documents_customer_type_number
                                        UNIQUE (customer_id, document_type, document_number)
);

CREATE INDEX idx_customer_documents_customer_id
    ON customer_documents(customer_id);