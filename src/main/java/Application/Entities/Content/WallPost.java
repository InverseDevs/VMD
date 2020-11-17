package Application.Entities.Content;

import Application.Entities.User;
import Application.Entities.Wall.Wall;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
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
    private Set<User> likes; //user id's

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    // Близнец-костыль, но не слишком серьёзный. По крайней, мере структуру проекта он не ломает :)
    public Set<Comment> getComments() {
        Set<Comment> comments = new HashSet<>();

        for (Comment comment : this.comments) {
            if (comment.getType() == Comment.CommentType.POST) {
                comments.add(comment);
            }
        }

        return comments;
    }

    @Deprecated
    public enum PageType {
        USER, GROUP;

        @Converter(autoApply = true)
        public static class PageTypeConverter implements AttributeConverter<PageType, String> {
            @Override
            public String convertToDatabaseColumn(PageType pageType) {
                if (pageType == null) return null;
                return pageType.toString();
            }

            @Override
            public PageType convertToEntityAttribute(String s) {
                if (s == null) return null;
                return PageType.valueOf(s);
            }
        }
    }

    // Задавать ИД сущностей напрямую не рекомендуется, если Hibernate задает их сам
    @Deprecated
    public WallPost(Long id, User sender, String content, LocalDateTime sentTime, Wall wall) {
        super(id, sender, content, sentTime);
        this.wall = wall;
    }

    public WallPost(User sender, String content, LocalDateTime sentTime, Wall wall) {
        super(sender, content, sentTime);
        this.wall = wall;
    }

    // TODO исправить
    public JSONObject toJson() {
        JSONObject post = new JSONObject();
        //post.put("page_id", this.getPageId());
        post.put("id", this.getId());
        //post.put("page_type", this.getPageType() == null ? "" : this.getPageType().toString());
        post.put("sender", this.getSender() == null ? "" : this.getSender().getUsername());
        post.put("content", this.getContent());
        post.put("sent_time", this.getSentTime() == null ? "" : this.getSentTime().toString());

        if (!getLikes().isEmpty()) {
            JSONObject likesJson = new JSONObject();
            int userIdx = 0;
            for (User user : getLikes()) {
                likesJson.put("like_" + ++userIdx, user.toJson());
            }
            post.put("likes", likesJson);
        } else {
            post.put("likes", "");
        }

        if (!getComments().isEmpty()) {
            JSONObject commentsJson = new JSONObject();

            AtomicInteger commentIdx = new AtomicInteger();
            Stream<Comment> commentStream = this.getComments().stream().sorted(
                    (comment, t1) -> t1.getSentTime().compareTo(comment.getSentTime()));
            commentStream.forEach(comment -> {
                commentsJson.put("comment_" + commentIdx.incrementAndGet(), comment.toJson());
            });
            post.put("comments", commentsJson);
        } else {
            post.put("comments", "");
        }

        return post;
    }
}
