package Application.Entities.Content;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "wall_posts")
public class WallPost extends Content {
    private Long pageId;
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

    public WallPost(Long id, String sender, String content, Date sentTime, Long pageId, PageType pageType) {
        super(id, sender, content, sentTime);
        this.pageId = pageId;
        this.pageType = pageType;
    }

    public WallPost(String sender, String content, Date sentTime, Long pageId, PageType pageType) {
        super(sender, content, sentTime);
        this.pageId = pageId;
        this.pageType = pageType;
    }
}
