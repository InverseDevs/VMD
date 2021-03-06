package Application.Entities;

import Application.Entities.Content.WallPost;
import Application.Entities.Wall.GroupWall;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "wall_id")
    private GroupWall wall;

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

    @ManyToMany
    @JoinTable(name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> members = new HashSet<>();

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] picture;

    public enum Access {
        EVERYONE, MEMBERS, ADMINISTRATORS, OWNER;
    }

    @Enumerated(EnumType.STRING)
    private Access commentAccess;
    @Enumerated(EnumType.STRING)
    private Access postAccess;

    public Group(String name, User owner, String namedLink) {
        this.name = name;
        this.owner = owner;
        this.namedLink = namedLink;
        this.members.add(owner);
        this.commentAccess = Access.EVERYONE;
        this.postAccess = Access.ADMINISTRATORS;
        this.wall = new GroupWall(this);
    }

    public boolean addAdministrator(User user) {
        if (owner.equals(user)) return false;
        return administrators.add(user);
    }

    public void removeAdministrator(User user) {
        administrators.remove(user);
    }

    public boolean banUser(User user) {
        if (user.equals(owner)) return false;
        administrators.remove(user);
        members.remove(user);
        bannedUsers.add(user);
        return true;
    }

    public void unbanUser(User user) {
        bannedUsers.remove(user);
    }

    public void addMember(User user) {
        members.add(user);
    }

    public void removeMember(User user) {
        administrators.remove(user);
        members.remove(user);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Group)) return false;
        Group g = (Group) o;
        return this.namedLink.equals(g.namedLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namedLink);
    }

    public JSONObject toJson() {
        JSONObject groupJson = new JSONObject();
        groupJson.put("id", this.getId() == null ? "" : this.getId());
        groupJson.put("name", this.getName() == null ? "" : this.getName());
        groupJson.put("named_link", this.getNamedLink() == null ? "" : this.getNamedLink());
        groupJson.put("owner_id", this.getOwner() == null ? "" : this.getOwner().getId());
        groupJson.put("picture", this.picture == null ? "" : new String(picture));

        if (this.getWall() != null && !this.getWall().getPosts().isEmpty()) {
            JSONArray postsArray = new JSONArray();

            Stream<WallPost> postsStream = this.getWall().getPosts().stream().sorted(
                    (post1, post2) -> post2.getSentTime().compareTo(post1.getSentTime()));
            postsStream.forEach(post -> postsArray.put(post.toJson()));

            groupJson.put("posts", postsArray);
        } else {
            groupJson.put("posts", "");
        }

        if (this.getAdministrators() != null && !this.getAdministrators().isEmpty()) {
            JSONArray adminsArray = new JSONArray();

            Stream<User> adminsStream = this.getAdministrators().stream().sorted(
                    (admin1, admin2) -> admin2.getName().compareTo(admin1.getName()));
            adminsStream.forEach(admin -> adminsArray.put(admin.toJson()));

            groupJson.put("admins", adminsArray);
        } else {
            groupJson.put("admins", "");
        }

        if (this.getBannedUsers() != null && !this.getBannedUsers().isEmpty()) {
            JSONArray bannedUsersArray = new JSONArray();

            Stream<User> bannedUsersStream = this.getBannedUsers().stream().sorted(
                    (bannedUser1, bannedUser2) -> bannedUser2.getName().compareTo(bannedUser1.getName()));
            bannedUsersStream.forEach(bannedUser -> bannedUsersArray.put(bannedUser.toJson()));

            groupJson.put("banned_users", bannedUsersArray);
        } else {
            groupJson.put("banned_users", "");
        }

        if (this.getMembers() != null && !this.getMembers().isEmpty()) {
            JSONArray membersArray = new JSONArray();

            Stream<User> membersStream = this.getMembers().stream().sorted(
                    (member1, member2) -> member2.getName().compareTo(member1.getName()));
            membersStream.forEach(member -> membersArray.put(member.toJson()));

            groupJson.put("members", membersArray);
        } else {
            groupJson.put("members", "");
        }

        return groupJson;
    }
}