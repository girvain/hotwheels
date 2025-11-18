-- All tables will be created in the `hotwheels_store` database automatically
-- because the entrypoint runs this script after the DB is created.

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       name VARCHAR(255),
                       email VARCHAR(255),
                       sub VARCHAR(255),
                       profile_picture VARCHAR(255)
);

CREATE TABLE vehicle_type (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            name VARCHAR(255)
);

CREATE TABLE vehicle (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         name VARCHAR(255),
                         type_id UUID REFERENCES vehicle_type(id),
                         date TIMESTAMP,
                         colour VARCHAR(255),
                         user_id UUID REFERENCES users(id)
);

CREATE TABLE image_url (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           key VARCHAR(255),
                           url VARCHAR(255),
                           vehicle_id UUID REFERENCES vehicle(id)
);
