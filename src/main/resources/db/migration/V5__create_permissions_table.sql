CREATE TABLE permissions
(
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) DEFAULT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);