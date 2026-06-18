CREATE TABLE api_keys (

    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    key_hash VARCHAR(255) NOT NULL,

    name VARCHAR(255),

    created_at TIMESTAMP NOT NULL,

    expires_at TIMESTAMP,

    CONSTRAINT fk_api_key_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);