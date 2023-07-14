alter table beer DROP column beer_style;
alter table beer add column beer_style varchar(255) not null;
