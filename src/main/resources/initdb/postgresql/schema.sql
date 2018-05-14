create table IF NOT EXISTS countries
(
  id varchar(50) not null
    constraint countries_pkey
    primary key,
  name varchar(50)
)
;

create table IF NOT EXISTS hotels
(
  id varchar(50) not null
    constraint table_name_id_pk
    primary key,
  name varchar(50),
  phone varchar(50),
  country_id varchar(50)
    constraint hotels_countries_id_fk
    references countries,
  stars integer
)
;

create table IF NOT EXISTS reviews
(
  id varchar(50) not null
    constraint reviews_pkey
    primary key,
  tour_id varchar(50),
  user_id varchar(50),
  content varchar(50)
)
;

create table IF NOT EXISTS tours
(
  id varchar(50) not null
    constraint tours_pkey
    primary key,
  photo bytea,
  date timestamp,
  duration integer,
  country_id varchar(50)
    constraint tours_countries_id_fk
    references countries,
  hotel_id varchar(50)
    constraint tours_hotels_id_fk
    references hotels,
  tour_type_id integer,
  description varchar(50),
  cost numeric
)
;

alter table reviews
  add constraint reviews_tours_id_fk
foreign key (tour_id) references tours
;

create table IF NOT EXISTS users
(
  id varchar(50) not null
    constraint users_id_pk
    primary key,
  login varchar(20),
  password varchar(20)
)
;

alter table reviews
  add constraint reviews_users_id_fk
foreign key (user_id) references users
;

create table IF NOT EXISTS users_to_tours
(
  user_id varchar(50)
    constraint users_fk
    references users,
  tour_id varchar(50)
    constraint tours_fk
    references users,
  users_to_tours_id serial not null
    constraint users_to_tours_users_to_tours_id_pk
    primary key
)
;

create table IF NOT EXISTS tour_types
(
  id serial not null
    constraint tour_types_pkey
    primary key,
  name varchar(50)
)
;

alter table tours
  add constraint tours_tour_types_id_fk
foreign key (tour_type_id) references tour_types
;

