-- TODO УБРАТЬ УБРАТЬ УБРАТЬ ПЕРЕД РЕЛИЗОМ ЭТО ТОЛЬКО ДЛЯ ТЕСТИРОВАНИЯ !!!
drop table users;
drop table roles;
drop table user_to_role;
drop table chats;
drop table messages;
drop table friends;
drop table users_info;
drop table wall_posts;

create table if not exists users
(
  id            serial,
  username      varchar(256),
  password      varchar(256),
  email         varchar(256),
  permitted     boolean,
  name          varchar(256),
  birth_town    varchar(256),
  birth_date    date,
  avatar        bytea
);

create table if not exists roles
(
  id       serial,
  name     varchar(256)
);

create table if not exists user_to_role
(
  user_id int,
  role_id int
);

create table if not exists chats
(
  id          serial
);

create table if not exists chats_to_users
(
  chat_id   int,
  user_id   int
);

create table if not exists messages
(
  id        serial,
  sender_id int,
  type      varchar(64),
  chat_id   int,
  message   varchar(256),
  sent_time date
);

create table if not exists friends
(
  user1_id int,
  user2_id int
);

create table if not exists wall_posts
(
  id            serial,
  page_type     varchar(16),
  page_id       int,
  sender_id     int,
  message       varchar(256),
  sent_time     date
);

create table if not exists likes
(
  post_id int,
  user_id int
);

create table if not exists likes_comments
(
  comment_id int,
  user_id int
);

create table if not exists comments
(
  id                serial,
  message           varchar(256),
  sent_time         timestamp,
  type              varchar(256),
  sender_id         bigint,
  reference_comment bigint,
  post_id           bigint
);