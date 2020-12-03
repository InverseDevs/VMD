package Application.Database.Group;

import Application.Entities.Group;
import Application.Entities.User;

import java.util.Set;

public interface CustomGroup {
    Group addMembers(Group group, Set<User> members);
    Group removeMembers(Group group, Set<User> members);
    Group addAdministrators(Group group, Set<User> administrators);
    Group removeAdministrators(Group group, Set<User> administrators);
    Group banUsers(Group group, Set<User> banned);
    Group unbanUsers(Group group, Set<User> banned);
}
