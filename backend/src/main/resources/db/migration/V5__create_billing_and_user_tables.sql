CREATE TABLE services (
                          id BIGSERIAL PRIMARY KEY,

                          name VARCHAR(100) NOT NULL,
                          description TEXT,
                          default_price NUMERIC(10,2) NOT NULL DEFAULT 0,
                          active BOOLEAN NOT NULL DEFAULT TRUE,

                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT uk_services_name
                              UNIQUE (name),

                          CONSTRAINT chk_services_default_price
                              CHECK (default_price >= 0)
);


CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,

                       username VARCHAR(100) NOT NULL,
                       email VARCHAR(150) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       first_name VARCHAR(100),
                       last_name VARCHAR(100),
                       active BOOLEAN NOT NULL DEFAULT TRUE,

                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT uk_users_username
                           UNIQUE (username),

                       CONSTRAINT uk_users_email
                           UNIQUE (email)
);


CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,

                       name VARCHAR(100) NOT NULL,
                       description TEXT,

                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT uk_roles_name
                           UNIQUE (name)
);


CREATE TABLE reservation_charges (
                                     id BIGSERIAL PRIMARY KEY,

                                     reservation_id BIGINT NOT NULL,
                                     service_id BIGINT,

                                     description VARCHAR(255) NOT NULL,
                                     charge_type VARCHAR(50) NOT NULL,
                                     quantity INT NOT NULL,
                                     unit_price NUMERIC(10,2) NOT NULL,
                                     total_price NUMERIC(10,2) NOT NULL,
                                     charged_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     created_by_user_id BIGINT,

                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                     CONSTRAINT fk_reservation_charges_reservation
                                         FOREIGN KEY (reservation_id)
                                             REFERENCES reservations(id)
                                             ON DELETE CASCADE,

                                     CONSTRAINT fk_reservation_charges_service
                                         FOREIGN KEY (service_id)
                                             REFERENCES services(id)
                                             ON DELETE SET NULL,

                                     CONSTRAINT fk_reservation_charges_created_by_user
                                         FOREIGN KEY (created_by_user_id)
                                             REFERENCES users(id)
                                             ON DELETE SET NULL,

                                     CONSTRAINT chk_reservation_charges_type
                                         CHECK (charge_type IN (
                                                                'ROOM_RATE',
                                                                'SERVICE',
                                                                'CITY_TAX',
                                                                'PENALTY',
                                                                'DISCOUNT',
                                                                'OTHER'
                                             )),

                                     CONSTRAINT chk_reservation_charges_quantity
                                         CHECK (quantity > 0),

                                     CONSTRAINT chk_reservation_charges_unit_price
                                         CHECK (unit_price >= 0),

                                     CONSTRAINT chk_reservation_charges_total_price
                                         CHECK (total_price >= 0)
);


CREATE INDEX idx_reservation_charges_reservation_id
    ON reservation_charges(reservation_id);

CREATE INDEX idx_reservation_charges_service_id
    ON reservation_charges(service_id);

CREATE INDEX idx_reservation_charges_created_by_user_id
    ON reservation_charges(created_by_user_id);

CREATE INDEX idx_reservation_charges_charged_at
    ON reservation_charges(charged_at);


CREATE TABLE payments (
                          id BIGSERIAL PRIMARY KEY,

                          reservation_id BIGINT NOT NULL,

                          amount NUMERIC(10,2) NOT NULL,
                          method VARCHAR(50) NOT NULL,
                          status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                          paid_at TIMESTAMP,
                          transaction_reference VARCHAR(150),
                          notes TEXT,
                          created_by_user_id BIGINT,

                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_payments_reservation
                              FOREIGN KEY (reservation_id)
                                  REFERENCES reservations(id)
                                  ON DELETE CASCADE,

                          CONSTRAINT fk_payments_created_by_user
                              FOREIGN KEY (created_by_user_id)
                                  REFERENCES users(id)
                                  ON DELETE SET NULL,

                          CONSTRAINT chk_payments_amount
                              CHECK (amount > 0),

                          CONSTRAINT chk_payments_method
                              CHECK (method IN (
                                                'CASH',
                                                'CARD',
                                                'BANK_TRANSFER',
                                                'ONLINE',
                                                'OTHER'
                                  )),

                          CONSTRAINT chk_payments_status
                              CHECK (status IN (
                                                'PENDING',
                                                'COMPLETED',
                                                'FAILED',
                                                'CANCELLED',
                                                'REFUNDED'
                                  ))
);


CREATE INDEX idx_payments_reservation_id
    ON payments(reservation_id);

CREATE INDEX idx_payments_created_by_user_id
    ON payments(created_by_user_id);

CREATE INDEX idx_payments_status
    ON payments(status);

CREATE INDEX idx_payments_paid_at
    ON payments(paid_at);


CREATE TABLE user_roles (
                            id BIGSERIAL PRIMARY KEY,

                            user_id BIGINT NOT NULL,
                            role_id BIGINT NOT NULL,

                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_user_roles_user
                                FOREIGN KEY (user_id)
                                    REFERENCES users(id)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_user_roles_role
                                FOREIGN KEY (role_id)
                                    REFERENCES roles(id)
                                    ON DELETE CASCADE,

                            CONSTRAINT uk_user_roles_user_role
                                UNIQUE (user_id, role_id)
);


CREATE INDEX idx_user_roles_user_id
    ON user_roles(user_id);

CREATE INDEX idx_user_roles_role_id
    ON user_roles(role_id);