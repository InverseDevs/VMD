INSERT INTO roles(name)
VALUES ('ROLE_USER'), ('ROLE_ADMIN');

INSERT INTO users(username, password, permitted)
VALUES ('admin', 'admin', TRUE);
INSERT INTO user_to_role(user_id, role_id)
VALUES (1, 2);

insert into users(username, password, email, token, permitted)
values ('test1', '1234', 'test1@vmd.com', '41e05828b3d94dd0b05b42d7b4d31537', TRUE);

insert into users(username, password, email, token, permitted)
values ('test2', '1234', 'test2@vmd.com', '41e05828b3d94dd0b05b55d7b4d31537', TRUE);

insert into user_to_role(user_id, role_id)
values (2, 1);

insert into user_to_role(user_id, role_id)
values (3, 1);