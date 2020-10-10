package Application.Database;

import Application.Entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class RoleRepository {
    private JdbcTemplate jdbc;

    @Autowired
    public RoleRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Iterable<Role> findAll() {
        return jdbc.query("select id, name from roles", this::mapRowToRole);
    }

    public Role findById(Long id) {
        return jdbc.queryForObject("select id, name from roles where id =" + id,
                this::mapRowToRole, id);
    }

    public Role findByRoleName(String name) {
        return jdbc.queryForObject("select id, name from roles where name = '" + name + "'",
                this::mapRowToRole, name);
    }

    public Role save(Role role) {
        jdbc.update("insert into roles (name) values (?)",
                role.getId(),
                role.getName());
        return role;
    }

    public Role deleteById(Long id) {
        Role roleToRemove = findById(id);
        jdbc.update("delete from roles where id = ?", id);
        return roleToRemove;
    }

    public Role deleteByName(String name) {
        Role roleToRemove = findByRoleName(name);
        jdbc.update("delete from roles where name = '?'", name);
        return roleToRemove;
    }

    private Role mapRowToRole(ResultSet resultSet, int rowNum) throws SQLException {
        Role role = new Role();

        role.setId(resultSet.getLong("id"));
        role.setName(resultSet.getString("name"));

        return role;
    }
}
