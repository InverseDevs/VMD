package Application.Database;

import Application.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepository implements DBRepository<User> {
    private JdbcTemplate jdbc;

    @Autowired
    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Iterable<User> findAll() {
        return jdbc.query("select id, username, password from users", this::mapRowToUser);
    }

    @Override
    public User findById(Long id) {
        List<User> users = jdbc.query("select id, username, password from users where id =" + id,
                this::mapRowToUser);

        return users.size() != 0 ? users.get(0) : null;
    }

    public User findByUsername(String username) {
        List<User> users = jdbc.query("select id, username, password from users where username ='" + username + "'",
                this::mapRowToUser);

        return users.size() != 0 ? users.get(0) : null;
    }

    @Override
    public User save(User user) {
        jdbc.update("insert into users (username, password) values (?,?)",
                user.getUsername(),
                user.getPassword());
        return user;
    }

    @Override
    public User deleteById(Long id) {
        User userToRemove = findById(id);
        jdbc.update("delete from users where id = ?", id);
        return userToRemove;
    }

    public User deleteByUsername(String username) {
        User userToRemove = findByUsername(username);
        jdbc.update("delete from users where username = '?'", username);
        return userToRemove;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();

        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));

        return user;
    }
}
