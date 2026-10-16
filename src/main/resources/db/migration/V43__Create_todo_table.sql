CREATE TABLE todo
(
    id uuid primary key ,
    username varchar not null ,
    title varchar not null ,
    description varchar ,
    completed BOOLEAN not null DEFAULT FALSE ,
    s3_attachment_key varchar ,
    creation_date timestamptz
);