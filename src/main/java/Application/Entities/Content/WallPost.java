package Application.Entities.Content;

import Application.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    private Set<User> likes; //user id's

    public enum PageType {
        USER, GROUP;

        @Converter(autoApply = true)
        public static class PageTypeConverter implements AttributeConverter<PageType, String> {
            @Override
            public String convertToDatabaseColumn(PageType pageType) {
                if(pageType == null) return null;
                return pageType.toString();
            }

            @Override
            public PageType convertToEntityAttribute(String s) {
                if(s == null) return null;
                return PageType.valueOf(s);
            }
        }
    }

    public WallPost(Long id, User sender, String content, Date sentTime, Long pageId, PageType pageType) {
        super(id, sender, content, sentTime);
        this.pageId = pageId;
        this.pageType = pageType;
    }

    public WallPost(User sender, String content, Date sentTime, Long pageId, PageType pageType) {
        super(sender, content, sentTime);
        this.pageId = pageId;
        this.pageType = pageType;
    }

    public JSONObject toJson() {
        JSONObject post = new JSONObject();
        post.put("page_id", this.getPageId());
        post.put("id", this.getId());
        post.put("page_type", this.getPageType().toString());
        post.put("sender", this.getSender().getUsername());
        post.put("content", this.getContent());
        post.put("sent_time", this.getSentTime().toString());

        Set<Long> likedUserId = new HashSet<>();
        for (User user : likes) {
            likedUserId.add(user.getId());
        }
        post.put("likes", likedUserId.toString());

        return post;
    }
}
