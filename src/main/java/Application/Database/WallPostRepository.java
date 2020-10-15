package Application.Database;

import Application.Content.WallPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class WallPostRepository {
    private final JdbcTemplate jdbc;

    private final String find_query = "select " +
            "wall_posts.id as id," +
            "wall_posts.page_type as page_type," +
            "wall_posts.page_id as page_id," +
            "wall_posts.sender as sender," +
            "wall_posts.message as message," +
            "wall_posts.sent_time as sent_time " +
            "from wall_posts ";

    @Autowired
    public WallPostRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Iterable<WallPost> findAll() {
        return jdbc.query(find_query, this::mapRowToWallPost);
    }

    public Iterable<WallPost> findByPage(Long pageId, WallPost.PageType pageType) {
        return jdbc.query(find_query + "where page_type = ? and page_id = ? ",
                this::mapRowToWallPost, pageType.toString(), pageId);
    }

    public WallPost findById(Long id) {
        List<WallPost> posts = jdbc.query(find_query + "where id = ?",
                this::mapRowToWallPost, id);
        return posts.size() == 0 ? null : posts.get(0);
    }

    public WallPost save(WallPost post) {
        jdbc.update("insert into wall_posts(page_type, page_id, sender, message, sent_time) " +
                "values (?,?,?,?,?)",
                post.getPageId(), post.getPageType().toString(), post.getPageId(),
                post.getSender(), post.getContent(), post.getSentTime());
        return post;
    }

    public WallPost deleteById(Long id) {
        WallPost post = this.findById(id);
        jdbc.update("delete from wall_posts where id = ?", id);
        return post;
    }

    private WallPost mapRowToWallPost(ResultSet rs, int rowNum) throws SQLException {
        WallPost post = new WallPost();
        post.setPageId(rs.getLong("page_id"));
        post.setPageType(WallPost.PageType.valueOf(rs.getString("page_type")));
        post.setContent(rs.getString("message"));
        post.setSender(rs.getString("sender"));
        post.setId(rs.getLong("id"));
        post.setSentTime(rs.getDate("sent_time"));
        return post;
    }
}
