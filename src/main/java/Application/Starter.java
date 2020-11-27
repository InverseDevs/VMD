package Application;
import Application.Controllers.API.Exceptions.WallPost.NoWallFoundException;
import Application.Controllers.API.Exceptions.WallPost.WallNoPostAccessException;
import Application.Database.CommentRepository;
import Application.Database.GroupRepository;
import Application.Database.Wall.WallRepository;
import Application.Database.WallPostRepository;
import Application.Entities.Chat;
import Application.Entities.Content.ChatMessage;
import Application.Entities.Content.Comment;
import Application.Entities.Content.WallPost;
import Application.Database.RoleRepository;
import Application.Database.User.UserRepository;
import Application.Entities.Group;
import Application.Entities.Role;
import Application.Entities.User;
import Application.Services.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

@SpringBootApplication
@Slf4j
public class Starter {
    public static final String homeLink = "http://inversedevs.herokuapp.com";
    //public static final String homeLink = "http://localhost:8080";
    public static final String apiLink = "/api";
    @Autowired
    UserRepository userRepo;
    @Autowired
    RoleRepository roleRepo;
    @Autowired
    ChatService chatService;
    @Autowired
    CommentService commentService;
    @Autowired
    WallService wallService;
    @Autowired
    WallPostRepository postRepo;
    @Autowired
    UserService userService;
    @Autowired
    GroupService groupService;

    Logger logger = LoggerFactory.getLogger(Starter.class);

    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }

    @Bean
    CommandLineRunner addDefaultDbNotes() {
        return (String... args) -> {
            roleRepo.save(new Role(1L, "ROLE_USER"));
            roleRepo.save(new Role(2L, "ROLE_ADMIN"));

            User admin = new User("admin", "admin", "admin@vmd.com",
                    "Admin", "VMD", null);
            userService.saveUser(admin);
            userService.makeAdmin(admin);

            User test1 = new User("test1", "1234", "test1@vmd.com",
                    "Test Account 1", "VMD", null);

            User test2 = new User("test2", "1234", "test2@vmd.com",
                    "Test Account 2", "VMD", null);

            User skelantros = new User("skelantros", "23052001", "skelantros@vmd.com",
                    "Alex Egorowski", "Zelenokumsk", LocalDate.parse("1990-12-12"));

            User nixon = new User("NixoN", "67562211", "mythtics2001@mail.ru",
                    "Andrew Zhukov", "Ishim", LocalDate.parse("2001-02-15"));

            ArrayList<User> users = new ArrayList<>();
            users.add(test1);
            users.add(test2);
            users.add(skelantros);
            users.add(nixon);
            for(User user : users) {
                userService.saveUser(user);
                userService.makeUser(user);
            }

            userService.makeFriends(nixon, admin);
            userService.makeFriends(nixon, test1);
            userService.makeFriends(nixon, test2);

            ArrayList<WallPost> posts = new ArrayList<>();
            posts.add(wallService.addPost(users.get(2), "Hello admin!", admin));
            posts.add(wallService.addPost(admin, "Hello skelantros!", users.get(2)));
            posts.add(wallService.addPost(users.get(0), "thx for making me alive!", admin));
            posts.add(wallService.addPost(users.get(2), "u a de best", admin));
            posts.add(wallService.addPost(users.get(1), "hey twin!", users.get(0)));
            posts.add(wallService.addPost(users.get(0), "hi there!", users.get(1)));


            posts.add(wallService.addPost(users.get(2), "Special for Andrew!", users.get(3)));
            // сохранять посты более нет необходимости: это делается в рамках метода WallService.addPost
            //posts.forEach(postRepo::save);

            userService.updatePostAccess(skelantros, User.Access.FRIENDS);
            userService.makeFriends(skelantros, test1);
            wallService.addPost(test1, "It's a-me, Skel, your friend!", skelantros);
            try {
                wallService.addPost(test2, "I am not a friend of Skel, so my post won't be sent ):", skelantros);
            } catch(WallNoPostAccessException e) {
                logger.info(e.getMessage());
            }
            userService.updatePostAccess(skelantros, User.Access.NOBODY);
            try {
                wallService.addPost(test1, "I am still a friend of Skel, but I can't send anything on his wall :(", skelantros);
            } catch(WallNoPostAccessException e) {
                logger.info(e.getMessage());
            }

            Comment simpleComment = new Comment(userRepo.findById(1L).get(),
                    "simple comment",
                    LocalDateTime.now(),
                    postRepo.findById(7L).get());

            Comment complexComment = new Comment(userRepo.findById(1L).get(),
                    "complex comment",
                    LocalDateTime.now(),
                    postRepo.findById(7L).get());

            Comment innerComment = new Comment(userRepo.findById(1L).get(),
                    "inner comment",
                    LocalDateTime.now(),
                    postRepo.findById(7L).get());
            innerComment.setReferenceComment(complexComment);

            commentService.addComment(simpleComment);
            commentService.addComment(complexComment);
            commentService.addComment(innerComment);

            Chat p2pChat = chatService.getChat(admin, users.get(2));
            chatService.saveMessage(new ChatMessage("Hello admin", LocalDateTime.now(), users.get(2), p2pChat));
            chatService.saveMessage(new ChatMessage("Hello skelantros!", LocalDateTime.now(), admin, p2pChat));

//            Chat multiChat = chatService.getChat(new HashSet<>(users));
//            chatService.saveMessage(new ChatMessage("hey y'all!", LocalDateTime.now(), users.get(2), multiChat));
//            chatService.saveMessage(new ChatMessage("hey dude!", LocalDateTime.now(), users.get(0), multiChat));

            Group group1 = groupService.createGroup("Test Group","test", admin);
            users.forEach(group1::addMember);
            group1.addAdministrator(users.get(2));
            group1.banUser(users.get(1));
            group1.banUser(admin); // этот пользователь не будет забанен, т.к. он является владельцем группы
            group1 = groupService.update(group1);

            try {
                wallService.addPost(admin, "Post from group owner", group1);
                wallService.addPost(users.get(2), "Post from group admin", group1);
                wallService.addPost(users.get(0), "Post from group member", group1);
            } catch(WallNoPostAccessException e) {
                logger.info(e.getMessage());
            }
        };
    }
}