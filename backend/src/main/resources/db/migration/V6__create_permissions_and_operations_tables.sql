CREATE TABLE permissions (
                             id BIGSERIAL PRIMARY KEY,

                             name VARCHAR(100) NOT NULL,
                             description TEXT,

                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT uk_permissions_name
                                 UNIQUE (name)
);


CREATE TABLE role_permissions (
                                  id BIGSERIAL PRIMARY KEY,

                                  role_id BIGINT NOT NULL,
                                  permission_id BIGINT NOT NULL,

                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                  CONSTRAINT fk_role_permissions_role
                                      FOREIGN KEY (role_id)
                                          REFERENCES roles(id)
                                          ON DELETE CASCADE,

                                  CONSTRAINT fk_role_permissions_permission
                                      FOREIGN KEY (permission_id)
                                          REFERENCES permissions(id)
                                          ON DELETE CASCADE,

                                  CONSTRAINT uk_role_permissions_role_permission
                                      UNIQUE (role_id, permission_id)
);


CREATE INDEX idx_role_permissions_role_id
    ON role_permissions(role_id);

CREATE INDEX idx_role_permissions_permission_id
    ON role_permissions(permission_id);


CREATE TABLE housekeeping_tasks (
                                    id BIGSERIAL PRIMARY KEY,

                                    room_id BIGINT NOT NULL,
                                    reservation_id BIGINT,
                                    assigned_user_id BIGINT,

                                    status VARCHAR(50) NOT NULL DEFAULT 'TODO',
                                    priority VARCHAR(50) NOT NULL DEFAULT 'NORMAL',
                                    task_type VARCHAR(50) NOT NULL DEFAULT 'CLEANING',
                                    due_date DATE,
                                    started_at TIMESTAMP,
                                    completed_at TIMESTAMP,
                                    notes TEXT,

                                    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                    CONSTRAINT fk_housekeeping_tasks_room
                                        FOREIGN KEY (room_id)
                                            REFERENCES rooms(id),

                                    CONSTRAINT fk_housekeeping_tasks_reservation
                                        FOREIGN KEY (reservation_id)
                                            REFERENCES reservations(id)
                                            ON DELETE SET NULL,

                                    CONSTRAINT fk_housekeeping_tasks_assigned_user
                                        FOREIGN KEY (assigned_user_id)
                                            REFERENCES users(id)
                                            ON DELETE SET NULL,

                                    CONSTRAINT chk_housekeeping_tasks_status
                                        CHECK (status IN (
                                                          'TODO',
                                                          'IN_PROGRESS',
                                                          'DONE',
                                                          'CANCELLED'
                                            )),

                                    CONSTRAINT chk_housekeeping_tasks_priority
                                        CHECK (priority IN (
                                                            'LOW',
                                                            'NORMAL',
                                                            'HIGH',
                                                            'URGENT'
                                            )),

                                    CONSTRAINT chk_housekeeping_tasks_task_type
                                        CHECK (task_type IN (
                                                             'CLEANING',
                                                             'INSPECTION',
                                                             'LINEN_CHANGE',
                                                             'DEEP_CLEANING',
                                                             'OTHER'
                                            )),

                                    CONSTRAINT chk_housekeeping_tasks_completed_after_started
                                        CHECK (completed_at IS NULL OR started_at IS NULL OR completed_at >= started_at)
);


CREATE INDEX idx_housekeeping_tasks_room_id
    ON housekeeping_tasks(room_id);

CREATE INDEX idx_housekeeping_tasks_reservation_id
    ON housekeeping_tasks(reservation_id);

CREATE INDEX idx_housekeeping_tasks_assigned_user_id
    ON housekeeping_tasks(assigned_user_id);

CREATE INDEX idx_housekeeping_tasks_status
    ON housekeeping_tasks(status);

CREATE INDEX idx_housekeeping_tasks_due_date
    ON housekeeping_tasks(due_date);


CREATE TABLE maintenance_tickets (
                                     id BIGSERIAL PRIMARY KEY,

                                     room_id BIGINT NOT NULL,
                                     reported_by_user_id BIGINT NOT NULL,
                                     assigned_user_id BIGINT,

                                     title VARCHAR(150) NOT NULL,
                                     description TEXT NOT NULL,
                                     status VARCHAR(50) NOT NULL DEFAULT 'OPEN',
                                     priority VARCHAR(50) NOT NULL DEFAULT 'NORMAL',
                                     resolved_at TIMESTAMP,

                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                     CONSTRAINT fk_maintenance_tickets_room
                                         FOREIGN KEY (room_id)
                                             REFERENCES rooms(id),

                                     CONSTRAINT fk_maintenance_tickets_reported_by_user
                                         FOREIGN KEY (reported_by_user_id)
                                             REFERENCES users(id),

                                     CONSTRAINT fk_maintenance_tickets_assigned_user
                                         FOREIGN KEY (assigned_user_id)
                                             REFERENCES users(id)
                                             ON DELETE SET NULL,

                                     CONSTRAINT chk_maintenance_tickets_status
                                         CHECK (status IN (
                                                           'OPEN',
                                                           'IN_PROGRESS',
                                                           'RESOLVED',
                                                           'CANCELLED'
                                             )),

                                     CONSTRAINT chk_maintenance_tickets_priority
                                         CHECK (priority IN (
                                                             'LOW',
                                                             'NORMAL',
                                                             'HIGH',
                                                             'URGENT'
                                             )),

                                     CONSTRAINT chk_maintenance_tickets_title_length
                                         CHECK (length(trim(title)) > 0),

                                     CONSTRAINT chk_maintenance_tickets_description_length
                                         CHECK (length(trim(description)) > 0)
);


CREATE INDEX idx_maintenance_tickets_room_id
    ON maintenance_tickets(room_id);

CREATE INDEX idx_maintenance_tickets_reported_by_user_id
    ON maintenance_tickets(reported_by_user_id);

CREATE INDEX idx_maintenance_tickets_assigned_user_id
    ON maintenance_tickets(assigned_user_id);

CREATE INDEX idx_maintenance_tickets_status
    ON maintenance_tickets(status);

CREATE INDEX idx_maintenance_tickets_priority
    ON maintenance_tickets(priority);