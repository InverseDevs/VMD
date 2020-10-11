package Application.Database;

import Application.Entities.Role;
import Application.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
public class UserRepository {
    private JdbcTemplate jdbc;

    @Autowired
    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Iterable<User> findAll() {
        return jdbc.query("select " +
                "users.id as id, " +
                "users.username as username, " +
                "users.password as password, " +
                "users.email as email, " +
                "users.token as token, " +
                "users.permitted as permitted, " +
                "roles.id as role_id, " +
                "roles.name as role, " +
                "friends.user2_id as friend_id " +
                "from users " +
                "inner join user_to_role on users.id = user_to_role.user_id " +
                "inner join roles on roles.id = user_to_role.role_id " +
                "left join friends on users.id = friends.user1_id", this::mapRowToUser);
    }

    private User findByParameter(Object parameter, String parameter_token) {
        List<User> users = jdbc.query("select " +
                        "users.id as id, " +
                        "users.username as username, " +
                        "users.password as password, " +
                        "users.email as email, " +
                        "users.token as token, " +
                        "users.permitted as permitted, " +
                        "roles.id as role_id, " +
                        "roles.name as role, " +
                        "friends.user2_id as friend_id " +
                        "from users " +
                        "inner join user_to_role on users.id = user_to_role.user_id " +
                        "inner join roles on roles.id = user_to_role.role_id " +
                        "left join friends on users.id = friends.user1_id " +
                        "where users." + parameter_token + " = ? ", this::mapRowToUser, parameter);

        return users.size() != 0 ? users.get(0) : null;
    }

    public User findById(Long id) {
        return this.findByParameter(id, "id");
    }

    public User findByUsername(String username) {
        return this.findByParameter(username, "username");
    }

    public User findByEmail(String email) {
        return this.findByParameter(email, "email");
    }

    public User findByToken(String token) {
        return this.findByParameter(token, "token");
    }

    public void permitUser(String token) {
        jdbc.update("update users set permitted = true where token = '" + token + "'");
    }

    public User save(User user) {
        jdbc.update("insert into users (username, password, email, token, permitted) values (?,?,?,?, false)",
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getToken());
        User newUser = jdbc.queryForObject("select id from users where username = '" + user.getUsername() + "'", this::mapRowToId);
        jdbc.update("insert into user_to_role (user_id, role_id) values (?,?)", newUser.getId(), 1);
        return newUser;
    }

    public void addFriend(User user, Long friendId) {
        jdbc.update("insert into friends (user1_id, user2_id) values (?,?)", user.getId(), friendId);
    }

    public void deleteFriend(User user, Long friendId) {
        jdbc.update("delete from friends where user1_id = ? and user2_id = ?", user.getId(), friendId);
        jdbc.update("delete from friends where user2_id = ? and user1_id = ?", user.getId(), friendId);
    }

    public User deleteById(Long id) {
        User userToRemove = findById(id);
        jdbc.update("delete from users where id = ?", id);
        jdbc.update("delete from user_to_role where user_id = ?", id);
        return userToRemove;
    }

    public User deleteByUsername(String username) {
        User userToRemove = findByUsername(username);
        jdbc.update("delete from users where username = '?'", username);
        jdbc.update("delete from user_to_role where user_id = ?", userToRemove.getId());
        return userToRemove;
    }

    public void makeAdmin(User user) {
        jdbc.update("update user_to_role set role_id = 2 where user_id = " + user.getId());
    }

    public void makeUser(User user) {
        jdbc.update("update user_to_role set role_id = 1 where user_id = " + user.getId());
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();

        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setEmail(resultSet.getString("email"));
        user.setToken(resultSet.getString("token"));
        user.setPermitted(resultSet.getBoolean("permitted"));
        user.setRoles(Collections.singleton(new Role(resultSet.getLong("role_id"), resultSet.getString("role"))));
        user.setFriends(Collections.singleton(resultSet.getLong("friend_id")));

        return user;
    }

    private User mapRowToId(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();

        user.setId(resultSet.getLong("id"));
        return user;
    }
}
