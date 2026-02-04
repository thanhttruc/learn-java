USE app_db;

ALTER TABLE accounts
    ADD COLUMN account_number VARCHAR(50);
