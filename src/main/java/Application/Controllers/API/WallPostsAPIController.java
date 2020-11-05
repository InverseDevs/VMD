package Application.Controllers.API;

import Application.Database.WallPostRepository;
import Application.Entities.Content.WallPost;
import Application.Starter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Starter.apiLink + WallPostsAPIController.apiWallPosts)
public class WallPostsAPIController {
    public final static String apiWallPosts = "/posts";

    @Autowired
    private WallPostRepository postRepository;

    @NoArgsConstructor
    @Getter
    private static class WallPostWrapper {
        private Long id;
        private String sender;
        private String message;
        private long sentTime;

        private Long pageId;
        private String pageType;

        public WallPostWrapper(WallPost post) {
            this.id = post.getId();
            this.sender = post.getSender();
            this.message = post.getContent();
            this.sentTime = post.getSentTime() != null ? post.getSentTime().getTime() : 0;

            this.pageId = post.getPageId();
            this.pageType = post.getPageType().toString();
        }

        public WallPost toWallPost() {
            return new WallPost(this.id, this.sender, this.message, new Date(this.sentTime),
                                this.pageId, WallPost.PageType.valueOf(pageType));
        }

        @Override
        public String toString() {
            return "WallPostWrapper{" +
                    "id=" + this.id +
                    "sender='" + this.sender + "'" +
                    "message='" + this.message + "'" +
                    "sentTime=" + this.sentTime + "'" +
                    "pageId=" + this.pageId + "'" +
                    "pageType='" + this.pageType + "'" +
                    "}";
        }
    }

    @GetMapping
    public List<WallPostWrapper> all() {
        return postRepository.findAll().stream().map(WallPostWrapper::new).collect(Collectors.toList());
    }
}