package Application.Services;

import Application.Entities.Wall.GroupWall;
import Application.Entities.Wall.UserWall;
import Application.Exceptions.NotEnoughPermissionsException;
import Application.Exceptions.User.NoUserFoundException;
import Application.Exceptions.WallPost.NoWallFoundException;
import Application.Exceptions.WallPost.WallNoPostAccessException;
import Application.Exceptions.WallPost.WallPostNotFoundException;
import Application.Database.User.UserRepository;
import Application.Database.Wall.GroupWallRepository;
import Application.Database.Wall.UserWallRepository;
import Application.Database.Wall.WallRepository;
import Application.Database.WallPostRepository;
import Application.Entities.Content.WallPost;
import Application.Entities.Group;
import Application.Entities.User;
import Application.Entities.Wall.Wall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class WallService {
    @Autowired
    private GroupWallRepository groupWallRepository;
    @Autowired
    private UserWallRepository userWallRepository;
    @Autowired
    private WallPostRepository postRepository;
    @Autowired
    private WallRepository wallRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Возвращает список всех постов на странице заданного пользователя.
     * @param user объект, соответствующий заданному пользователю.
     * @return список постов на странице пользователя.
     */
    public Set<WallPost> findAllUserPagePosts(User user) {
        return new HashSet<>(userWallRepository.findByUser(user).getPosts());
    }

    /**
     * Возвращает список всех постов на странице заданной группы.
     * @param group объект, соответствующий заданной группы.
     * @return список постов на странице группы.
     */
    public Set<WallPost> findAllGroupPosts(Group group) {
        return new HashSet<>(groupWallRepository.findByGroup(group).getPosts());
    }

    /**
     * Возвращает множество всех постов, отправленных заданным пользователем.
     * @param sender объект, соответствующий заданному пользователю.
     * @return множество постов, отправленных пользователем.
     */
    public Set<WallPost> findAllBySender(User sender) {
        return new HashSet<>(postRepository.findAllBySender(sender));
    }

    /**
     * Возвращает пост по его числовому идентификатору.
     * В случае, если поста с заданным идентификатором не существует, генерирует исключение.
     * @param id числовой идентификатор.
     * @return пост с данным идентификатором.
     */
    public WallPost findPostById(long id) {
        return postRepository.findById(id).orElseThrow(WallPostNotFoundException::new);
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
     * Удаляет пост по его числовому идентификатору, если пользователь, пытающийся сделать это,
     * имеет соответствующие права.
     * @param id числовой идентификатор поста.
     * @param attempter пользователь, пытающийся удалить пост.
     * @throws NotEnoughPermissionsException пользователь не имеет соответствующие права.
     */
    public void deletePostByIdByUser(long id, User attempter) throws NotEnoughPermissionsException {
        Optional<WallPost> postOptional = postRepository.findById(id);
        if(!postOptional.isPresent()) return;
        WallPost post = postOptional.get();
        if(!post.getSender().equals(attempter)) {
            if(post.getPageType() == WallPost.PageType.USER) {
                User user = ((UserWall) post.getWall()).getUser();
                if(!user.equals(attempter))
                    throw new NotEnoughPermissionsException();
            } else if(post.getPageType() == WallPost.PageType.GROUP) {
                Group group = ((GroupWall) post.getWall()).getGroup();
                if(!group.getAdministrators().contains(attempter) && !group.getOwner().equals(attempter))
                    throw new NotEnoughPermissionsException();
            } else {
                throw new RuntimeException();
            }
        }
        postRepository.deleteById(id);
    }

    /**
     * "Частный случай" метода {@link WallService#addPost(User, String, byte[], Wall)}.
     * Добавляет пост на стену пользователя с заданным сообщением и отправителем.
     * В случае, если отправитель не имеет права отправить пост на стену пользователя,
     * генерирует соответствующие исключения. В противном случае возвращает объект, соответствующий отправленному
     * посту.
     * @param sender отправитель.
     * @param message сообщение поста.
     * @param pageOwner пользователь, на чью стену необходимо отправить пост.
     * @return объект, соответствующий отправленному посту.
     * @see WallService#addPost(User, String, byte[], Wall)
     * @see Wall#canPost(User)
     */
    public WallPost addPost(User sender, String message, User pageOwner) {
        return addPost(sender, message, null, userWallRepository.findByUser(pageOwner));
    }

    /**
     * "Частный случай" метода {@link WallService#addPost(User, String, byte[], Wall)}.
     * Добавляет пост на стену пользователя с заданным сообщением, отправителем и прикрепленным изображением.
     * В случае, если отправитель не имеет права отправить пост на стену пользователя,
     * генерирует соответствующие исключения. В противном случае возвращает объект, соответствующий отправленному
     * посту.
     * @param sender отправитель.
     * @param message сообщение поста.
     * @param pageOwner пользователь, на чью стену необходимо отправить пост.
     * @param picture изображение в виде байтового массива.
     * @return объект, соответствующий отправленному посту.
     * @see WallService#addPost(User, String, byte[], Wall)
     * @see Wall#canPost(User)
     */
    public WallPost addPost(User sender, String message, User pageOwner, byte[] picture) {
        return addPost(sender, message, picture, userWallRepository.findByUser(pageOwner));
    }

    /**
     * "Частный случай" метода {@link WallService#addPost(User, String, byte[], Wall)}.
     * Добавляет пост на стену группы с заданным сообщением и отправителем.
     * В случае, если отправитель не имеет права отправить пост на стену группы,
     * генерирует соответствующие исключения. В противном случае возвращает объект, соответствующий отправленному
     * посту.
     * @param sender отправитель.
     * @param message сообщение поста.
     * @param group группа, на чью стену необходимо отправить пост.
     * @return объект, соответствующий отправленному посту.
     * @see WallService#addPost(User, String, byte[], Wall)
     * @see Wall#canPost(User)
     */
    public WallPost addPost(User sender, String message, Group group) {
        return addPost(sender, message, null, groupWallRepository.findByGroup(group));
    }

    /**
     * "Частный случай" метода {@link WallService#addPost(User, String, byte[], Wall)}.
     * Добавляет пост на стену группы с заданным сообщением и отправителем.
     * В случае, если отправитель не имеет права отправить пост на стену группы,
     * генерирует соответствующие исключения. В противном случае возвращает объект, соответствующий отправленному
     * посту.
     * @param sender отправитель.
     * @param message сообщение поста.
     * @param group группа, на чью стену необходимо отправить пост.
     * @param picture изображение в виде байтового массива.
     * @return объект, соответствующий отправленному посту.
     * @see WallService#addPost(User, String, byte[], Wall)
     * @see Wall#canPost(User)
     */
    public WallPost addPost(User sender, String message, Group group, byte[] picture) {
        return addPost(sender, message, picture, groupWallRepository.findByGroup(group));
    }

    /**
     * Добавляет пост с заданным сообщением и отправителем на заданную стену.
     * В случае, если стены не существует (т.е. у нее нулевой идентификатор) или
     * если отправитель не имеет права отправить пост на заданную стену,
     * генерирует соответствующие исключения. В противном случае возвращает объект, соответствующий
     * отправленному посту.
     * @param sender
     * @param message
     * @param picture
     * @param wall
     * @return сохраненный пост
     */
    private WallPost addPost(User sender, String message, byte[] picture, Wall wall) {
        sender = userRepository.findById(sender.getId()).orElseThrow(NoUserFoundException::new);
        if(wall == null || wall.getId() == null) throw new NoWallFoundException();
        // обновление стены (возможно, в ней хранится старый объект пользователя или группы)
        wall = wallRepository.findById(wall.getId()).get();
        if(!wall.canPost(sender)) throw new WallNoPostAccessException();
        WallPost post = new WallPost(sender, message, LocalDateTime.now(), wall, picture);
        wall.getPosts().add(post);
        wallRepository.save(wall);
        return postRepository.save(post);
    }

    public void removePost(WallPost post) {
        postRepository.delete(post);
    }


    public WallPost like(WallPost post, User user) {
        postRepository.addLike(post.getId(), user.getId());
        return post;
    }

    public WallPost removeLike(WallPost post, User user) {
        postRepository.removeLike(post.getId(), user.getId());
        return post;
    }

    public boolean checkLike(WallPost post, User user) {
        return postRepository.checkLike(post.getId(), user.getId()).size() == 0;
    }

    public void updatePostPicture(WallPost post, byte[] picture) {
        postRepository.updatePicture(post.getId(), picture);
    }
}
