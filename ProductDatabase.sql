CREATE DATABASE IF NOT EXISTS ProductDatabase;

USE ProductDatabase;
CREATE TABLE IF NOT EXISTS Product (
	PRODUCTID VARCHAR(10) PRIMARY KEY NOT NULL,
    PRODUCTNAME VARCHAR(30) NOT NULL,
    QUANTITY INT NOT NULL,
    PRICE DECIMAL(7,2) NOT NULL,
    CATEGORY VARCHAR(20)
);

select * From productdatabase.product
WHere productName Like '%1%';
select * from productdatabase.product 