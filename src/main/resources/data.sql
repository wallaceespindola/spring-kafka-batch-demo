-- Seed a ready pair (UUID 00000000-0000-0000-0000-000000000001) with amounts
INSERT INTO messages (correlation_id, part_no, content, amount, processed, created_at)
VALUES (X'00000000000000000000000000000001', 1, 'seed part 1', 5.00, FALSE, CURRENT_TIMESTAMP());

INSERT INTO messages (correlation_id, part_no, content, amount, processed, created_at)
VALUES (X'00000000000000000000000000000001', 2, 'seed part 2', 7.50, FALSE, CURRENT_TIMESTAMP());
