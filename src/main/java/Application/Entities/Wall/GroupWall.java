package Application.Entities.Wall;

import Application.Entities.Group;
import Application.Entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@Entity
@NoArgsConstructor
@DiscriminatorValue("GROUP")
public class GroupWall extends Wall {
    @OneToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public GroupWall(Group group) {
        this.group = group;
    }
    @Override
    public boolean canPost(User user) {
        return hasAccess(group.getPostAccess(), user);
    }

    @Override
    public boolean canComment(User user) {
        return hasAccess(group.getCommentAccess(), user);
    }

    private boolean hasAccess(Group.Access access, User user) {
        if(group.getOwner().equals(user)) return true;
        if(group.getBannedUsers().contains(user)) return false;

        if(access == Group.Access.EVERYONE)
            return true;
        if(access == Group.Access.MEMBERS)
            return group.getMembers().contains(user);
        if(access == Group.Access.ADMINISTRATORS)
            return group.getAdministrators().contains(user);
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof GroupWall)) return false;
        GroupWall w = (GroupWall) o;
        return this.group.equals(w.group);
    }
}
