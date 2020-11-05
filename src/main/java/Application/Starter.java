package Application;
import Application.Database.WallPostRepository;
import Application.Entities.Content.WallPost;
import Application.Entities.User.UserInfo;
import Application.Database.RoleRepository;
import Application.Database.User.UserRepository;
import Application.Database.UserInfoRepository;
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
    //public static final String homeLink = "http://inversedevs.herokuapp.com";
    public static final String homeLink = "http://localhost:8080";
    public static final String apiLink = "/api";
    @Autowired
    UserRepository userRepo;
    @Autowired
    RoleRepository roleRepo;
    @Autowired
    UserInfoRepository infoRepo;
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

            User admin = new User("admin", "admin", "admin@vmd.com");
            userRepo.save(admin);
            userRepo.makeAdmin(admin);

            ArrayList<User> users = new ArrayList<>();
            users.add(new User("test1", "1234", "test1@vmd.com"));
            users.add(new User("test2", "1234", "test2@vmd.com"));
            users.add(new User("skelantros", "23052001", "skelantros@vmd.com"));
            for(User user : users) {
                userRepo.save(user);
                userRepo.makeUser(user);
            }
            ArrayList<UserInfo> infos = new ArrayList<>();
            infos.add(new UserInfo(1L, "admin", "Admin", "VMD", null));
            infos.add(new UserInfo(2L, "test1", "Test Account 1", "VMD", null));
            infos.add(new UserInfo(3L, "test2", "Test Account 2", "VMD", null));
            infos.add(new UserInfo(4L, "skelantros", "Alex Egorowski", "Zelenokumsk", null));
            infos.forEach(i -> infoRepo.save(i));

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