package Application.Entities.Content;

import Application.Entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
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
    @Column(name = "page_id")
    private Long pageId;
    @Column(name = "page_type")
    private PageType pageType;

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
            if (comment.getReference_comment() == null) {
                comments.add(comment);
            }
        }

        return comments;
    }

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

    public WallPost(Long id, User sender, String content, LocalDateTime sentTime, Long pageId, PageType pageType) {
        super(id, sender, content, sentTime);
        this.pageId = pageId;
        this.pageType = pageType;
    }

    public WallPost(User sender, String content, LocalDateTime sentTime, Long pageId, PageType pageType) {
        super(sender, content, sentTime);
        this.pageId = pageId;
        this.pageType = pageType;
    }

    public JSONObject toJson() {
        JSONObject post = new JSONObject();
        post.put("id", this.getId());
        post.put("page_id", this.getPageId());
        post.put("page_type", this.getPageType() == null ? "" : this.getPageType().toString());
        post.put("sender", this.getSender() == null ? "" : this.getSender().getUsername());
        post.put("content", this.getContent());
        post.put("sent_time", this.getSentTime() == null ? "" : this.getSentTime().toString());
        post.put("picture", this.getPicture() == null ? "" : this.getPicture());

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
                    (comment1, comment2) -> comment2.getSentTime().compareTo(comment1.getSentTime()));
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
