package Application.Entities.Wall;

import Application.Entities.Content.WallPost;
import Application.Entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor
@DiscriminatorColumn(name = "type", length = 16)
@Table(name = "walls")
public abstract class Wall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public enum AccessType {
        EVERYONE, FRIENDS, MEMBERS, ADMINISTRATORS
    }

    @Enumerated(EnumType.STRING)
    @Setter
    private AccessType postAccess;
    @Enumerated(EnumType.STRING)
    @Setter
    private AccessType commentAccess;

    @OneToMany(mappedBy = "wall")
    private List<WallPost> posts = new ArrayList<>();

    public Wall(AccessType postAccess, AccessType commentAccess) {
        this.postAccess = postAccess;
        this.commentAccess = commentAccess;
    }

    public abstract boolean canPost(User user);
    public abstract boolean canComment(User user);
}