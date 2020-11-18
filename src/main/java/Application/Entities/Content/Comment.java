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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

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
    private Comment reference_comment;

    @ManyToMany
    @JoinTable(name = "likes_comments",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likes;

    @OneToMany(mappedBy = "reference_comment", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    public Set<Comment> getComments() {
        Set<Comment> comments = new HashSet<>();

        for (Comment comment : this.comments) {
            if (comment.getReference_comment() != null) {
                comments.add(comment);
            }
        }

        return comments;
    }

    public Comment(User sender, String content, LocalDateTime sentTime, WallPost post) {
        super(sender, content, sentTime);
        this.post = post;
    }

    public JSONObject toJson() {
        JSONObject resultJson = new JSONObject();
        resultJson.put("id", this.getId());
        resultJson.put("sender", this.getSender() == null ? "" : this.getSender().getUsername());
        resultJson.put("content", this.getContent());
        resultJson.put("sent_time", this.getSentTime() == null ? "" : this.getSentTime().toString());
        resultJson.put("post_id", this.getPost() == null ? "" : this.getPost().getId());
        resultJson.put("reference_comment", this.reference_comment == null ? "" : this.reference_comment.getId());

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
            AtomicInteger commentIdx = new AtomicInteger();
            Stream<Comment> commentStream = this.getComments().stream().sorted(
                    (comment1, comment2) -> comment2.getSentTime().compareTo(comment1.getSentTime()));
            commentStream.forEach(comment -> {
                commentsJson.put("comment_" + commentIdx.incrementAndGet(), comment.toJson());
            });
            resultJson.put("comments", commentsJson);
        } else {
            resultJson.put("comments", "");
        }

        return resultJson;
    }

    @Override
    public String toString() {
        return "Comment: " +
                "id = " + getId() +
                " sender = " + getSender() +
                " content = " + getContent() +
                " post = " + getPost() +
                " comment = " + getReference_comment();
    }
}
