create table if not exists users(
  id       int,
  username varchar(256),
  password varchar(256)
);

create table if not exists roles(
  id       int,
  name     varchar(256)
);

create table if not exists user_to_role(
  user_id int,
  role_id int
);