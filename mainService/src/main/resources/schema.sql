DROP TABLE IF EXISTS users, events, categories, requests,events_creators CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    INT     NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name  VARCHAR NOT NULL,
    email VARCHAR NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS categories
(
    id   INT            NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL
);
CREATE TABLE IF NOT EXISTS events
(
    id                 INT                         NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation         VARCHAR(2000)               NOT NULL,
    category_id        INT                         NOT NULL REFERENCES categories (id) ON DELETE CASCADE,
    description        VARCHAR(7000)               NOT NULL,
    confirmed_requests INT,
    event_date         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    created_on         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    lat                FLOAT                       NOT NULL,
    lon                FLOAT                       NOT NULL,
    initiator_id       INT                         NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    views              INT,
    state              VARCHAR                     NOT NULL,
    paid               BOOLEAN                     NOT NULL,
    participant_limit  INT                         NOT NULL,
    request_moderation BOOLEAN                     NOT NULL,
    title              VARCHAR(120)                NOT NULL
);
CREATE TABLE IF NOT EXISTS requests
(
    id           INT                         NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id     INT                         NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    requester_id INT                         NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    status       VARCHAR                     NOT NULL
);

