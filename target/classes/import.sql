INSERT INTO tb_roles(authorities) VALUES('1');
INSERT INTO tb_roles(authorities) VALUES('2');

-- User
INSERT INTO tb_user(email,password,deleted) VALUES('gustavo@gmail.com','$2a$10$H9EpOJEoqKL9prNlUO1PbuRGm7L3OxOsayV9IzZaBF4pP4R18rX1i', 0);
INSERT INTO tb_user(email,password,deleted) VALUES('giovana@gmail.com','$2a$10$H9EpOJEoqKL9prNlUO1PbuRGm7L3OxOsayV9IzZaBF4pP4R18rX1i', 0);
INSERT INTO tb_user(email,password,deleted) VALUES('felipe@gmail.com','$2a$10$H9EpOJEoqKL9prNlUO1PbuRGm7L3OxOsayV9IzZaBF4pP4R18rX1i', 0);
INSERT INTO tb_user(email,password,deleted) VALUES('beatriz@gmail.com','$2a$10$H9EpOJEoqKL9prNlUO1PbuRGm7L3OxOsayV9IzZaBF4pP4R18rX1i', 0);
INSERT INTO tb_user(email,password,deleted) VALUES('lucia@gmail.com','$2a$10$H9EpOJEoqKL9prNlUO1PbuRGm7L3OxOsayV9IzZaBF4pP4R18rX1i', 0);

---- ValidFor
--INSERT INTO tb_timeperiod(start_date_time,end_date_time,retire_time) VALUES ((TO_DATE('18/10/2022 00:00:00','DD/MM/YYYY HH24:MI:SS')),(TO_DATE('19/10/2022 00:00:00','DD/MM/YYYY HH24:MI:SS')),(TO_DATE('22/10/2022 00:00:00','DD/MM/YYYY HH24:MI:SS')));
--INSERT INTO tb_timeperiod(start_date_time,end_date_time,retire_time) VALUES ((TO_DATE('18/10/2022 15:30:00','DD/MM/YYYY HH24:MI:SS')),(TO_DATE('19/10/2022 00:00:00','DD/MM/YYYY HH24:MI:SS')),(TO_DATE('23/10/2022 00:00:00','DD/MM/YYYY HH24:MI:SS')));
--INSERT INTO tb_timeperiod(start_date_time,end_date_time,retire_time) VALUES ((TO_DATE('18/10/2022 20:00:00','DD/MM/YYYY HH24:MI:SS')),(TO_DATE('20/10/2022 00:00:00','DD/MM/YYYY HH24:MI:SS')),(TO_DATE('24/10/2022 00:00:00','DD/MM/YYYY HH24:MI:SS')));
-- ValidFor
INSERT INTO tb_timeperiod(start_date_time,end_date_time,retire_time) VALUES ("2022-10-10 00:00:00.000000","2022-10-10 00:00:00.000000","2022-10-10 00:00:00.000000");
INSERT INTO tb_timeperiod(start_date_time,end_date_time,retire_time) VALUES ("2022-10-10 00:00:00.000000","2022-10-10 00:00:00.000000","2022-10-10 00:00:00.000000");
INSERT INTO tb_timeperiod(start_date_time,end_date_time,retire_time) VALUES ("2022-10-10 00:00:00.000000","2022-10-10 00:00:00.000000","2022-10-10 00:00:00.000000");


-- Relacionamentos Users - Roles
INSERT INTO tb_user_roles(user_id, roles_id) VALUES( 1 , 1 );
INSERT INTO tb_user_roles(user_id, roles_id) VALUES( 2 , 2 );
INSERT INTO tb_user_roles(user_id, roles_id) VALUES( 3 , 2 );
INSERT INTO tb_user_roles(user_id, roles_id) VALUES( 4 , 2);
INSERT INTO tb_user_roles(user_id, roles_id) VALUES( 5 , 2);
------ Customers
INSERT INTO tb_customer(credit_score, customer_name, customer_status, customer_type, document_number, user_id, deleted) VALUES ('Perfect', 'Gustavo', 'Active', '1', 1111 , 1, 0);
INSERT INTO tb_customer(credit_score, customer_name, customer_status, customer_type, document_number, user_id, deleted) VALUES ('Good', 'Giovana', 'Inactive', '1', 2222 , 2, 0);
INSERT INTO tb_customer(credit_score, customer_name, customer_status, customer_type, document_number, user_id, deleted) VALUES ('Bad', 'Felipe', 'Active', '1', 3333 , 3, 0);
INSERT INTO tb_customer(credit_score, customer_name, customer_status, customer_type, document_number, user_id, deleted) VALUES ('Good', 'Beatriz', 'Inactive', '1', 4444 , 4, 0);
INSERT INTO tb_customer(credit_score, customer_name, customer_status, customer_type, document_number, user_id, deleted) VALUES ('Bad', 'Lucia', 'Active', '1', 2222 , 5, 0);

-- Addresses
INSERT INTO tb_address(address_type, complement, country, house_number, neighborhood, street, zip_code, client_id) VALUES (1, 'Apto 13' , 'Brazil', 114, 'Parque Amaral', 'Street 1', 11122333, 1);
INSERT INTO tb_address(address_type, complement, country, house_number, neighborhood, street, zip_code, client_id) VALUES (2, '-' , 'Brazil', 67, 'Center', 'Street 1', 11122333, 1);
INSERT INTO tb_address(address_type, complement, country, house_number, neighborhood, street, zip_code, client_id) VALUES (1, '-'  , 'Brazil', 11, 'Center', 'Street 1', 11122333, 2);
INSERT INTO tb_address(address_type, complement, country, house_number, neighborhood, street, zip_code, client_id) VALUES (1, 'Apto 101' , 'Brazil', 230, 'Center', 'Street 2', 14, 3);
INSERT INTO tb_address(address_type, complement, country, house_number, neighborhood, street, zip_code, client_id) VALUES (3, '-' , 'Brazil', 455, 'Cidade Nova', 'Street 3', 10, 4);
INSERT INTO tb_address(address_type, complement, country, house_number, neighborhood, street, zip_code, client_id) VALUES (1, 'Apto 304' , 'Brazil', 25, 'Jardim das Flores', 'Street 7', 33, 5);
-- Characteristics
INSERT INTO tb_characteristic(name, value_type, type )VALUES ('Books', 'Value Type', 1 );
INSERT INTO tb_characteristic(name, value_type, type ) VALUES ('Fiction Books', 'Value Type', 1);
INSERT INTO tb_characteristic(name, value_type, type ) VALUES ('Romance Books', 'Value Type', 2);
INSERT INTO tb_characteristic(name, value_type, type ) VALUES ('Educational Books', 'Value Type', 2);
INSERT INTO tb_characteristic(name, value_type, type ) VALUES ('Businees Books', 'Value Type', 1);
INSERT INTO tb_characteristic(name, value_type, type ) VALUES ('Eletronics', 'Value Type', 1);
INSERT INTO tb_characteristic(name, value_type, type ) VALUES ('Adventure Books', 'Value Type', 1);
INSERT INTO tb_characteristic(name, value_type, type ) VALUES ('Computers', 'Value Type', 1);
INSERT INTO tb_characteristic(name, value_type, type ) VALUES ('Smartphones', 'Value Type', 2);
INSERT INTO tb_characteristic(name, value_type, type ) VALUES ('Smart Home', 'Value Type', 2);
INSERT INTO tb_characteristic(name, value_type, type ) VALUES ('Products on Offer', 'Value Type', 1);
-- ProductOffering
INSERT INTO tb_productoffering(name, sell_indicator, state) VALUES ('The Lord of the Rings: the two towers', 1, 1);
INSERT INTO tb_productoffering(name, sell_indicator,state) VALUES ('Harry Potter', 1, 1);
INSERT INTO tb_productoffering(name, sell_indicator,state) VALUES ('Atomic Habits', 1, 1);
INSERT INTO tb_productoffering(name, sell_indicator,state) VALUES ('The Lord of the Rings: the ring society', 1, 1);
--ProductOfferings_Characteristics
INSERT INTO tb_product_characteristic(productoffering_id,characteristic_id) VALUES ( 1, 2);
INSERT INTO tb_product_characteristic(productoffering_id,characteristic_id) VALUES (2, 2 );
INSERT INTO tb_product_characteristic(productoffering_id,characteristic_id) VALUES (2, 1 );
INSERT INTO tb_product_characteristic(productoffering_id,characteristic_id) VALUES (2, 7 );
INSERT INTO tb_product_characteristic(productoffering_id,characteristic_id) VALUES (2, 11);
INSERT INTO tb_product_characteristic(productoffering_id,characteristic_id) VALUES (3, 4 );
INSERT INTO tb_product_characteristic(productoffering_id,characteristic_id) VALUES (3, 1 );
INSERT INTO tb_product_characteristic(productoffering_id,characteristic_id) VALUES (3, 11 );
INSERT INTO tb_product_characteristic(productoffering_id,characteristic_id) VALUES (4, 2 );
INSERT INTO tb_product_characteristic(productoffering_id,characteristic_id) VALUES (4, 1 );
INSERT INTO tb_product_characteristic(productoffering_id,characteristic_id) VALUES (4, 7 );
------  Order
INSERT INTO tb_order(instant, client_id, deleted) VALUES (current_timestamp, 1, 0 );
INSERT INTO tb_order(instant, client_id, deleted) VALUES (current_timestamp, 4, 0 );
INSERT INTO tb_order(instant, client_id, deleted) VALUES (current_timestamp, 2, 0 );
--------  Order
--INSERT INTO tb_order(instant, client_id, deleted) VALUES ((TO_DATE('18/10/2022 00:00:00','DD/MM/YYYY HH24:MI:SS')), 1, 0 );
--INSERT INTO tb_order(instant, client_id, deleted) VALUES ((TO_DATE('18/10/2022 00:00:00','DD/MM/YYYY HH24:MI:SS')), 4, 0 );
--INSERT INTO tb_order(instant, client_id, deleted) VALUES ((TO_DATE('18/10/2022 00:00:00','DD/MM/YYYY HH24:MI:SS')), 2, 0 );
-- OrderItem
INSERT INTO tb_order_item(product_id,order_id,discount,quantity,total_price) VALUES (1, 2 , 100.0, 10, 1200.0 );
INSERT INTO tb_order_item(product_id,order_id,discount,quantity,total_price) VALUES (4, 3 ,50.0, 2, 500.0 );
INSERT INTO tb_order_item(product_id,order_id,discount,quantity,total_price) VALUES (3, 1 , 70.0, 5, 700.0 );

-- Payments