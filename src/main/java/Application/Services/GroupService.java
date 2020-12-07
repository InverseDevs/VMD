package Application.Services;

import Application.Exceptions.Group.*;
import Application.Database.Group.GroupRepository;
import Application.Database.Wall.WallRepository;
import Application.Entities.Group;
import Application.Entities.User;
import Application.Exceptions.User.UserIsNotPersistedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private WallRepository wallRepository;
    @Autowired
    private UserService userService;

    public Group findGroupById(Long id) {
        return groupRepository.findById(id).orElseThrow(GroupNotFoundException::new);
    }

    public Group findByNamedLink(String namedLink) {
        return groupRepository.findByNamedLink(namedLink.toLowerCase()).orElseThrow(GroupNotFoundByLinkException::new);
    }

    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }

    public List<Group> findAllGroupsByUserId(long userId) {
        List<Long> groupIds = groupRepository.findGroupsByUserId(userId);
        return groupIds.stream().map(this::findGroupById).collect(Collectors.toList());
    }

    public void updatePicture(Long groupId, byte[] picture) {
        groupRepository.updatePicture(groupId, picture);
    }


    public Group refresh(Group group) throws GroupIsNotPersistedException {
        return groupRepository.findById(group.getId()).orElseThrow(GroupIsNotPersistedException::new);
    }

    @Deprecated
    public Group update(Group group) throws GroupIsNotPersistedException {
        if(group.getId() == null) throw new GroupIsNotPersistedException();
        if(!groupRepository.existsById(group.getId())) throw new GroupNotFoundException();
        return groupRepository.save(group);
    }

    public Group createGroup(String name, String namedLink, User owner) throws GroupAlreadyExistsByLinkException {
        if(groupRepository.existsByNamedLink(namedLink.toLowerCase())) throw new GroupAlreadyExistsByLinkException();
        if(namedLink.equals("") || Pattern.compile("-?\\d+(\\.\\d+)?").matcher(namedLink).matches())
            throw new InvalidNamedLinkException();
        Group group = new Group(name, owner, namedLink.toLowerCase());
        wallRepository.save(group.getWall());
        return groupRepository.save(group);
    }

    public void deleteGroup(Long groupId) {
        Group group = findGroupById(groupId);
        wallRepository.deleteById(group.getWall().getId());
        groupRepository.deleteById(groupId);
    }

    public Group addMembers(Group group, Set<User> members)
            throws GroupIsNotPersistedException, UserIsNotPersistedException {
        return this.changeGroup(group, members, groupRepository::addMembers);
    }

    public Group addMember(Group group, User member)
            throws GroupIsNotPersistedException, UserIsNotPersistedException {
        return this.addMembers(group, Collections.singleton(member));
    }

    public Group removeMembers(Group group, Set<User> members)
            throws GroupIsNotPersistedException, UserIsNotPersistedException {
        return this.changeGroup(group, members, groupRepository::removeMembers);
    }

    public Group removeMember(Group group, User member)
            throws GroupIsNotPersistedException, UserIsNotPersistedException {
        return this.removeMembers(group, Collections.singleton(member));
    }

    public Group addAdministrators(Group group, Set<User> admins)
            throws GroupIsNotPersistedException, UserIsNotPersistedException {
        return this.changeGroup(group, admins, groupRepository::addAdministrators);
    }

    public Group addAdministrator(Group group, User admin)
            throws GroupIsNotPersistedException, UserIsNotPersistedException {
        return this.addAdministrators(group, Collections.singleton(admin));
    }

    public Group removeAdministrators(Group group, Set<User> admins)
            throws GroupIsNotPersistedException, UserIsNotPersistedException {
        return this.changeGroup(group, admins, groupRepository::removeAdministrators);
    }

    public Group removeAdministrator(Group group, User admin)
            throws GroupIsNotPersistedException, UserIsNotPersistedException {
        return this.removeAdministrators(group, Collections.singleton(admin));
    }

    public Group banUsers(Group group, Set<User> users)
            throws GroupIsNotPersistedException, UserIsNotPersistedException {
        return this.changeGroup(group, users, groupRepository::banUsers);
    }

    public Group banUser(Group group, User user)
            throws GroupIsNotPersistedException, UserIsNotPersistedException {
        return this.banUsers(group, Collections.singleton(user));
    }

    public Group unbanUsers(Group group, Set<User> users)
            throws GroupIsNotPersistedException, UserIsNotPersistedException {
        return this.changeGroup(group, users, groupRepository::unbanUsers);
    }

    public Group unbanUser(Group group, User user)
            throws GroupIsNotPersistedException, UserIsNotPersistedException {
        return this.unbanUsers(group, Collections.singleton(user));
    }

    public void updatePostAccess(Group group, Group.Access postAccess) {
        groupRepository.updatePostAccess(group, postAccess);
    }

    public void updateCommentAccess(Group group, Group.Access commentAccess) {
        groupRepository.updateCommentAccess(group, commentAccess);
    }

    private Group changeGroup(Group group, Set<User> users, BiFunction<Group, Set<User>, Group> method)
            throws GroupIsNotPersistedException, UserIsNotPersistedException {
        if(group.getId() == null)
            throw new GroupIsNotPersistedException();
        if(users.stream().anyMatch(u -> u.getId() == null))
            throw new UserIsNotPersistedException();
        return method.apply(group, users);
    }
}