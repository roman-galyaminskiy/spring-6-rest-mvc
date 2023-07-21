drop table if exists category cascade;

drop table if exists beer_category cascade;

create table category
(
    id           varchar not null,
    description  varchar(255) not null,
    version      integer,
    created_date timestamp(6),
    update_date  timestamp(6),
    primary key (id)
);

create table beer_category
(
    beer_id            varchar not null,
    category_id      varchar not null,
    primary key (beer_id, category_id),
    CONSTRAINT fk_beer_category_beer_id
        FOREIGN KEY (beer_id)
            REFERENCES beer(id),
    CONSTRAINT fk_beer_category_category
        FOREIGN KEY (category_id)
            REFERENCES category(id)
);