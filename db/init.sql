ALTER SESSION SET CONTAINER = FREEPDB1;
ALTER SESSION SET CURRENT_SCHEMA = SHOP;

CREATE TABLE products (
                          id    NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          name  VARCHAR2(100) NOT NULL,
                          price NUMBER(10,2)  NOT NULL
);

INSERT INTO products (name, price) VALUES ('Laptop', 1999.99);
INSERT INTO products (name, price) VALUES ('Mouse', 25.50);
COMMIT;


CREATE TABLE orders (
                        id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        product_id  NUMBER        NOT NULL,
                        quantity    NUMBER(10)    NOT NULL,
                        total_price NUMBER(12,2)  NOT NULL
);