INSERT INTO roles(name)
VALUES ('ROLE_USER'), ('ROLE_ADMIN');

INSERT INTO users(username, password, permitted)
VALUES ('admin', 'admin', TRUE);
INSERT INTO user_to_role(user_id, role_id)
VALUES (1, 2);

insert into users(username, password, email, token, permitted)
values ('test1', '1234', 'test1@vmd.com', '41e05828b3d94dd0b05b42d7b4d31537', TRUE);

insert into users(username, password, email, token, permitted)
values ('test2', '1234', 'test2@vmd.com', '41e05828b3d94dd0b05112d7b4d31537', TRUE);

insert into users(username, password, email, token, permitted)
values ('skelantros', '23052001', 'skelantros@vmd.com', '41e05828b3d94dd0b05b55d7b4d31537', TRUE);

insert into user_to_role(user_id, role_id)
values (2, 1);

insert into user_to_role(user_id, role_id)
values (3, 1);

insert into user_to_role(user_id, role_id)
values (4, 1);

insert into users_info(user_id, username, name, birth_town, birth_date)
values (4, 'skelantros', 'Alex Egorowski', 'Zelenokumsk', '2001-05-23');

insert into users_info(user_id, username, name, birth_town, birth_date)
values (2, 'test1', 'Test account', 'VMD Network', '2020-09-12');

insert into users_info(user_id, username, name, birth_town, birth_date)
values (3, 'test2', 'Test account', 'VMD Network', '2020-09-12');

insert into wall_posts(id, page_type, page_id, sender, message, sent_time)
values (1, 'USER', 4, 'skelantros', 'Hello VMD!', '2020-10-14');

insert into wall_posts(id, page_type, page_id, sender, message, sent_time)
values (2, 'USER', 4, 'test1', 'Hello skelantros!', '2020-10-15');