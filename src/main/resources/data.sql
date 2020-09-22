INSERT INTO roles(name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');

INSERT INTO users (username, password) VALUES ('admin', 'admin');
INSERT INTO user_to_role(user_id, role_id) VALUES (1, 2);