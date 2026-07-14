create table document_data (
    id uuid primary key,
    client_name varchar(160) not null,
    provider_name varchar(160) not null,
    service_description varchar(1000) not null,
    effective_date date not null,
    fee_amount numeric(12, 2) not null,
    created_at timestamp with time zone not null
);

create table document_generation (
    id uuid primary key,
    document_data_id uuid not null references document_data(id),
    status varchar(32) not null,
    generated_file_path varchar(1000),
    failure_reason varchar(2000),
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    completed_at timestamp with time zone
);

create index idx_document_generation_document_data_id on document_generation(document_data_id);
create index idx_document_generation_status on document_generation(status);
