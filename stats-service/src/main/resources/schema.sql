DROP TABLE IF EXISTS statistics CASCADE;

CREATE TABLE IF NOT EXISTS statistics
(
    id         INT     NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app        VARCHAR NOT NULL,
    uri        VARCHAR NOT NULL,
    ip         VARCHAR NOT NULL,
    time_stamp TIMESTAMP WITHOUT TIME ZONE
);
