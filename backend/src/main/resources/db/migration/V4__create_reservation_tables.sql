CREATE TABLE reservations (
                              id BIGSERIAL PRIMARY KEY,

                              code VARCHAR(50) NOT NULL,
                              primary_customer_id BIGINT NOT NULL,

                              check_in_date DATE NOT NULL,
                              check_out_date DATE NOT NULL,
                              actual_check_in_at TIMESTAMP,
                              actual_check_out_at TIMESTAMP,

                              adults INT NOT NULL,
                              children INT NOT NULL DEFAULT 0,

                              status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                              source VARCHAR(50) NOT NULL DEFAULT 'DIRECT',
                              special_requests TEXT,
                              total_amount NUMERIC(10,2) NOT NULL DEFAULT 0,

                              created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_reservations_primary_customer
                                  FOREIGN KEY (primary_customer_id)
                                      REFERENCES customers(id),

                              CONSTRAINT uk_reservations_code
                                  UNIQUE (code),

                              CONSTRAINT chk_reservations_dates
                                  CHECK (check_out_date > check_in_date),

                              CONSTRAINT chk_reservations_adults
                                  CHECK (adults >= 1),

                              CONSTRAINT chk_reservations_children
                                  CHECK (children >= 0),

                              CONSTRAINT chk_reservations_total_amount
                                  CHECK (total_amount >= 0),

                              CONSTRAINT chk_reservations_status
                                  CHECK (status IN (
                                                    'PENDING',
                                                    'CONFIRMED',
                                                    'CHECKED_IN',
                                                    'CHECKED_OUT',
                                                    'CANCELLED',
                                                    'NO_SHOW'
                                      )),

                              CONSTRAINT chk_reservations_source
                                  CHECK (source IN (
                                                    'DIRECT',
                                                    'PHONE',
                                                    'EMAIL',
                                                    'BOOKING_COM',
                                                    'AIRBNB',
                                                    'EXPEDIA',
                                                    'WALK_IN',
                                                    'OTHER'
                                      ))
);


CREATE INDEX idx_reservations_primary_customer_id
    ON reservations(primary_customer_id);

CREATE INDEX idx_reservations_dates
    ON reservations(check_in_date, check_out_date);

CREATE INDEX idx_reservations_status
    ON reservations(status);


CREATE TABLE reservation_guests (
                                    id BIGSERIAL PRIMARY KEY,

                                    reservation_id BIGINT NOT NULL,
                                    customer_id BIGINT NOT NULL,
                                    role VARCHAR(50) NOT NULL DEFAULT 'GUEST',

                                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                    CONSTRAINT fk_reservation_guests_reservation
                                        FOREIGN KEY (reservation_id)
                                            REFERENCES reservations(id)
                                            ON DELETE CASCADE,

                                    CONSTRAINT fk_reservation_guests_customer
                                        FOREIGN KEY (customer_id)
                                            REFERENCES customers(id),

                                    CONSTRAINT uk_reservation_guests_reservation_customer
                                        UNIQUE (reservation_id, customer_id),

                                    CONSTRAINT chk_reservation_guests_role
                                        CHECK (role IN (
                                                        'PRIMARY',
                                                        'GUEST'
                                            ))
);


CREATE INDEX idx_reservation_guests_reservation_id
    ON reservation_guests(reservation_id);

CREATE INDEX idx_reservation_guests_customer_id
    ON reservation_guests(customer_id);


CREATE TABLE reservation_rooms (
                                   id BIGSERIAL PRIMARY KEY,

                                   reservation_id BIGINT NOT NULL,
                                   room_id BIGINT NOT NULL,

                                   start_date DATE NOT NULL,
                                   end_date DATE NOT NULL,
                                   price_per_night NUMERIC(10,2) NOT NULL,

                                   created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                   CONSTRAINT fk_reservation_rooms_reservation
                                       FOREIGN KEY (reservation_id)
                                           REFERENCES reservations(id)
                                           ON DELETE CASCADE,

                                   CONSTRAINT fk_reservation_rooms_room
                                       FOREIGN KEY (room_id)
                                           REFERENCES rooms(id),

                                   CONSTRAINT chk_reservation_rooms_dates
                                       CHECK (end_date > start_date),

                                   CONSTRAINT chk_reservation_rooms_price_per_night
                                       CHECK (price_per_night >= 0)
);


CREATE INDEX idx_reservation_rooms_reservation_id
    ON reservation_rooms(reservation_id);

CREATE INDEX idx_reservation_rooms_room_id
    ON reservation_rooms(room_id);

CREATE INDEX idx_reservation_rooms_dates
    ON reservation_rooms(start_date, end_date);