INSERT INTO roles(id, name) VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');

INSERT INTO users (id, username, password) VALUES (1, 'admin', 'admin');
INSERT INTO user_to_role(user_id, role_id) VALUES (1, 2);