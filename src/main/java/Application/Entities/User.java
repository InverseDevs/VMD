package Application.Entities;

import lombok.*;
import org.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
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
    //@Setter(AccessLevel.PRIVATE)
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

    private String name;
    private String birthTown;
    private Date birthDate;

    @Lob
    @Column(columnDefinition = "BYTEA")
    private byte[] avatar;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.permitted = true;
        this.token = UUID.randomUUID().toString().replace("-", "");
        this.passwordConfirm = null;
    }

    public User(String username, String password, String email,
                String name, String birthTown, Date birthDate) {
        this(username, password, email);
        this.name = name;
        this.birthDate = birthDate;
        this.birthTown = birthTown;
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
                ", name='" + name + '\'' +
                ", birthTown='" + birthTown + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }

    public JSONObject toJson() {
        JSONObject user = new JSONObject();
        user.put("id", this.getId());
        user.put("username", this.getUsername());
        user.put("email", this.getEmail());
        user.put("token", this.getToken());
        user.put("name", this.getName());
        user.put("birth_town", this.getBirthTown());
        user.put("birth_date", this.getBirthDate());
        user.put("avatar", new String(avatar));
        user.put("role", this.getRoles().toString());
        user.put("friends", this.getFriends().toString());

        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return user.username.equals(this.username)
                && user.email.equals(this.email)
                && user.token.equals(this.token);
    }
}