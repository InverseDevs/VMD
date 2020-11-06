package Application;
import Application.Database.WallPostRepository;
import Application.Entities.Content.WallPost;
import Application.Database.RoleRepository;
import Application.Database.User.UserRepository;
import Application.Entities.Role;
import Application.Entities.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;

@SpringBootApplication
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
                    "Alex Egorowski", "Zelenokumsk", new Date(990561600000L)));
            for(User user : users) {
                userRepo.save(user);
                userRepo.makeUser(user);
            }

            ArrayList<WallPost> posts = new ArrayList<>();
            WallPost.PageType type = WallPost.PageType.USER;
            posts.add(new WallPost("skelantros", "Hello admin!", new Date(), 1L, type));
            posts.add(new WallPost("test1", "thx for making me alive!", new Date(), 1L, type));
            posts.add(new WallPost("test2", "u a de best", new Date(), 1L, type));
            posts.add(new WallPost("admin", "Hello skelantros!", new Date(), 4L, type));
            posts.add(new WallPost("test2", "hey twin!", new Date(), 2L, type));
            posts.add(new WallPost("test1", "hi there!", new Date(), 3L, type));
            posts.forEach(postRepo::save);
        };
    }
}