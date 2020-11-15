package Application.Entities.Content;

import Application.Entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// Если ты когда-нибудь решишь, что написал костыль, просто посмотри на этот класс

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comments")
public class Comment extends Content {
    @ManyToOne
    @JoinColumn(name = "post_id")
    private WallPost post;

    @ManyToOne
    @JoinColumn(name = "reference_comment")
    private Comment comment;

    @ManyToMany
    @JoinTable(name = "likes_comments",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likes;

    private CommentType type;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    // Близнец-костыль, но не слишком серьёзный. По крайней, мере структуру проекта он не ломает :)
    public Set<Comment> getComments() {
        Set<Comment> comments = new HashSet<>();

        for (Comment comment : this.comments) {
            if (comment.getType() == CommentType.COMMENT) {
                comments.add(comment);
            }
        }

        return comments;
    }

    public enum CommentType {
        POST, COMMENT;

        @Converter(autoApply = true)
        public static class PageTypeConverter implements AttributeConverter<CommentType, String> {
            @Override
            public String convertToDatabaseColumn(CommentType commentType) {
                return commentType == null ? null : commentType.toString();
            }

            @Override
            public CommentType convertToEntityAttribute(String s) {
                return s == null ? null : CommentType.valueOf(s);
            }
        }
    }

    public Comment(User sender, String content, Date sentTime, WallPost post, CommentType type) {
        super(sender, content, sentTime);
        this.post = post;
        this.type = type;
    }

    public JSONObject toJson() {
        JSONObject commentJson = new JSONObject();
        commentJson.put("id", this.getId());
        commentJson.put("sender", this.getSender() == null ? "" : this.getSender().getUsername());
        commentJson.put("content", this.getContent());
        commentJson.put("sent_time", this.getSentTime() == null ? "" : this.getSentTime().toString());
        commentJson.put("post_id", this.getPost() == null ? "" : this.getPost().getId());
        commentJson.put("reference_comment", this.comment == null ? "" : this.comment.getId());
        commentJson.put("type", this.getType().toString());

        JSONObject likesJson = new JSONObject();
        int userIdx = 0;
        for (User user : likes) {
            likesJson.put("like_" + ++userIdx, user.toJson());
        }
        commentJson.put("likes", likesJson);

        return commentJson;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "post=" + post +
                ", type=" + type +
                '}';
    }
}
