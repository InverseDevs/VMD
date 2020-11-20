package Application.Entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String name;
    private String namedLink;
    @Setter
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @ManyToMany
    @JoinTable(name = "group_admins",
               joinColumns = @JoinColumn(name = "group_id"),
               inverseJoinColumns = @JoinColumn(name = "admin_id"))
    private Set<User> administrators = new HashSet<>();
    @ManyToMany
    @JoinTable(name = "group_bans",
               joinColumns = @JoinColumn(name = "group_id"),
               inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> bannedUsers = new HashSet<>();

    public Group(String name, User owner, String namedLink) {
        this.name = name;
        this.owner = owner;
        this.namedLink = namedLink;
    }

    public void addAdministrator(User user) {
        administrators.add(user);
    }

    public void removeAdministrator(User user) {
        administrators.remove(user);
    }

    public boolean banUser(User user) {
        if(user.equals(owner)) return false;
        administrators.remove(user);
        bannedUsers.add(user);
        return true;
    }

    public void unbanUser(User user) {
        bannedUsers.remove(user);
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Group)) return false;
        Group g = (Group) o;
        return this.namedLink.equals(g.namedLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namedLink);
    }
}