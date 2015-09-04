IN PSQL:

Guest=# CREATE DATABASE shoe_stores;
shoe_stores=# CREATE TABLE stores (id serial PRIMARY KEY, store_name varchar);
shoe_stores=# CREATE TABLE brands (id serial PRIMARY KEY, brand_name varchar);
shoe_stores=# CREATE TABLE stores_brands (id serial PRIMARY KEY, brand_id int, store_id int);
shoe_stores=# CREATE DATABASE shoe_stores_test WITH TEMPLATE shoe_stores;
