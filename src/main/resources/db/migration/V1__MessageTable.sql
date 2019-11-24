CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IOT_Message (
    id UUID NOT NULL DEFAULT uuid_generate_v4(),
    deviceId BIGINT NULL,
    deviceTypeId BIGINT NULL,
    groupId BIGINT NULL,
    value NUMERIC (5, 2) NULL,
    timestamp TIMESTAMP NULL,
    CONSTRAINT iotmessage_pk PRIMARY KEY (id)
)