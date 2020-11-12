package Application;
import Application.Database.WallPostRepository;
import Application.Entities.Chat;
import Application.Entities.Content.ChatMessage;
import Application.Entities.Content.WallPost;
import Application.Database.RoleRepository;
import Application.Database.User.UserRepository;
import Application.Entities.Role;
import Application.Entities.User;
import Application.Services.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

            ArrayList<User> users = new ArrayList<>();
            users.add(new User("test1", "1234", "test1@vmd.com",
                    "Test Account 1", "VMD", null));
            users.add(new User("test2", "1234", "test2@vmd.com",
                    "Test Account 2", "VMD", null));
            users.add(new User("skelantros", "23052001", "skelantros@vmd.com",
                    "Alex Egorowski", "Zelenokumsk", LocalDate.parse("1990-12-12")));
            users.add(new User("NixoN", "67562211", "mythtics2001@mail.ru",
                    "Andrew Zhukov", "Ishim", LocalDate.parse("2001-02-15")));
            for(User user : users) {
                userRepo.save(user);
                userRepo.makeUser(user);
            }

            ArrayList<WallPost> posts = new ArrayList<>();
            WallPost.PageType type = WallPost.PageType.USER;
            posts.add(new WallPost(users.get(2), "Hello admin!", new Date(), 1L, type));
            posts.add(new WallPost(users.get(0), "thx for making me alive!", new Date(), 1L, type));
            posts.add(new WallPost(users.get(2), "u a de best", new Date(), 1L, type));
            posts.add(new WallPost(admin, "Hello skelantros!", new Date(), 4L, type));
            posts.add(new WallPost(users.get(2), "hey twin!", new Date(), 2L, type));
            posts.add(new WallPost(users.get(1), "hi there!", new Date(), 3L, type));
            posts.forEach(postRepo::save);

            Chat p2pChat = chatService.getChat(admin, users.get(2));
            chatService.saveMessage(new ChatMessage("Hello admin", new Date(), users.get(2), p2pChat));
            chatService.saveMessage(new ChatMessage("Hello skelantros!", new Date(), admin, p2pChat));

            Chat multiChat = chatService.getChat(new HashSet<>(users));
            chatService.saveMessage(new ChatMessage("hey y'all!", new Date(), users.get(2), multiChat));
            chatService.saveMessage(new ChatMessage("hey dude!", new Date(), users.get(0), multiChat));
            chatService.saveMessage(new ChatMessage("it's a multi chat test", new Date(), users.get(1), multiChat));
        };
    }
}