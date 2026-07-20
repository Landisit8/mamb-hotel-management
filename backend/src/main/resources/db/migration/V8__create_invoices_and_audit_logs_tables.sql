CREATE TABLE invoices (
                          id BIGSERIAL PRIMARY KEY,

                          reservation_id BIGINT NOT NULL,

                          invoice_number VARCHAR(100) NOT NULL,
                          invoice_type VARCHAR(50) NOT NULL,
                          status VARCHAR(50) NOT NULL DEFAULT 'DRAFT',
                          issue_date DATE,
                          customer_name VARCHAR(200),
                          customer_tax_code VARCHAR(50),
                          billing_address VARCHAR(255),
                          total_amount NUMERIC(10,2) NOT NULL DEFAULT 0,

                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_invoices_reservation
                              FOREIGN KEY (reservation_id)
                                  REFERENCES reservations(id)
                                  ON DELETE CASCADE,

                          CONSTRAINT uk_invoices_invoice_number
                              UNIQUE (invoice_number),

                          CONSTRAINT chk_invoices_invoice_type
                              CHECK (invoice_type IN (
                                                      'RECEIPT',
                                                      'INVOICE'
                                  )),

                          CONSTRAINT chk_invoices_status
                              CHECK (status IN (
                                                'DRAFT',
                                                'ISSUED',
                                                'CANCELLED'
                                  )),

                          CONSTRAINT chk_invoices_total_amount
                              CHECK (total_amount >= 0)
);


CREATE INDEX idx_invoices_reservation_id
    ON invoices(reservation_id);

CREATE INDEX idx_invoices_status
    ON invoices(status);

CREATE INDEX idx_invoices_issue_date
    ON invoices(issue_date);


CREATE TABLE audit_logs (
                            id BIGSERIAL PRIMARY KEY,

                            user_id BIGINT,
                            action VARCHAR(100) NOT NULL,
                            entity_name VARCHAR(100) NOT NULL,
                            entity_id BIGINT,
                            old_value TEXT,
                            new_value TEXT,

                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_audit_logs_user
                                FOREIGN KEY (user_id)
                                    REFERENCES users(id)
                                    ON DELETE SET NULL,

                            CONSTRAINT chk_audit_logs_action_length
                                CHECK (length(trim(action)) > 0),

                            CONSTRAINT chk_audit_logs_entity_name_length
                                CHECK (length(trim(entity_name)) > 0)
);


CREATE INDEX idx_audit_logs_user_id
    ON audit_logs(user_id);

CREATE INDEX idx_audit_logs_entity_name
    ON audit_logs(entity_name);

CREATE INDEX idx_audit_logs_entity_name_entity_id
    ON audit_logs(entity_name, entity_id);

CREATE INDEX idx_audit_logs_created_at
    ON audit_logs(created_at);