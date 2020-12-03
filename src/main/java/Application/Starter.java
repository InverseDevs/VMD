package Application;
import Application.Exceptions.WallPost.WallNoPostAccessException;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

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

    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }

    @Bean
    CommandLineRunner addDefaultDbNotes() {
        return (String... args) -> {
            roleRepo.save(new Role(1L, "ROLE_USER"));
            roleRepo.save(new Role(2L, "ROLE_ADMIN"));

            User admin = userService.createUser("admin", "admin", "admin@vmd.com",
                    "Admin", "VMD", null);
            userService.makeAdmin(admin);

            User test1 = userService.createUser("test1", "1234", "test1@vmd.com",
                    "Test Account 1", "VMD", null);

            User test2 = userService.createUser("test2", "1234", "test2@vmd.com",
                    "Test Account 2", "VMD", null);

            User skelantros = userService.createUser("skelantros", "23052001", "skelantros@vmd.com",
                    "Alex Egorowski", "Zelenokumsk", LocalDate.parse("1990-12-12"));

            User nixon = userService.createUser("NixoN", "67562211", "mythtics2001@mail.ru",
                    "Andrew Zhukov", "Ishim", LocalDate.parse("2001-02-15"));

            ArrayList<User> users = new ArrayList<>();
            users.add(test1);
            users.add(test2);
            users.add(skelantros);
            users.add(nixon);
            users.forEach(userService::permitUser);
            userService.permitUser(admin);

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
                log.info(e.getMessage());
            }
            userService.updatePostAccess(skelantros, User.Access.NOBODY);
            try {
                wallService.addPost(test1, "I am still a friend of Skel, but I can't send anything on his wall :(", skelantros);
            } catch(WallNoPostAccessException e) {
                log.info(e.getMessage());
            }

            Comment simpleComment = new Comment(userService.findUserById(1L),
                    "simple comment",
                    LocalDateTime.now(),
                    wallService.findPostById(7));

            Comment complexComment = new Comment(userService.findUserById(1L),
                    "complex comment",
                    LocalDateTime.now(),
                    wallService.findPostById(7));

            Comment innerComment = new Comment(userService.findUserById(1L),
                    "inner comment",
                    LocalDateTime.now(),
                    wallService.findPostById(7));
            innerComment.setReferenceComment(complexComment);

            commentService.addComment(simpleComment);
            commentService.addComment(complexComment);
            commentService.addComment(innerComment);

            Chat p2pChat = chatService.getChat(admin, users.get(2));
            chatService.saveMessage(new ChatMessage("Hello admin", LocalDateTime.now(), users.get(2), p2pChat));
            chatService.saveMessage(new ChatMessage("Hello skelantros!", LocalDateTime.now(), admin, p2pChat));

            Group group = groupService.createGroup("Test group", "test", nixon);
            groupService.addMembers(group, new HashSet<>(users));
            groupService.removeMember(group, test1);
            groupService.addAdministrator(group, skelantros);
            groupService.banUser(group, test2);
            groupService.banUser(group, admin);
            groupService.unbanUser(group, admin);

            WallPost groupPost = wallService.addPost(nixon, "Hello group!", group);

            Comment groupComment = new Comment(nixon, "This post is wonderful!",
                    LocalDateTime.now(), groupPost);
            Comment groupComplexComment = new Comment(skelantros, "I am an admin here!",
                    LocalDateTime.now(), groupPost);
            Comment groupInnerComment = new Comment(test2, "Hello admin!",
                    LocalDateTime.now(), groupPost);
            groupInnerComment.setReferenceComment(groupComplexComment);
            commentService.addComment(groupComment);
            commentService.addComment(groupComplexComment);
            commentService.addComment(groupInnerComment);
        };
    }
}