USE app_db;

ALTER TABLE accounts
    MODIFY COLUMN type
    ENUM('CASH','BANK','CREDIT_CARD','SAVING_GOAL','WALLET') NOT NULL;

ALTER TABLE goals
DROP COLUMN current_amount;

ALTER TABLE goals
    ADD COLUMN account_id BIGINT NOT NULL AFTER user_id;

ALTER TABLE goals
    ADD CONSTRAINT fk_goal_account
        FOREIGN KEY (account_id) REFERENCES accounts(id)
            ON DELETE RESTRICT;

ALTER TABLE accounts
DROP COLUMN initial_balance;

CREATE INDEX idx_goals_account_id ON goals(account_id);
