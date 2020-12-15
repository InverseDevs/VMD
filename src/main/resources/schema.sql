create table if not exists users
(
  id                 serial,
  username           varchar(256),
  password           varchar(256),
  email              varchar(256),
  permitted          boolean,
  name               varchar(256),
  birth_town         varchar(256),
  birth_date         timestamp,
  avatar             bytea,
  round              bytea,
  study_place        varchar(256),
  languages          varchar(256),
  phone              varchar(256),
  hobbies            varchar(256),
  online             boolean,
  wall_id            int,
  post_access        varchar(32),
  comment_access     varchar(32),
  message_access     varchar(32),
  last_online        timestamp
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
  id                   serial,
  picture              bytea
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
  chat_id   int,
  message   varchar(256),
  sent_time timestamp
);

create table if not exists friends
(
  user1_id int,
  user2_id int
);

create table if not exists friend_requests
(
  to_user   int,
  from_user int
);

create table if not exists wall_posts
(
  id            serial,
  sender_id     int,
  message       varchar(256),
  sent_time     timestamp,
  wall_id       int,
  picture       bytea
);

create table if not exists likes
(
  post_id int,
  user_id int
);

create table if not exists likes_comments
(
  comment_id int,
  user_id    int
);

create table if not exists comments
(
  id                serial,
  message           varchar(256),
  sent_time         timestamp,
  sender_id         bigint,
  reference_comment bigint,
  post_id           bigint,
  picture           bytea
);

create table if not exists walls
(
  id        serial,
  type      varchar(16),
  user_id   int,
  group_id  int
);

create table if not exists groups
(
  id             serial,
  name           varchar(256),
  named_link     varchar(64),
  owner_id       int,
  wall_id        int,
  picture        bytea,
  comment_access varchar(32),
  post_access    varchar(32)
);

create table if not exists group_admins
(
  group_id int,
  admin_id int
);

create table if not exists group_bans
(
  group_id int,
  user_id  int
);

create table if not exists group_members
(
  group_id int,
  user_id  int
);