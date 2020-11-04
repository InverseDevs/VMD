package Application.Entities.User;

import Application.Entities.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, message = "Не меньше 2 знаков")
    private String username;
    @Size(min = 2, message = "Не меньше 2 знаков")
    private String password;
    private String email;
    private String token;
    private Boolean permitted;
    // TODO убрать этот великолепный костыль, когда фронтэнд будет к этому готов
    @Transient
    private String passwordConfirm;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.permitted = true;
        this.token = UUID.randomUUID().toString().replace("-", "");
        this.passwordConfirm = null;
    }

    @ManyToMany
    @JoinTable(name = "user_to_role",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @ManyToMany
    @JoinTable(name = "friends",
    joinColumns = @JoinColumn(name = "user1_id"),
    inverseJoinColumns = @JoinColumn(name = "user2_id"))
    private Set<User> friends; // friends' ids

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                ", permitted=" + permitted +
                ", passwordConfirm='" + passwordConfirm + '\'' +
                '}';
    }

    // TODO временное решение! переписать!
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof User)) return false;
        return ((User) o).id.equals(this.id);
    }
}