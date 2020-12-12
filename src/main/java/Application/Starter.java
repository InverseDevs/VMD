package Application;

import Application.Database.RoleRepository;
import Application.Entities.Role;
import Application.Entities.User;
import Application.Services.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
@Slf4j
public class Starter {
    public static final String homeLink = "http://inversedevs.herokuapp.com";
    //public static final String homeLink = "http://localhost:8080";
    @Autowired
    RoleRepository roleRepo;
    @Autowired
    ChatService chatService;
    @Autowired
    CommentService commentService;
    @Autowired
    WallService wallService;
    @Autowired
    UserService userService;
    @Autowired
    GroupService groupService;

    public static final long adminRoleId = 2;
    public static final long userRoleId = 1;

    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }

    @Bean
    CommandLineRunner addDefaultDbNotes() {
        return (String... args) -> {
            try {
                //roleRepo.save(new Role(userRoleId, "ROLE_USER"));
                //roleRepo.save(new Role(adminRoleId, "ROLE_ADMIN"));

                User admin = userService.createUser("admin", "admin", "admin@vmd.com",
                        "Admin", "VMD", LocalDate.parse("1988-01-01"));
                userService.makeAdmin(admin);
                userService.permitUser(admin);
            } catch (Exception e) {
                log.info("init info lready exists");
            }
        };
    }
}