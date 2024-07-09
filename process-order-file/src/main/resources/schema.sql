CREATE TABLE IF NOT EXISTS transaction
(
    id            SERIAL primary key,
    id_user       bigint,
    name          varchar(255),
    id_order      bigint,
    product_id    bigint,
    value_product decimal,
    date_order    date
);