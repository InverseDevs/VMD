package Application.Services;

import Application.Database.Wall.UserWallRepository;
import Application.Database.Wall.WallRepository;
import Application.Database.WallPostRepository;
import Application.Entities.Content.WallPost;
import Application.Entities.User;
import Application.Entities.Wall.UserWall;
import Application.Entities.Wall.Wall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class WallService {
    @Autowired
    private UserWallRepository userWallRepository;
    @Autowired
    private WallPostRepository postRepository;
    @Autowired
    private WallRepository wallRepository;

    /**
     * Возвращает список всех постов на странице заданного пользователя.
     * @param user объект, соответствующий заданному пользователю.
     * @return список постов на странице пользователя.
     */
    public List<WallPost> findAllUserPagePosts(User user) {
        return new ArrayList<>(userWallRepository.findByUser(user).getPosts());
    }

    /**
     * Возвращает список всех постов, отправленных заданным пользователем.
     * @param sender объект, соответствующий заданному пользователю.
     * @return список постов, отправленных пользователем.
     */
    public List<WallPost> findAllBySender(User sender) {
        return new ArrayList<>(postRepository.findBySender(sender));
    }

    /**
     * Возвращает пост по его числовому идентификатору.
     * В случае, если поста с заданным идентификатором не существует, возвращает null.
     * @param id числовой идентификатор.
     * @return пост с данным идентификатором или null, если таковой отсутствует.
     */
    public WallPost findPostById(long id) {
        return postRepository.findById(id).orElse(null);
    }

    /**
     * Удаляет пост по его числовому идентификатору.
     * Если в базе данных поста с заданным идентификатор не существует, ничего не делает.
     * @param id числовой идентификатор.
     */
    public void deletePostById(long id) {
        postRepository.deleteById(id);
    }

    /**
     * Находит в базе данных объект, соответствующий абстрактной стене постов, и возвращает его.
     * В случае, если стена не представлена в БД (то есть у нее нет идентификатора), возвращает null.
     * @param wall устаревший объект, соответствующий стене постов.
     * @return обновленный объект.
     * @see WallService#refresh(UserWall) 
     */
    public Wall refresh(Wall wall) {
        if(wall.getId() == null) return null;
        return wallRepository.findById(wall.getId()).get();
    }

    /**
     * Находит в базе данных объект, соответствующий стене постов пользователя, и возвращает его.
     * В случае, если стена не представлена в БД (то есть у нее нет идентификатора), возвращает null.
     * @param wall устаревший объект, соответствующий стене постов.
     * @return обновленный объект.
     * @see WallService#refresh(Wall)
     */
    public UserWall refresh(UserWall wall) {
        if(wall.getId() == null) return null;
        return userWallRepository.findById(wall.getId()).get();
    }

    /**
     * Обновляет стену, находящуюся в БД, в соответствии с заданным параметром.
     * В случае, если стена не представлена в БД (то есть у нее нет идентификатора), возвращает null.
     * @param wall обновленный объект, соответствующий стене постов.
     * @return обновленный объект.
     * @see WallService#update(UserWall)
     */
    public Wall update(Wall wall) {
        if(wall.getId() == null) return null;
        return wallRepository.save(wall);
    }

    /**
     * Обновляет стену пользователя, находящуюся в БД, в соответствии с заданным параметром.
     * В случае, если стена не представлена в БД (то есть у нее нет идентификатора), возвращает null.
     * @param wall обновленный объект, соответствующий стене постов.
     * @return обновленный объект.
     * @see WallService#update(Wall)
     */
    public UserWall update(UserWall wall) {
        if(wall.getId() == null) return null;
        return userWallRepository.save(wall);
    }

    /**
     * "Частный случай" метода {@link WallService#addPost(User, String, Wall)}.
     * Добавляет пост на стену пользователя с заданным сообщением и отправителем.
     * В случае, если отправитель не имеет права отправить пост на стену пользователя,
     * возвращает null. В противном случае возвращает объект, соответствующий отправленному
     * посту.
     * @param sender отправитель.
     * @param message сообщение поста.
     * @param pageOwner пользователь, на чью стену необходимо отправить пост.
     * @return объект, соответствующий отправленному посту.
     * @see WallService#addPost(User, String, Wall)
     * @see Wall#canPost(User)
     */
    public WallPost addPost(User sender, String message, User pageOwner) {
        return addPost(sender, message, userWallRepository.findByUser(pageOwner));
    }

    /**
     * Добавляет пост с заданным сообщением и отправителем на заданную стену.
     * В случае, если стены не существует (т.е. у нее нулевой идентификатор) или
     * если отправитель не имеет права отправить пост на заданную стену,
     * возвращает null. В противном случае возвращает объект, соответствующий
     * отправленному посту.
     * @param sender
     * @param message
     * @param wall
     * @return
     */
    public WallPost addPost(User sender, String message, Wall wall) {
        if(wall.getId() == null) return null;
        if(!wall.canPost(sender)) return null;
        WallPost post = new WallPost(sender, message, new Date(), wall);
        wall.getPosts().add(post);
        wallRepository.save(wall);
        return postRepository.save(post);
    }

    public void setUserWallPostAccess(User user, Wall.AccessType postAccess) {
        user.getWall().setPostAccess(postAccess);
        wallRepository.save(user.getWall());
    }
}