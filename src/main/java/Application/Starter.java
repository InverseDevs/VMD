package Application;
import Application.Database.WallPostRepository;
import Application.Entities.Chat;
import Application.Entities.Content.ChatMessage;
import Application.Entities.Content.WallPost;
import Application.Database.RoleRepository;
import Application.Database.User.UserRepository;
import Application.Entities.Role;
import Application.Entities.User;
import Application.Entities.Wall.Wall;
import Application.Services.ChatService;
import Application.Services.UserService;
import Application.Services.WallService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
    ChatService chatService;
    @Autowired
    WallService wallService;
    @Autowired
    UserService userService;

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
            userService.createUser(admin);
            userService.makeAdmin(admin);

            ArrayList<User> users = new ArrayList<>();
            users.add(new User("test1", "1234", "test1@vmd.com",
                    "Test Account 1", "VMD", null));
            users.add(new User("test2", "1234", "test2@vmd.com",
                    "Test Account 2", "VMD", null));
            users.add(new User("skelantros", "23052001", "skelantros@vmd.com",
                    "Alex Egorowski", "Zelenokumsk", new Date(990561600000L)));
            for(int i = 0; i < users.size(); ++i) {
                users.set(i, userService.createUser(users.get(i)));
            }

            ArrayList<WallPost> posts = new ArrayList<>();

            posts.add(wallService.addPost(users.get(2), "Hello admin!", admin));
            posts.add(wallService.addPost(admin, "Hello skelantros!", users.get(2)));
            posts.add(wallService.addPost(users.get(0), "thx for making me alive!", admin));
            posts.add(wallService.addPost(users.get(2), "u a de best", admin));
            posts.add(wallService.addPost(users.get(1), "hey twin!", users.get(0)));
            posts.add(wallService.addPost(users.get(0), "hi there!", users.get(1)));

            wallService.setUserWallPostAccess(users.get(2), Wall.AccessType.FRIENDS);
            posts.add(wallService.addPost(users.get(0), "This post won't be sent because we are not friends", users.get(2)));
            userService.addFriend(users.get(2), users.get(0));
            posts.add(wallService.addPost(users.get(0), "This post will be sent because we are friends!", users.get(2)));

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