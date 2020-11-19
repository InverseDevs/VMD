package Application;
import Application.Database.WallPostRepository;
import Application.Entities.Chat;
import Application.Entities.Content.ChatMessage;
import Application.Entities.Content.Comment;
import Application.Entities.Content.WallPost;
import Application.Database.RoleRepository;
import Application.Database.User.UserRepository;
import Application.Entities.Role;
import Application.Entities.User;
import Application.Services.ChatService;
import Application.Services.CommentService;
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
    WallPostRepository postRepo;
    @Autowired
    ChatService chatService;
    @Autowired
    CommentService commentService;

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
            userRepo.save(admin);
            userRepo.makeAdmin(admin);

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
                userRepo.save(user);
                userRepo.makeUser(user);
            }

            userRepo.addFriend(nixon, 1L);
            userRepo.addFriend(nixon, 2L);
            userRepo.addFriend(nixon, 3L);

            ArrayList<WallPost> posts = new ArrayList<>();
            WallPost.PageType type = WallPost.PageType.USER;
            posts.add(new WallPost(users.get(2), "Hello admin!", LocalDateTime.now(), 1L, type));
            posts.add(new WallPost(users.get(0), "thx for making me alive!", LocalDateTime.now(), 1L, type));
            posts.add(new WallPost(users.get(2), "u a de best", LocalDateTime.now(), 1L, type));
            posts.add(new WallPost(admin, "Hello skelantros!", LocalDateTime.now(), 4L, type));
            posts.add(new WallPost(users.get(2), "hey twin!", LocalDateTime.now(), 2L, type));
            posts.add(new WallPost(users.get(1), "hi there!", LocalDateTime.now(), 3L, type));

            posts.add(new WallPost(users.get(3), "Special for Andrew!", LocalDateTime.now(), 5L, type));
            posts.forEach(postRepo::save);

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
            innerComment.setReference_comment(complexComment);

            commentService.addComment(simpleComment);
            commentService.addComment(complexComment);
            commentService.addComment(innerComment);

            Chat p2pChat = chatService.getChat(admin, users.get(2));
            chatService.saveMessage(new ChatMessage("Hello admin", LocalDateTime.now(), users.get(2), p2pChat));
            chatService.saveMessage(new ChatMessage("Hello skelantros!", LocalDateTime.now(), admin, p2pChat));

            Chat multiChat = chatService.getChat(new HashSet<>(users));
            chatService.saveMessage(new ChatMessage("hey y'all!", LocalDateTime.now(), users.get(2), multiChat));
            chatService.saveMessage(new ChatMessage("hey dude!", LocalDateTime.now(), users.get(0), multiChat));
            chatService.saveMessage(new ChatMessage("it's a multi chat test", LocalDateTime.now(), users.get(1), multiChat));
        };
    }
}