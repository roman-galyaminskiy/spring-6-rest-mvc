drop table if exists beer_order_shipment cascade;

create table beer_order_shipment
(
    id              varchar     not null,
    beer_order_id   varchar     not null,
    tracking_number varchar(50) not null,
    version         integer,
    created_date    timestamp(6),
    update_date     timestamp(6),
    primary key (id),
    CONSTRAINT beer_order_shipment_beer_order_id
        FOREIGN KEY (beer_order_id)
            REFERENCES beer_order (id)
);