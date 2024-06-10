CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS customers (
    id varchar(128) default uuid_generate_v4(),
    name varchar(256),
    operation varchar(256),
    arrived_at timestamp,
    start_processing_at timestamp,
    finished_processing_at timestamp,
    processed_in_work_hour boolean,

    primary key (id)
);