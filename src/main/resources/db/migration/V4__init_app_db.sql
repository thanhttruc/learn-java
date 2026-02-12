USE app_db;

ALTER TABLE categories
DROP FOREIGN KEY fk_category_parent;

ALTER TABLE categories
DROP COLUMN parent_id;
