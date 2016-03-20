CREATE TABLE order_items (
    id         SERIAL PRIMARY KEY,
    order_id   INT,                 --Должен быть FK на orders
    goods_name VARCHAR(255),        --Должен быть FK на goods
    value      INT
);



create table remains (
    id         SERIAL PRIMARY KEY,
    goods_name VARCHAR(255),        --Должен быть FK на goods
    value      INT                  --Величина остатка
);

create unique index on remains(goods_name);