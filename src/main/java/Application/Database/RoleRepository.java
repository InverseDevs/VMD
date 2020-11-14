package Application.Database;

import Application.Entities.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByName(String name);
    Role deleteByName(String name);
}