-- seed.sql

-- Users
INSERT INTO users (id, name, email, sub)
VALUES
    ('11111111-1111-1111-1111-111111111111', 'Alice', 'alice@example.com', 'sub1'),
    ('22222222-2222-2222-2222-222222222222', 'Bob', 'bob@example.com', 'sub2');

-- Vehicle types
INSERT INTO vehicle_type (id, name)
VALUES
    ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', 'Car'),
    ('aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2', 'Truck');

-- Optional: Vehicles
-- You can add some vehicles pointing to the seeded users and vehicle types
INSERT INTO vehicle (id, name, type_id, date, colour, user_id)
VALUES
    (gen_random_uuid(), 'Speedster', 'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaa1', now(), 'Red', '11111111-1111-1111-1111-111111111111'),
    (gen_random_uuid(), 'Hauler', 'aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaa2', now(), 'Blue', '22222222-2222-2222-2222-222222222222');
