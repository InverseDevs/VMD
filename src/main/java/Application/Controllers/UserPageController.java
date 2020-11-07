package Application.Controllers;

import Application.Entities.Content.WallPost;
import Application.Entities.User;
import Application.Security.JwtProvider;
import Application.Services.UserService;
import Application.Services.WallPostService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Controller
@Slf4j
public class UserPageController {
    @Autowired
    private WallPostService postService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/posts/{token}", method = RequestMethod.GET)
    @ResponseBody
    public String getPosts(@PathVariable("token") String token, HttpServletRequest request, HttpServletResponse response) {
        String jwt = request.getHeader("Authorization").substring(7);
        JSONObject result = new JSONObject();

        if (JwtProvider.validateToken(jwt)) {
            User user = userService.findUserByToken(token);
            Iterable<WallPost> posts = postService.allUserpagePosts(user.getId());

            int idx = 0;
            for (WallPost post : posts) {
                JSONObject postJson = new JSONObject();
                postJson.put("page_id", post.getPageId());
                postJson.put("id", post.getId());
                postJson.put("page_type", post.getPageType().toString());
                postJson.put("sender", post.getSender().getUsername());
                postJson.put("content", post.getContent());
                postJson.put("sent_time", post.getSentTime().toString());

                result.put("post_" + ++idx, postJson);
            }
        } else {
            result.put("status", "user not authorized");
        }

        System.out.println(result.toString());
        return result.toString();
    }

    @RequestMapping(value = "/posts/{token}", method = RequestMethod.POST)
    @ResponseBody
    public String addPost(@PathVariable("token") String token,
                            HttpServletRequest request, HttpServletResponse response) throws IOException {
        String jwt = request.getHeader("Authorization").substring(7);
        JSONObject result = new JSONObject();

        if (JwtProvider.validateToken(jwt)) {
            StringBuilder data = new StringBuilder();
            try {
                String line;
                while ((line = request.getReader().readLine()) != null) {
                    data.append(line);
                }
            } catch (IOException e) {
                throw new IOException("Error while parsing http request, " + this.getClass() + ", register");
            }
            JSONObject jsonObject = new JSONObject(data.toString());
            String sender = jsonObject.getString("sender");
            String content = jsonObject.getString("content");
            User user = (User) userService.loadUserByUsername(sender);

            System.out.println(user);

            postService.addPost(new WallPost(
                    user,
                    content,
                    new Date(),
                    userService.findUserByToken(token).getId(),
                    WallPost.PageType.USER));

            result.put("status", "post created");
        } else {
            result.put("status", "user not authorized");
        }

        return result.toString();
    }
}
