# Java_psql_ShoeBrandInventory

##### Database to search for brands in shoe stores and vice versa, Sep. 3, 2015

#### By Marie L. Mendez-Nunez

## Description

This application can be used by a shoe store chain to add/edit/delete Stores.  Users can also add/edit/delete shoe brands.  

There is a many-to-many relationship between brands and shoe stores, so many shoe stores can carry many brands a brand of shoes can be carried in many stores.  A join table stores these relationships

Users can search all the stores.  Each store lists its inventory of brands.  
Users can search all brands.  A lists of stores that stock the brand is available. 

## Setup

* Database must be created using psql.
	IN PSQL:
	Guest=# CREATE DATABASE shoe_stores;
	Guest=# /c shoe_stores;
	shoe_stores=# CREATE TABLE stores (id serial PRIMARY KEY, store_name varchar);
	shoe_stores=# CREATE TABLE brands (id serial PRIMARY KEY, brand_name varchar);
	shoe_stores=# CREATE TABLE stores_brands (id serial PRIMARY KEY, brand_id int, store_id int);
	shoe_stores=# CREATE DATABASE shoe_stores_test WITH TEMPLATE shoe_stores;

* To run tests, enter gradle test.  
* To run program, enter gradle run. 

* Dependencies: spark-core 2.1, velocity 1.7, sql2o 1.5.4, postgresql 9.4-1201-jdbc41, 
  junit 4.+, fluentlenium-core 0.10.3, fluentlenium-assertj 0.10.3, 

## Technologies Used

* HTML/CSS, Java, Sql, Velocity

### Legal
Copyright (c) 2015 Marie L. Mendez-Nunez

This software is licensed under the MIT license.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.