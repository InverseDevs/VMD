package Application.Database;

import Application.Content.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserInfoRepository {
    private final String find_query = "select " +
            "users_info.user_id as user_id, " +
            "users_info.username as username, " +
            "users_info.name as name, " +
            "users_info.birth_town as birth_town, " +
            "users_info.birth_date as birth_date " +
            "from users_info ";

    private JdbcTemplate jdbc;

    @Autowired
    public UserInfoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Iterable<UserInfo> findAll() {
        return jdbc.query(find_query, this::mapRowToUserInfo);
    }

    public UserInfo findById(Long userId) {
        List<UserInfo> users = jdbc.query(find_query + "where user_id = ?", this::mapRowToUserInfo, userId);
        return users.size() == 0 ? null : users.get(0);
    }

    public UserInfo findByUsername(String username) {
        List<UserInfo> users = jdbc.query(find_query + "where username = ?", this::mapRowToUserInfo, username);
        return users.size() == 0 ? null : users.get(0);
    }

    public UserInfo save(UserInfo info) {
        jdbc.update("insert into users_info(user_id, username, name, birth_town, birth_date) values (?,?,?,?,?)",
                    info.getUserId(), info.getUsername(), info.getName(), info.getBirthTown(), info.getBirthDate());
        return info;
    }

    public UserInfo deleteById(Long userId) {
        UserInfo info = this.findById(userId);
        jdbc.update("delete from users_info where user_id = ?", userId);
        return info;
    }

    public UserInfo deleteByUsername(String username) {
        UserInfo info = this.findByUsername(username);
        jdbc.update("delete from users_info where username = ?", username);
        return info;
    }

    private UserInfo mapRowToUserInfo(ResultSet rs, int rowNum) throws SQLException {
        UserInfo info = new UserInfo();
        info.setUserId(rs.getLong("user_id"));
        info.setName(rs.getString("name"));
        info.setUsername(rs.getString("username"));
        info.setBirthTown(rs.getString("birth_town"));
        info.setBirthDate(rs.getDate("birth_date"));
        return info;
    }
}
