package Application.Entities.Content;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
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
}
