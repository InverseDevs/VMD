package Application;
import Application.Database.RoleRepository;
import Application.Database.UserRepository;
import Application.Entities.Role;
import Application.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.UUID;

@SpringBootApplication
public class Starter {
    @Autowired
    UserRepository userRepo;
    @Autowired
    RoleRepository roleRepo;

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
        };
    }
}