package Application.Controllers.API;

import Application.Controllers.API.Exceptions.WallPostNotFoundException;
import Application.Controllers.API.Exceptions.WrongRequestException;
import Application.Database.WallPostRepository;
import Application.Entities.Content.WallPost;
import Application.Starter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
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

        private String _href;

        public WallPostWrapper(WallPost post) {
            this.id = post.getId();
            this.sender = post.getSender();
            this.message = post.getContent();
            this.sentTime = post.getSentTime() != null ? post.getSentTime().getTime() : 0;

            this.pageId = post.getPageId();
            this.pageType = post.getPageType().toString();

            this._href = Starter.homeLink + Starter.apiLink + apiWallPosts + "/" + id;
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
                    "_href='" + this._href + "'" +
                    "}";
        }
    }

    @GetMapping
    public List<WallPostWrapper> all() {
        return postRepository.findAll().stream().map(WallPostWrapper::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public WallPostWrapper one(@PathVariable long id) {
        return new WallPostWrapper(postRepository.findById(id).orElseThrow(WallPostNotFoundException::new));
    }

    @GetMapping("page/{pageId}")
    public List<WallPostWrapper> allPostsByPageId(@PathVariable long pageId,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
                                                  @RequestParam(name = "page", defaultValue = "0") int currPage) {
        return postRepository.findByPageId(pageId, PageRequest.of(currPage, pageSize))
                .stream().map(WallPostWrapper::new).collect(Collectors.toList());
    }

    @PostMapping
    public WallPostWrapper addPost(@RequestBody WallPostWrapper wrapper) {
        if(wrapper.sender == null || wrapper.sentTime < 0
                || wrapper.pageId == null || wrapper.message == null)
            throw new WrongRequestException();
        WallPost post = new WallPost(null, wrapper.sender, wrapper.message, new Date(),
                wrapper.pageId, WallPost.PageType.USER);
        return new WallPostWrapper(postRepository.save(post));
    }
}