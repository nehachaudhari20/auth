CREATE TABLE audit_logs (

    id UUID PRIMARY KEY,

    user_email VARCHAR(255),

    action VARCHAR(100) NOT NULL,

    details TEXT,

    created_at TIMESTAMP NOT NULL
);