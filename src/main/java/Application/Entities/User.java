package Application.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

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
    private Boolean permitted;

    private String name;
    private String birthTown;
    private String studyPlace;
    private String languages;
    private String phone;
    private String hobbies;
    private LocalDate birthDate;
    private Boolean online;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] avatar;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] round;

    @ManyToMany
    @JoinTable(name = "user_to_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @ManyToMany
    @JoinTable(name = "friends",
            joinColumns = @JoinColumn(name = "user1_id"),
            inverseJoinColumns = @JoinColumn(name = "user2_id"))
    private Set<User> friends;

    @ManyToMany
    @JoinTable(name = "friendRequests",
            joinColumns = @JoinColumn(name = "to_user"),
            inverseJoinColumns = @JoinColumn(name = "from_user"))
    private Set<User> friendRequests;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.permitted = true;
    }

    public User(String username, String password, String email,
                String name, String birthTown, LocalDate birthDate) {
        this(username, password, email);
        this.name = name;
        this.birthDate = birthDate;
        this.birthTown = birthTown;
    }

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
                ", permitted=" + permitted +
                ", name='" + name + '\'' +
                ", birthTown='" + birthTown + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }

    public JSONObject toJson() {
        JSONObject userJson = new JSONObject();
        userJson.put("id", this.getId() == null ? "" : this.getId());
        userJson.put("username", this.getUsername() == null ? "" : this.getUsername());
        userJson.put("email", this.getEmail() == null ? "" : this.email);
        userJson.put("name", this.getName() == null ? "" : this.getName());
        userJson.put("birth_town", this.getBirthTown() == null ? "" : this.birthTown);
        userJson.put("birth_date", this.getBirthDate() == null ? "" :
                this.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        userJson.put("avatar", this.avatar == null ? "" : new String(avatar));
        userJson.put("study_place", this.getStudyPlace() == null ? "" : this.getStudyPlace());
        userJson.put("languages", this.getLanguages() == null ? "" : this.getLanguages());
        userJson.put("phone", this.getPhone() == null ? "" : this.getPhone());
        userJson.put("hobbies", this.getHobbies() == null ? "" : this.getHobbies());
        userJson.put("online", this.getOnline() == null ? "false" : this.getOnline());

        JSONObject rolesJson = new JSONObject();
        int roleIdx = 0;
        for (Role role : this.getRoles()) {
            rolesJson.put("role_" + ++roleIdx, role.getAuthority());
        }
        userJson.put("roles", rolesJson);

        JSONObject friendsJson = new JSONObject();
        int friendIdx = 0;
        for (User friend : this.getFriends()) {
            friendsJson.put("friend_" + ++friendIdx, friend.getId());
        }
        userJson.put("friends", friendsJson);

        JSONObject friendsRequestsJson = new JSONObject();
        int friendRequestIdx = 0;
        for (User friendRequest : this.getFriendRequests()) {
            friendsRequestsJson.put("friend_request_" + ++friendRequestIdx, friendRequest.getId());
        }
        userJson.put("friends_requests", friendsRequestsJson);

        return userJson;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return user.id.equals(this.id);
    }
}