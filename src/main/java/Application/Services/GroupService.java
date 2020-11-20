package Application.Services;

import Application.Database.GroupRepository;
import Application.Database.Wall.WallRepository;
import Application.Entities.Group;
import Application.Entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private WallRepository wallRepository;

    public Group findById(Long id) {
        return groupRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public Group findByNamedLink(String namedLink) {
        return groupRepository.findByNamedLink(namedLink).orElseThrow(RuntimeException::new);
    }

    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }

    public Group refresh(Group group) {
        return groupRepository.findById(group.getId()).orElseThrow(RuntimeException::new);
    }

    public Group update(Group group) {
        if(group.getId() == null) throw new RuntimeException();
        if(!groupRepository.existsById(group.getId())) throw new RuntimeException();
        return groupRepository.save(group);
    }

    public Group createGroup(String name, String namedLink, User owner) {
        if(groupRepository.existsByNamedLink(namedLink)) throw new RuntimeException();
        Group group = new Group(name, owner, namedLink);
        wallRepository.save(group.getWall());
        return groupRepository.save(group);
    }
}
