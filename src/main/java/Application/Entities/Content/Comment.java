package Application.Entities.Content;

import Application.Entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    public Comment(User sender, String content, LocalDateTime sentTime, WallPost post, CommentType type) {
        super(sender, content, sentTime);
        this.post = post;
        this.type = type;
    }

    public JSONObject toJson() {
        JSONObject resultJson = new JSONObject();
        resultJson.put("id", this.getId());
        resultJson.put("sender", this.getSender() == null ? "" : this.getSender().getUsername());
        resultJson.put("content", this.getContent());
        resultJson.put("sent_time", this.getSentTime() == null ? "" : this.getSentTime().toString());
        resultJson.put("post_id", this.getPost() == null ? "" : this.getPost().getId());
        resultJson.put("reference_comment", this.comment == null ? "" : this.comment.getId());
        resultJson.put("type", this.getType().toString());

        if (!getLikes().isEmpty()) {
            JSONObject likesJson = new JSONObject();
            int userIdx = 0;
            for (User user : getLikes()) {
                likesJson.put("like_" + ++userIdx, user.toJson());
            }
            resultJson.put("likes", likesJson);
        } else {
            resultJson.put("likes", "");
        }

        if (!getComments().isEmpty()) {
            JSONObject commentsJson = new JSONObject();
            int commentIdx = 0;
            for (Comment comment : getComments()) {
                commentsJson.put("comment_" + ++commentIdx, comment.toJson());
            }
            resultJson.put("comments", commentsJson);
        } else {
            resultJson.put("comments", "");
        }

        return resultJson;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + getId() +
                "sender=" + getSender() +
                "content=" + getContent() +
                "post=" + post +
                ", comment=" + comment +
                ", type=" + type +
                '}';
    }
}
