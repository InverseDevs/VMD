create table if not exists t_user(
  id       int,
  username varchar(256),
  password varchar(256)
);

create table if not exists t_role(
  id       int,
  name     varchar(256)
);

create table if not exists t_user_roles(
  user_id int,
  role_id int
);