CREATE TABLE sessions (

    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    refresh_token_id UUID NOT NULL,

    device_name VARCHAR(255),

    ip_address VARCHAR(255),

    created_at TIMESTAMP NOT NULL,

    last_active_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_session_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_session_refresh_token
        FOREIGN KEY (refresh_token_id)
        REFERENCES refresh_tokens(id)
        ON DELETE CASCADE
);