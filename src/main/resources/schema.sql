CREATE TABLE IF NOT EXISTS desk (
    id BIGINT NOT NULL AUTO_INCREMENT,
    code VARCHAR(32) NOT NULL,
    floor INT NOT NULL,
    has_monitor BOOLEAN NOT NULL,
    is_active BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_desk_code UNIQUE (code),
    INDEX idx_desk_floor (floor)
);

CREATE TABLE IF NOT EXISTS booking (
    id BIGINT NOT NULL AUTO_INCREMENT,
    desk_id BIGINT NOT NULL,
    employee_name VARCHAR(100) NOT NULL,
    booking_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_booking_desk FOREIGN KEY (desk_id) REFERENCES desk (id),
    CONSTRAINT uk_booking_desk_date UNIQUE (desk_id, booking_date),
    INDEX idx_booking_date (booking_date)
);
