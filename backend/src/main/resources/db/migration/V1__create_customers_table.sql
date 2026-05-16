-- V1__create_customers_table.sql
-- Prima migration Flyway per la creazione della tabella customers

CREATE TABLE customers (
                           id BIGSERIAL PRIMARY KEY,

                           first_name VARCHAR(100) NOT NULL,
                           last_name VARCHAR(100) NOT NULL,

                           email VARCHAR(150),
                           phone VARCHAR(50),

                           date_of_birth DATE,
                           nationality VARCHAR(100),

                           document_type VARCHAR(50),
                           document_number VARCHAR(100),

                           tax_code VARCHAR(50),

                           address VARCHAR(255),
                           city VARCHAR(100),
                           country VARCHAR(100),

                           notes TEXT,

                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);