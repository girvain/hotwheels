-- All tables will be created in the `hotwheels_store` database automatically
-- because the entrypoint runs this script after the DB is created.

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    sub VARCHAR NOT NULL,
    profile_picture VARCHAR
);

CREATE TABLE vehicle_type (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR NOT NULL
);

CREATE TABLE vehicle (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR NOT NULL,
    type_id UUID NOT NULL,
    date TIMESTAMP NOT NULL,
    colour VARCHAR NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT vehicle_type_fk FOREIGN KEY (type_id)
        REFERENCES vehicle_type (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT vehicle_user_fk FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE image_url (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    s3_key VARCHAR NOT NULL,
    url VARCHAR NOT NULL,
    vehicle_id UUID NOT NULL,
    CONSTRAINT image_vehicle_fk FOREIGN KEY (vehicle_id)
        REFERENCES vehicle (id)
        ON UPDATE NO ACTION ON DELETE NO ACTION
);
