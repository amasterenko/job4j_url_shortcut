CREATE TABLE sites (
    id serial primary key,
    name varchar(100) not null unique,
    login varchar(100) not null unique,
    password varchar(100) not null unique
);
CREATE TABLE urlstat (
                         id serial primary key,
                         count int not null default 0
);
CREATE TABLE urls (
    id serial primary key,
    url varchar(250) not null unique,
    shortcut varchar(50) not null unique,
    site_id int not null references sites(id),
    urlstat_id int not null references urlstat(id)
);

