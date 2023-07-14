
    drop table if exists beer cascade;

    drop table if exists customer cascade;

    create table beer (
       id varchar not null,
        version integer,
        beer_name varchar(255) not null,
        beer_style smallint not null,
        created_date timestamp(6),
        price numeric(38,2) not null,
        quantity_on_hand integer,
        upc varchar(255) not null,
        update_date timestamp(6),
        primary key (id)
    );

    create table customer (
       id varchar not null,
        version integer,
        created_date timestamp(6),
        name varchar(255),
        update_date timestamp(6),
        primary key (id)
    );

    alter table if exists beer 
       add constraint UK_nbvqgsw4s7o6c5gqlstyt96ql unique (beer_name);
