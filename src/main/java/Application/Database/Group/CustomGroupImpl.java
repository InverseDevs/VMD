package Application.Database.Group;

import Application.Entities.Group;
import Application.Entities.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CustomGroupImpl implements CustomGroup {
    @PersistenceContext
    private EntityManager em;
    private final Function<Group, Group> groupInDb = g -> em.find(Group.class, g.getId());
    private final Function<Set<User>, Set<User>> usersFromDb =
            uc -> uc.stream().map(u -> em.find(User.class, u.getId())).collect(Collectors.toSet());

    @Override
    public Group addMembers(Group group, Set<User> members) {
        Group groupDb = groupInDb.apply(group);
        usersFromDb.apply(members).forEach(groupDb::addMember);
        return groupDb;
    }

    @Override
    public Group removeMembers(Group group, Set<User> members) {
        Group groupDb = groupInDb.apply(group);
        usersFromDb.apply(members).forEach(groupDb::removeMember);
        return groupDb;
    }

    @Override
    public Group addAdministrators(Group group, Set<User> administrators) {
        Group groupDb = groupInDb.apply(group);
        usersFromDb.apply(administrators).forEach(groupDb::addAdministrator);
        return groupDb;
    }

    @Override
    public Group removeAdministrators(Group group, Set<User> administrators) {
        Group groupDb = groupInDb.apply(group);
        usersFromDb.apply(administrators).forEach(groupDb::removeAdministrator);
        return groupDb;
    }

    @Override
    public Group banUsers(Group group, Set<User> banned) {
        Group groupDb = groupInDb.apply(group);
        usersFromDb.apply(banned).forEach(groupDb::banUser);
        return groupDb;
    }

    @Override
    public Group unbanUsers(Group group, Set<User> banned) {
        Group groupDb = groupInDb.apply(group);
        usersFromDb.apply(banned).forEach(groupDb::unbanUser);
        return groupDb;
    }
}
