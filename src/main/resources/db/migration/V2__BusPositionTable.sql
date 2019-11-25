CREATE TABLE IOT_BusPosition (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    busId VARCHAR NOT NULL,
    milesPerHour DOUBLE PRECISION NOT NULL,
    latitude DOUBLE PRECISION NOT NULL,
    longitude DOUBLE PRECISION NOT NULL,
    timestamp TIMESTAMP NULL,
    CONSTRAINT iotbusposition_pk PRIMARY KEY (id)
)