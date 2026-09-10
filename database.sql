-- E-Commerce Web Application - Database Script (PostgreSQL)
--
-- Usage:
--   psql -U postgres -f database.sql
--
-- This script drops and recreates the postgres database, then connects
-- to it to create the tables and insert sample data.

DROP DATABASE IF EXISTS postgres;
CREATE DATABASE postgres;

\c postgres

-- ============================================================
-- Tables
-- ============================================================

CREATE TABLE users (
    id       SERIAL PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(20)  NOT NULL CHECK (role IN ('admin', 'customer'))
);

CREATE TABLE customers (
    id                 SERIAL PRIMARY KEY,
    user_id            INT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    name               VARCHAR(100)  NOT NULL,
    email              VARCHAR(100)  NOT NULL UNIQUE,
    phone              VARCHAR(20),
    address            VARCHAR(255),
    registration_date  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categories (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE products (
    id          SERIAL PRIMARY KEY,
    category_id INT NOT NULL REFERENCES categories(id) ON DELETE RESTRICT,
    name        VARCHAR(150) NOT NULL,
    description TEXT,
    price       NUMERIC(10,2) NOT NULL CHECK (price >= 0),
    stock       INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    image       VARCHAR(255)
);

CREATE TABLE orders (
    id            SERIAL PRIMARY KEY,
    customer_id   INT NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    order_date    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_amount  NUMERIC(10,2) NOT NULL CHECK (total_amount >= 0),
    status        VARCHAR(20) NOT NULL DEFAULT 'Pending'
                  CHECK (status IN ('Pending', 'Confirmed', 'Shipped', 'Delivered', 'Cancelled'))
);

CREATE TABLE order_items (
    id          SERIAL PRIMARY KEY,
    order_id    INT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id  INT NOT NULL REFERENCES products(id),
    quantity    INT NOT NULL CHECK (quantity > 0),
    price       NUMERIC(10,2) NOT NULL CHECK (price >= 0)
);

CREATE INDEX idx_customers_user_id   ON customers(user_id);
CREATE INDEX idx_products_category   ON products(category_id);
CREATE INDEX idx_orders_customer_id  ON orders(customer_id);
CREATE INDEX idx_order_items_order   ON order_items(order_id);
CREATE INDEX idx_order_items_product ON order_items(product_id);

-- ============================================================
-- Sample data
-- ============================================================

-- Categories
INSERT INTO categories (name, description) VALUES
('Electronics', 'Electronic gadgets and devices'),
('Clothing', 'Apparel for men and women'),
('Books', 'Fiction and non-fiction books'),
('Accessories', 'Bags, watches and other accessories');

-- Products
INSERT INTO products (category_id, name, description, price, stock, image) VALUES
(1, 'Laptop', '15-inch laptop with 16GB RAM and 512GB SSD', 800.00, 15, 'laptop.jpg'),
(1, 'Wireless Mouse', 'Ergonomic wireless mouse', 20.00, 100, 'mouse.jpg'),
(1, 'Bluetooth Headphones', 'Noise-cancelling over-ear headphones', 60.00, 40, 'headphones.jpg'),
(1, 'Smartphone', '6.5-inch display, 128GB storage', 500.00, 25, 'smartphone.jpg'),
(2, 'Men''s T-Shirt', '100% cotton crew neck t-shirt', 15.00, 200, 'tshirt.jpg'),
(2, 'Women''s Jacket', 'Water-resistant winter jacket', 45.00, 60, 'jacket.jpg'),
(3, 'Clean Code', 'A handbook of agile software craftsmanship', 35.00, 50, 'clean-code.jpg'),
(3, 'The Pragmatic Programmer', 'Your journey to mastery', 32.00, 50, 'pragmatic-programmer.jpg'),
(4, 'Leather Wallet', 'Genuine leather bifold wallet', 25.00, 80, 'wallet.jpg'),
(4, 'Analog Watch', 'Stainless steel analog wrist watch', 55.00, 30, 'watch.jpg');

-- Admin user
INSERT INTO users (username, password, role) VALUES
('admin', 'admin123', 'admin');

-- Customer users
INSERT INTO users (username, password, role) VALUES
('jdoe', 'customer123', 'customer'),
('asmith', 'customer123', 'customer');

-- Customers linked to the users above
INSERT INTO customers (user_id, name, email, phone, address) VALUES
((SELECT id FROM users WHERE username = 'jdoe'),   'John Doe',    'jdoe@example.com',   '0100000001', '123 Main St, Cairo'),
((SELECT id FROM users WHERE username = 'asmith'), 'Alice Smith', 'asmith@example.com', '0100000002', '456 Nile Ave, Giza');
