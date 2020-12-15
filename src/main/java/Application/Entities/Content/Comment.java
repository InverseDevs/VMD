package Application.Entities.Content;

import Application.Entities.User;
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
@Table(name = "comments")
public class Comment extends Content {
    @ManyToOne
    @JoinColumn(name = "post_id")
    private WallPost post;

    @ManyToOne
    @JoinColumn(name = "reference_comment")
    private Comment referenceComment;

    @ManyToMany
    @JoinTable(name = "likes_comments",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> likes;

    @OneToMany(mappedBy = "referenceComment", cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] picture;

    public Set<Comment> getComments() {
        Set<Comment> comments = new HashSet<>();

        for (Comment comment : this.comments) {
            if (comment.getReferenceComment() != null) {
                comments.add(comment);
            }
        }

        return comments;
    }

    public Comment(User sender, String content, LocalDateTime sentTime, WallPost post) {
        super(sender, content, sentTime);
        this.post = post;
    }

    public Comment(User sender, String content, LocalDateTime sentTime, WallPost post, Comment referenceComment, byte[] picture) {
        super(sender, content, sentTime);
        this.post = post;
        this.referenceComment = referenceComment;
        this.picture = picture;
    }

    public JSONObject toJson() {
        JSONObject resultJson = new JSONObject();
        resultJson.put("id", this.getId() == null ? "" : this.getId());
        resultJson.put("sender", this.getSender() == null ? "" : this.getSender().toJson());
        resultJson.put("content", this.getContent() == null ? "" : this.getContent());
        resultJson.put("sent_time", this.getSentTime() == null ? "" :
                this.getSentTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy:hh.mm.ss")));
        resultJson.put("post_id", this.getPost() == null ? "" : this.getPost().getId());
        resultJson.put("reference_comment", this.getReferenceComment() == null ? "" : this.getReferenceComment().getId());
        resultJson.put("picture", this.getPicture() == null ? "" : new String(this.getPicture()));

        if (!getLikes().isEmpty()) {
            JSONArray likesArray = new JSONArray();

            for (User user : getLikes()) {
                likesArray.put(user.toJson());
            }
            resultJson.put("likes", likesArray);
        } else {
            resultJson.put("likes", "");
        }

        if (!getComments().isEmpty()) {
            JSONArray commentsArray = new JSONArray();

            Stream<Comment> commentStream = this.getComments().stream().sorted(
                    Comparator.comparing(Content::getId));
            commentStream.forEach(comment -> commentsArray.put(comment.toJson()));

            resultJson.put("comments", commentsArray);
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
                " comment = " + getReferenceComment();
    }
}
