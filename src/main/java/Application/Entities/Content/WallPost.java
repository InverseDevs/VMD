package Application.Entities.Content;

import Application.Entities.User;
import Application.Entities.Wall.GroupWall;
import Application.Entities.Wall.UserWall;
import Application.Entities.Wall.Wall;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "wall_posts")
public class WallPost extends Content {
    @ManyToOne
    @JoinColumn(name = "wall_id")
    private Wall wall;

    @ManyToMany
    @JoinTable(name = "likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] picture;

    public Set<Comment> getComments() {
        Set<Comment> comments = new HashSet<>();

        for (Comment comment : this.comments) {
            if (comment.getReferenceComment() == null) {
                comments.add(comment);
            }
        }

        return comments;
    }

    public enum PageType {
        USER, GROUP;
    }

    public Long getPageId() {
        if (wall instanceof UserWall) return ((UserWall) wall).getUser().getId();
        else if (wall instanceof GroupWall) return ((GroupWall) wall).getGroup().getId();
        else return null;
    }

    public PageType getPageType() {
        if (wall instanceof UserWall) return PageType.USER;
        else if (wall instanceof GroupWall) return PageType.GROUP;
        else return null;
    }

    // Задавать ИД сущностей напрямую не рекомендуется, если Hibernate задает их сам
    @Deprecated
    public WallPost(Long id, User sender, String content, LocalDateTime sentTime, Wall wall) {
        super(id, sender, content, sentTime);
        this.wall = wall;
    }

    public WallPost(User sender, String content, LocalDateTime sentTime, Wall wall) {
        this(sender, content, sentTime, wall, null);
    }

    public WallPost(User sender, String content, LocalDateTime sentTime, Wall wall, byte[] picture) {
        super(sender, content, sentTime);
        this.wall = wall;
        this.picture = picture;
    }

    public JSONObject toJson() {
        JSONObject post = new JSONObject();
        post.put("id", this.getId() == null ? "" : this.getId());
        post.put("page_id", this.getPageId() == null ? "" : this.getPageId());
        post.put("page_type", this.getPageType() == null ? "" : this.getPageType().toString());
        post.put("sender", this.getSender() == null ? "" : this.getSender().toJson());
        post.put("content", this.getContent() == null ? "" : this.getContent());
        post.put("sent_time", this.getSentTime() == null ? "" :
                this.getSentTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy:hh.mm.ss")));
        post.put("picture", this.getPicture() == null ? "" : new String(this.getPicture()));

        if (!getLikes().isEmpty()) {
            JSONArray likesArray = new JSONArray();

            for (User user : getLikes()) {
                likesArray.put(user.toJson());
            }

            post.put("likes", likesArray);
        } else {
            post.put("likes", "");
        }

        if (!getComments().isEmpty()) {
            JSONArray commentsArray = new JSONArray();

            Stream<Comment> friendStream = this.getComments().stream().sorted(
                    Comparator.comparing(Content::getId));
            friendStream.forEach(comment -> commentsArray.put(comment.toJson()));

            post.put("comments", commentsArray);
        } else {
            post.put("comments", "");
        }

        return post;
    }
}
