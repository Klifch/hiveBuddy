-- -- Insert sample users
-- INSERT INTO users (username, password) VALUES
--                                            ('user1', 'password1'),
--                                            ('user2', 'password2'),
--                                            ('user3', 'password3');
--
-- -- Insert sample devices for user1
-- INSERT INTO devices (user_id, device_name, serial_number) VALUES
--                                                (1, 'Device 1 for User 1', 'qwerty1'),
--                                                (1, 'Device 2 for User 1', 'qwerty2');
--
-- -- Insert sample devices for user2
-- INSERT INTO devices (user_id, device_name, serial_number) VALUES
--                                                (2, 'Device 1 for User 2', 'asdf4'),
--                                                (2, 'Device 2 for User 2', 'qwwefhh4');
--
-- -- Insert sample sensor data for Device 1 for User 1
-- INSERT INTO sensor_data (device_id, timestamp, sensor1, sensor2, sensor3, sensor4, sensor5) VALUES
--                                                                                                 (1, CURRENT_TIMESTAMP - interval '10' minute, 10.5, 20.3, 15.7, 8.2, 12.0),
--                                                                                                 (1, CURRENT_TIMESTAMP - interval '9' minute, 11.2, 21.1, 14.3, 9.0, 11.5),
--                                                                                                 (1, CURRENT_TIMESTAMP - interval '8' minute, 9.8, 19.5, 16.2, 8.7, 12.8);
--
-- -- Insert sample sensor data for Device 2 for User 1
-- INSERT INTO sensor_data (device_id, timestamp, sensor1, sensor2, sensor3, sensor4, sensor5) VALUES
--                                                                                                 (2, CURRENT_TIMESTAMP - interval '10' minute, 12.3, 18.9, 17.6, 10.1, 11.9),
--                                                                                                 (2, CURRENT_TIMESTAMP - interval '9' minute, 10.7, 20.5, 15.8, 9.5, 12.3),
--                                                                                                 (2, CURRENT_TIMESTAMP - interval '8' minute, 11.5, 19.3, 16.7, 8.9, 11.2);

-- -- Insert sample roles
-- INSERT INTO roles (name) VALUES
--                              ('ROLE_USER'),
--                              ('ROLE_ADMIN');
--
-- -- Insert sample users
-- INSERT INTO users (username, password, enabled, role_id) VALUES
--                                                                     ('user1', 'test123', true, 1), -- hashed version of 'password1' $2a$10$S2xwT5bGpiDBEiQpD3mK5u13n5gC7NWn8iysC4HTOHfxlmld9/RZu'
--                                                                     ('user2', '$2a$10$6A/AXI59sy4rSdaz4PEUbOHXte9.VdsRYp2th0RXO6b0YnHsWZw5a', true, 1), -- hashed version of 'password2'
--                                                                     ('user3', '$2a$10$3TRQRu9tZ2gjmlFJTHcX9u1Wb1e5pIzE8RnZSDENwPU3J9cKo.5Oy', true, 1); -- hashed version of 'password3'
--  12qw - susan password
INSERT INTO users (username, password, enabled)
VALUES
    ('john', '$2a$12$Y7u0QoZa.cRdilgjNe5YluEjqXLMcFISOQKkrr3YV8gl/GMAHMHJe', true),
    ('mary', '$2a$10$gHL4quzbc6QEsLi5ppz2T.tlW2./eSBKwpyirHNF1tSmndDZzr04C', true),
    ('susan', '$2a$10$gHL4quzbc6QEsLi5ppz2T.tlW2./eSBKwpyirHNF1tSmndDZzr04C', true);

INSERT INTO roles (name)
VALUES
    ('ROLE_REGULAR'),('ROLE_ADMIN');

INSERT INTO users_roles (user_id, role_id)
VALUES
    (1, 1),
    (2, 1),
    (3, 1),
    (3, 2);


INSERT INTO unregistered_pool (serial_number, security_code) VALUES
                                                                 ('zxcvbn', '1234'),
                                                                 ('asdzxc', '1122');

-- Insert sample devices for user1
INSERT INTO devices (user_id, device_name, serial_number, active, security_code) VALUES
                                                              (1, 'Device 1 for User 1', 'qwerty1', true, '1234'),
                                                              (1, 'Device 2 for User 1', 'qwerty2', true, '1234');

-- Insert sample devices for user2
INSERT INTO devices (user_id, device_name, serial_number, active, security_code) VALUES
                                                              (2, 'Device 1 for User 2', 'asdf4', true, '1122'),
                                                              (2, 'Device 2 for User 2', 'qwwefhh4', true, '1122');

-- Insert sample sensor data for Device 1 for User 1
INSERT INTO sensor_data (device_id, timestamp, sensor1, sensor2, sensor3, sensor4, sensor5) VALUES
                                                                                                (1, CURRENT_TIMESTAMP - interval '10' minute, 10.5, 20.3, 15.7, 8.2, 12.0),
                                                                                                (1, CURRENT_TIMESTAMP - interval '9' minute, 11.2, 21.1, 14.3, 9.0, 11.5),
                                                                                                (1, CURRENT_TIMESTAMP - interval '8' minute, 9.8, 19.5, 16.2, 8.7, 12.8);

-- Insert sample sensor data for Device 2 for User 1
INSERT INTO sensor_data (device_id, timestamp, sensor1, sensor2, sensor3, sensor4, sensor5) VALUES
                                                                                                (2, CURRENT_TIMESTAMP - interval '10' minute, 12.3, 18.9, 17.6, 10.1, 11.9),
                                                                                                (2, CURRENT_TIMESTAMP - interval '9' minute, 10.7, 20.5, 15.8, 9.5, 12.3),
                                                                                                (2, CURRENT_TIMESTAMP - interval '8' minute, 11.5, 19.3, 16.7, 8.9, 11.2);

INSERT INTO sensor_data (device_id, timestamp, sensor1, sensor2, sensor3, sensor4, sensor5) VALUES
                                                                                                (3, CURRENT_TIMESTAMP - interval '10' minute, 10.5, 20.3, 15.7, 8.2, 12.0),
                                                                                                (3, CURRENT_TIMESTAMP - interval '9' minute, 11.2, 21.1, 14.3, 9.0, 11.5),
                                                                                                (3, CURRENT_TIMESTAMP - interval '8' minute, 9.8, 19.5, 16.2, 8.7, 12.8);

