ALTER TABLE API_USER ADD COLUMN ROLE VARCHAR(20) NOT NULL DEFAULT 'ADMIN';

DROP TABLE API_USER_ROLE_MAP;
DROP TABLE API_ROLE;
