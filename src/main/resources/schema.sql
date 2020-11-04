-- TODO УБРАТЬ УБРАТЬ УБРАТЬ ПЕРЕД РЕЛИЗОМ ЭТО ТОЛЬКО ДЛЯ ТЕСТИРОВАНИЯ !!!
drop table users;
drop table roles;
drop table user_to_role;
drop table chats;
drop table messages;
drop table friends;
drop table users_info;
drop table wall_posts;
--
--create table if not exists users(
--  id        serial,
--  username  varchar(256),
--  password  varchar(256),
--  email     varchar(256),
--  token     varchar(256),
--  permitted boolean
--);
--
--create table if not exists roles(
--  id       serial,
--  name     varchar(256)
--);
--
--create table if not exists user_to_role(
--  user_id int,
--  role_id int
--);

create table if not exists chats(
  id          serial,
  sender_id   int,
  receiver_id int
);

create table if not exists messages(
  chat_id  int,
  sender   varchar(256),
  message  varchar(256)
);

--create table if not exists friends(
--  user1_id int,
--  user2_id int
--);
--
--create table if not exists users_info(
--  user_id       int,
--  username      varchar(256),
--  name          varchar(256),
--  birth_town    varchar(256),
--  birth_date    date
--);
--
--create table if not exists wall_posts(
--  id            serial,
--  pageType     varchar(16),
--  pageId       int,
--  sender        varchar(256),
--  message       varchar(256),
--  sentTime     date
--);