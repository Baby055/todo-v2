CREATE TABLE app_user
(
    id uuid primary key ,
    username varchar not null ,
    email varchar not null unique ,
    password_hash varchar not null
);