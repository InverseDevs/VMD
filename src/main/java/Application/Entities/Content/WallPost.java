package Application.Entities.Content;

import Application.Entities.User;
import Application.Entities.Wall.Wall;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "wall_posts")
public class WallPost extends Content {
    @ManyToOne
    @JoinColumn(name = "wall_id")
    private Wall wall;

    public WallPost(Long id, User sender, String content, Date sentTime, Wall wall) {
        super(id, sender, content, sentTime);
        this.wall = wall;
    }

    public WallPost(User sender, String content, Date sentTime, Wall wall) {
        super(sender, content, sentTime);
        this.wall = wall;
    }

    public JSONObject toJson() {
        JSONObject post = new JSONObject();
        //post.put("page_id", this.getPageId());
        post.put("id", this.getId());
        //post.put("page_type", this.getPageType().toString());
        post.put("sender", this.getSender().getUsername());
        post.put("content", this.getContent());
        post.put("sent_time", this.getSentTime().toString());

        return post;
    }
}
