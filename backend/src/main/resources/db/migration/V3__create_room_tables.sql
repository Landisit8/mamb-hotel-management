CREATE TABLE room_types (
                            id BIGSERIAL PRIMARY KEY,

                            name VARCHAR(100) NOT NULL,
                            description TEXT,
                            max_guests INT NOT NULL,
                            base_price NUMERIC(10,2) NOT NULL,

                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT uk_room_types_name
                                UNIQUE (name),

                            CONSTRAINT chk_room_types_max_guests
                                CHECK (max_guests > 0),

                            CONSTRAINT chk_room_types_base_price
                                CHECK (base_price >= 0)
);


CREATE TABLE rooms (
                       id BIGSERIAL PRIMARY KEY,

                       room_type_id BIGINT NOT NULL,
                       room_number VARCHAR(20) NOT NULL,
                       floor INT,
                       operational_status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
                       cleaning_status VARCHAR(50) NOT NULL DEFAULT 'CLEAN',
                       notes TEXT,

                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT fk_rooms_room_type
                           FOREIGN KEY (room_type_id)
                               REFERENCES room_types(id),

                       CONSTRAINT uk_rooms_room_number
                           UNIQUE (room_number),

                       CONSTRAINT chk_rooms_operational_status
                           CHECK (operational_status IN (
                                                         'ACTIVE',
                                                         'MAINTENANCE',
                                                         'OUT_OF_SERVICE'
                               )),

                       CONSTRAINT chk_rooms_cleaning_status
                           CHECK (cleaning_status IN (
                                                      'CLEAN',
                                                      'DIRTY',
                                                      'CLEANING',
                                                      'INSPECTION_REQUIRED'
                               ))
);


CREATE INDEX idx_rooms_room_type_id
    ON rooms(room_type_id);


CREATE TABLE room_blocks (
                             id BIGSERIAL PRIMARY KEY,

                             room_id BIGINT NOT NULL,
                             start_date DATE NOT NULL,
                             end_date DATE NOT NULL,
                             block_type VARCHAR(50) NOT NULL,
                             reason TEXT,

                             created_by_user_id BIGINT,

                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT fk_room_blocks_room
                                 FOREIGN KEY (room_id)
                                     REFERENCES rooms(id)
                                     ON DELETE CASCADE,

                             CONSTRAINT chk_room_blocks_dates
                                 CHECK (end_date > start_date),

                             CONSTRAINT chk_room_blocks_block_type
                                 CHECK (block_type IN (
                                                       'MAINTENANCE',
                                                       'OUT_OF_SERVICE',
                                                       'INTERNAL_USE',
                                                       'OTHER'
                                     ))
);


CREATE INDEX idx_room_blocks_room_id
    ON room_blocks(room_id);

CREATE INDEX idx_room_blocks_dates
    ON room_blocks(start_date, end_date);