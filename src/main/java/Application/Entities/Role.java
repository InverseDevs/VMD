package Application.Entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    // TODO на данный момент идентификатор роли не генерируется, в будущем исправить
    @Id
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "user_to_role",
    joinColumns = @JoinColumn(name = "role_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

    public Role() {
    }

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return getName();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Role && ((Role) o).id.equals(this.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}