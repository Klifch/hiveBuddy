DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS users_roles CASCADE;
DROP TABLE IF EXISTS sensor_data CASCADE;
DROP TABLE IF EXISTS devices CASCADE;
DROP TABLE IF EXISTS unregistered_pool CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS role CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
--
-- CREATE TABLE roles (
--                        role_id SERIAL PRIMARY KEY,
--                        name VARCHAR(50) NOT NULL
-- );
--
-- CREATE TABLE users (
--                        user_id SERIAL PRIMARY KEY,
--                        username VARCHAR(50) NOT NULL,
--                        password VARCHAR(80) NOT NULL,
--                        enabled BOOLEAN NOT NULL,
--                        role_id INT REFERENCES roles(role_id)
-- );
--
-- CREATE TABLE user_roles (
--                             user_id INT REFERENCES users(user_id),
--                             role_id INT REFERENCES roles(role_id),
--                             PRIMARY KEY (user_id, role_id)
-- );

CREATE TABLE users (
                       user_id serial PRIMARY KEY,
                       username varchar(50) NOT NULL,
                       password varchar(60) NOT NULL,
                       enabled boolean NOT NULL
);

CREATE TABLE roles (
                      id serial PRIMARY KEY,
                      name varchar(50)
);

CREATE TABLE users_roles (
                             user_id int NOT NULL,
                             role_id int NOT NULL,
                             PRIMARY KEY (user_id, role_id),
                             FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                             FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE devices (
                         device_id SERIAL PRIMARY KEY,
                         user_id INT REFERENCES users(user_id),
                         device_name VARCHAR(50) NOT NULL,
                         serial_number VARCHAR(50) NOT NULL,
                         active boolean NOT NULL,
                         security_code VARCHAR(50) NOT NULL
);

CREATE TABLE unregistered_pool (
                         serial_number VARCHAR(50) PRIMARY KEY ,
                         security_code VARCHAR(50) NOT NULL
);

CREATE TABLE sensor_data (
                             data_id SERIAL PRIMARY KEY,
                             device_id INT REFERENCES devices(device_id),
                             timestamp TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                             sensor1 DECIMAL,
                             sensor2 DECIMAL,
                             sensor3 DECIMAL,
                             sensor4 DECIMAL,
                             sensor5 DECIMAL
);