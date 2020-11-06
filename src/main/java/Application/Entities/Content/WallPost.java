package Application.Entities.Content;

import Application.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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
}
