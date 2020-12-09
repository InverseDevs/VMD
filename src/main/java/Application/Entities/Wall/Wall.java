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
@DiscriminatorColumn(name = "type", length = 16)
@Table(name = "walls")
public abstract class Wall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "wall")
    private List<WallPost> posts = new ArrayList<>();

    public abstract boolean canPost(User user);
    public abstract boolean canComment(User user);
    public abstract boolean canDeleteContent(User user);
}