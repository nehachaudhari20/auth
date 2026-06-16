CREATE TABLE refresh_tokens (

    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    token VARCHAR(255) UNIQUE NOT NULL,

    expires_at TIMESTAMP NOT NULL,

    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_refresh_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);
