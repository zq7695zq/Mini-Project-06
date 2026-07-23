INSERT INTO desk (id, code, floor, has_monitor, is_active)
SELECT 1, 'HEL-1F-01', TRUE, TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM desk WHERE id = 1);

INSERT INTO desk (id, code, floor, has_monitor, is_active)
SELECT 2, 'HEL-1F-02', 1, FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM desk WHERE id = 2);

INSERT INTO desk (id, code, floor, has_monitor, is_active)
SELECT 3, 'HEL-1F-03', 1, TRUE, FALSE
WHERE NOT EXISTS (SELECT 1 FROM desk WHERE id = 3);

INSERT INTO desk (id, code, floor, has_monitor, is_active)
SELECT 4, 'HEL-1F-04', 1, FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM desk WHERE id = 4);

INSERT INTO desk (id, code, floor, has_monitor, is_active)
SELECT 5, 'HEL-2F-01', 2, TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM desk WHERE id = 5);

INSERT INTO desk (id, code, floor, has_monitor, is_active)
SELECT 6, 'HEL-2F-02', 2, TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM desk WHERE id = 6);

INSERT INTO desk (id, code, floor, has_monitor, is_active)
SELECT 7, 'HEL-2F-03', 2, FALSE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM desk WHERE id = 7);

INSERT INTO desk (id, code, floor, has_monitor, is_active)
SELECT 8, 'HEL-3F-01', 3, TRUE, TRUE
WHERE NOT EXISTS (SELECT 1 FROM desk WHERE id = 8);

INSERT INTO booking (id, desk_id, employee_name, booking_date, created_at)
SELECT 1, 1, 'Anna Kowalska', '2026-07-24', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM booking WHERE id = 1);

INSERT INTO booking (id, desk_id, employee_name, booking_date, created_at)
SELECT 2, 5, 'Matti Virtanen', '2026-07-24', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM booking WHERE id = 2);

INSERT INTO booking (id, desk_id, employee_name, booking_date, created_at)
SELECT 3, 6, 'Liisa Nieminen', '2026-07-25', CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM booking WHERE id = 3);
