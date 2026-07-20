CREATE TABLE reservation_status_history (
                                            id BIGSERIAL PRIMARY KEY,

                                            reservation_id BIGINT NOT NULL,
                                            old_status VARCHAR(50),
                                            new_status VARCHAR(50) NOT NULL,
                                            changed_by_user_id BIGINT NOT NULL,
                                            reason TEXT,

                                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                            CONSTRAINT fk_reservation_status_history_reservation
                                                FOREIGN KEY (reservation_id)
                                                    REFERENCES reservations(id)
                                                    ON DELETE CASCADE,

                                            CONSTRAINT fk_reservation_status_history_changed_by_user
                                                FOREIGN KEY (changed_by_user_id)
                                                    REFERENCES users(id),

                                            CONSTRAINT chk_reservation_status_history_old_status
                                                CHECK (old_status IS NULL OR old_status IN (
                                                                                            'PENDING',
                                                                                            'CONFIRMED',
                                                                                            'CHECKED_IN',
                                                                                            'CHECKED_OUT',
                                                                                            'CANCELLED',
                                                                                            'NO_SHOW'
                                                    )),

                                            CONSTRAINT chk_reservation_status_history_new_status
                                                CHECK (new_status IN (
                                                                      'PENDING',
                                                                      'CONFIRMED',
                                                                      'CHECKED_IN',
                                                                      'CHECKED_OUT',
                                                                      'CANCELLED',
                                                                      'NO_SHOW'
                                                    ))
);


CREATE INDEX idx_reservation_status_history_reservation_id
    ON reservation_status_history(reservation_id);

CREATE INDEX idx_reservation_status_history_changed_by_user_id
    ON reservation_status_history(changed_by_user_id);

CREATE INDEX idx_reservation_status_history_created_at
    ON reservation_status_history(created_at);


CREATE TABLE room_status_history (
                                     id BIGSERIAL PRIMARY KEY,

                                     room_id BIGINT NOT NULL,
                                     old_operational_status VARCHAR(50),
                                     new_operational_status VARCHAR(50),
                                     old_cleaning_status VARCHAR(50),
                                     new_cleaning_status VARCHAR(50),
                                     changed_by_user_id BIGINT NOT NULL,
                                     reason TEXT,

                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                     CONSTRAINT fk_room_status_history_room
                                         FOREIGN KEY (room_id)
                                             REFERENCES rooms(id)
                                             ON DELETE CASCADE,

                                     CONSTRAINT fk_room_status_history_changed_by_user
                                         FOREIGN KEY (changed_by_user_id)
                                             REFERENCES users(id),

                                     CONSTRAINT chk_room_status_history_old_operational_status
                                         CHECK (old_operational_status IS NULL OR old_operational_status IN (
                                                                                                             'ACTIVE',
                                                                                                             'MAINTENANCE',
                                                                                                             'OUT_OF_SERVICE'
                                             )),

                                     CONSTRAINT chk_room_status_history_new_operational_status
                                         CHECK (new_operational_status IS NULL OR new_operational_status IN (
                                                                                                             'ACTIVE',
                                                                                                             'MAINTENANCE',
                                                                                                             'OUT_OF_SERVICE'
                                             )),

                                     CONSTRAINT chk_room_status_history_old_cleaning_status
                                         CHECK (old_cleaning_status IS NULL OR old_cleaning_status IN (
                                                                                                       'CLEAN',
                                                                                                       'DIRTY',
                                                                                                       'CLEANING',
                                                                                                       'INSPECTION_REQUIRED'
                                             )),

                                     CONSTRAINT chk_room_status_history_new_cleaning_status
                                         CHECK (new_cleaning_status IS NULL OR new_cleaning_status IN (
                                                                                                       'CLEAN',
                                                                                                       'DIRTY',
                                                                                                       'CLEANING',
                                                                                                       'INSPECTION_REQUIRED'
                                             )),

                                     CONSTRAINT chk_room_status_history_at_least_one_new_status
                                         CHECK (new_operational_status IS NOT NULL OR new_cleaning_status IS NOT NULL)
);


CREATE INDEX idx_room_status_history_room_id
    ON room_status_history(room_id);

CREATE INDEX idx_room_status_history_changed_by_user_id
    ON room_status_history(changed_by_user_id);

CREATE INDEX idx_room_status_history_created_at
    ON room_status_history(created_at);