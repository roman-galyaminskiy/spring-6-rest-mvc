drop table if exists beer_order cascade;

drop table if exists beer_order_line cascade;

create table beer_order
(
    id           varchar not null,
    customer_id  varchar not null,
    customer_ref varchar(255),
    version      integer,
    created_date timestamp(6),
    update_date  timestamp(6),
    primary key (id),
    CONSTRAINT fk_customer
        FOREIGN KEY (customer_id)
            REFERENCES customer (id)
);

create table beer_order_line
(
    id                 varchar not null,
    beer_id            varchar not null,
    beer_order_id      varchar not null,
    order_quantity     integer,
    quantity_allocated integer,
    version            integer,
    created_date       timestamp(6),
    update_date        timestamp(6),
    primary key (id),
    CONSTRAINT fk_beer
        FOREIGN KEY (beer_id)
            REFERENCES beer(id),
    CONSTRAINT fk_beer_order
        FOREIGN KEY (beer_order_id)
            REFERENCES beer_order(id)
);