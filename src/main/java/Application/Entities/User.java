package Application.Entities;

import Application.Entities.Wall.UserWall;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Slf4j

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
    private LocalDateTime lastOnline;

    @OneToOne
    @JoinColumn(name = "wall_id")
    @Setter(AccessLevel.PRIVATE)
    private UserWall wall;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] avatar;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] round;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_to_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "friends",
            joinColumns = @JoinColumn(name = "user1_id"),
            inverseJoinColumns = @JoinColumn(name = "user2_id"))
    private Set<User> friends;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "friendRequests",
            joinColumns = @JoinColumn(name = "to_user"),
            inverseJoinColumns = @JoinColumn(name = "from_user"))
    private Set<User> friendRequests;

    public enum Access {
        EVERYONE, FRIENDS, NOBODY;
    }

    @Enumerated(EnumType.STRING)
    private Access messageAccess;
    @Enumerated(EnumType.STRING)
    private Access postAccess;
    @Enumerated(EnumType.STRING)
    private Access commentAccess;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.permitted = false;
        this.wall = new UserWall(this);
        this.messageAccess = Access.EVERYONE;
        this.postAccess = Access.EVERYONE;
        this.commentAccess = Access.EVERYONE;
    }

    public User(String username, String password, String email,
                String name, String birthTown, LocalDate birthDate) {
        this(username, password, email);
        this.name = name;
        this.birthDate = birthDate;
        this.birthTown = birthTown;
        this.wall = new UserWall(this);
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
        userJson.put("email", this.getEmail() == null ? "" : this.getEmail());
        userJson.put("name", this.getName() == null ? "" : this.getName());
        userJson.put("birth_town", this.getBirthTown() == null ? "" : this.getBirthTown());
        userJson.put("birth_date", this.getBirthDate() == null ? "" :
                this.getBirthDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        userJson.put("avatar", this.getAvatar() == null ? "" : new String(this.getAvatar()));
        userJson.put("study_place", this.getStudyPlace() == null ? "" : this.getStudyPlace());
        userJson.put("languages", this.getLanguages() == null ? "" : this.getLanguages());
        userJson.put("phone", this.getPhone() == null ? "" : this.getPhone());
        userJson.put("hobbies", this.getHobbies() == null ? "" : this.getHobbies());
        userJson.put("last_online", this.getLastOnline() == null ? "" :
                this.getLastOnline().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        userJson.put("online", this.getLastOnline() != null &&
                Duration.between(this.getLastOnline(), LocalDateTime.now()).getSeconds() < 120L);

        if (!this.getRoles().isEmpty()) {
            JSONArray rolesArray = new JSONArray();

            for (Role role : this.getRoles()) {
                rolesArray.put(role.getAuthority());
            }

            userJson.put("roles", rolesArray);
        } else {
            userJson.put("roles", "");
        }

        if (!getFriends().isEmpty()) {
            JSONArray friendsArray = new JSONArray();

            Stream<User> friendStream = this.getFriends().stream().sorted(
                    (friend1, friend2) -> friend2.getName().compareTo(friend1.getName()));
            friendStream.forEach(friend -> friendsArray.put(friend.getId()));

            userJson.put("friends", friendsArray);
        } else {
            userJson.put("friends", "");
        }

        if (!friendRequests.isEmpty()) {
            JSONArray friendsRequestsArray = new JSONArray();

            for (User friendRequest : this.getFriendRequests()) {
                friendsRequestsArray.put(friendRequest.getId());
            }

            userJson.put("friends_requests", friendsRequestsArray);
        } else {
            userJson.put("friends_requests", "");
        }

        return userJson;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return user.username.equals(this.username) && user.email.equals(this.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
}