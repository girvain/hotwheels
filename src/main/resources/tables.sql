-- All tables will be created in the `hotwheels_store` database automatically
-- because the entrypoint runs this script after the DB is created.

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       name VARCHAR,
                       email VARCHAR,
                       sub VARCHAR,
                       profile_picture VARCHAR
);

CREATE TABLE vehicle_type (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            name VARCHAR
);

CREATE TABLE vehicle (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         name VARCHAR,
                         type_id UUID REFERENCES vehicle_type(id),
                         date TIMESTAMP,
                         colour VARCHAR,
                         user_id UUID REFERENCES users(id)
);

CREATE TABLE image_url (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           key VARCHAR,
                           url VARCHAR,
                           vehicle_id UUID REFERENCES vehicle(id)
);
