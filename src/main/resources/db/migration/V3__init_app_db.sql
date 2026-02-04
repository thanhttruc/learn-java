USE app_db;

ALTER TABLE accounts
    ADD COLUMN bank_name VARCHAR(255),
    ADD COLUMN branch_name VARCHAR(255);
