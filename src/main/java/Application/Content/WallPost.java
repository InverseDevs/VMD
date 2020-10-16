package Application.Content;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WallPost extends Content {
    private Long pageId;
    private PageType pageType;

    public enum PageType {
        USER, GROUP;
    }
}
