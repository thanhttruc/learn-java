-- DROP DATABASE IF EXISTS app_db;
-- CREATE DATABASE app_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE app_db;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100),
    email VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    avatar_url VARCHAR(255),
    default_currency VARCHAR(10) DEFAULT 'VND',

    role ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER',

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);

-- =====================
-- ACCOUNTS
-- =====================
CREATE TABLE accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    type ENUM('CASH','BANK','CREDIT_CARD') NOT NULL,
    initial_balance DECIMAL(15,2) DEFAULT 0,
    current_balance DECIMAL(15,2) DEFAULT 0,
    currency VARCHAR(10) DEFAULT 'VND',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =====================
-- CATEGORIES
-- =====================
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    type ENUM('INCOME','EXPENSE') NOT NULL,
    parent_id BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_category_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES categories(id)
);

-- =====================
-- USER TRANSACTIONS (FIXED)
-- =====================
CREATE TABLE user_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    target_account_id BIGINT,
    category_id BIGINT,
    type ENUM('INCOME','EXPENSE','TRANSFER') NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    description VARCHAR(255),
    transaction_time DATETIME NOT NULL,
    status ENUM('COMPLETED','PENDING','FAILED') DEFAULT 'COMPLETED',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tx_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_tx_account FOREIGN KEY (account_id) REFERENCES accounts(id),
    CONSTRAINT fk_tx_target_account FOREIGN KEY (target_account_id) REFERENCES accounts(id),
    CONSTRAINT fk_tx_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- =====================
-- BUDGETS
-- =====================
CREATE TABLE budgets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    limit_amount DECIMAL(15,2) NOT NULL,
    actual_spent DECIMAL(15,2) DEFAULT 0,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_budget_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_budget_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- =====================
-- GOALS
-- =====================
CREATE TABLE goals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    target_amount DECIMAL(15,2) NOT NULL,
    current_amount DECIMAL(15,2) DEFAULT 0,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_goal_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =====================
-- DEBT / LOAN
-- =====================
CREATE TABLE debt_loans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type ENUM('DEBT','LOAN') NOT NULL,
    person_name VARCHAR(100),
    total_amount DECIMAL(15,2) NOT NULL,
    remaining_amount DECIMAL(15,2) NOT NULL,
    interest_rate DECIMAL(5,2),
    due_date DATE,
    status ENUM('ONGOING','PAID') DEFAULT 'ONGOING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_debt_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =====================
-- RECURRING TRANSACTIONS
-- =====================
CREATE TABLE recurring_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    category_id BIGINT,
    type ENUM('INCOME','EXPENSE') NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    description VARCHAR(255),
    frequency ENUM('DAILY','WEEKLY','MONTHLY') NOT NULL,
    next_run_date DATE NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rt_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_rt_account FOREIGN KEY (account_id) REFERENCES accounts(id),
    CONSTRAINT fk_rt_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE email_queue (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             recipient VARCHAR(255) NOT NULL,
                             subject VARCHAR(255),
                             body TEXT,
                             status VARCHAR(50),
                             retry_count INT DEFAULT 0,
                             created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                             updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
                                 ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE refresh_token (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                token VARCHAR(255) NOT NULL UNIQUE,
                                user_id BIGINT NOT NULL,
                                revoked BOOLEAN DEFAULT FALSE,
                                expired_at DATETIME NOT NULL,
                                CONSTRAINT fk_refresh_token_user
                                    FOREIGN KEY (user_id) REFERENCES users(id)
                                        ON DELETE CASCADE
);
