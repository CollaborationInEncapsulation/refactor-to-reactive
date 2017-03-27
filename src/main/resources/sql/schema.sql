CREATE TABLE IF NOT EXISTS users (
  id           CHAR(60) PRIMARY KEY,
  display_name VARCHAR (256) NOT NULL
);

CREATE TABLE IF NOT EXISTS Messages (
  id          CHAR(60) PRIMARY KEY,
  text        TEXT NOT NULL,
  html        TEXT NOT NULL,
  sent        TIMESTAMP NOT NULL,
  user_id     CHAR(60) NOT NULL,
  read_by     BIGINT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS Mentions (
  id         CHAR(120) PRIMARY KEY,
  message_id CHAR(60) NOT NULL,
  user_id    CHAR(60) NOT NULL
);