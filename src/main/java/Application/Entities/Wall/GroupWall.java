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

    public GroupWall(Group group, AccessType postAccess, AccessType commentAccess) {
        super(postAccess, commentAccess);
        this.group = group;
    }

    public GroupWall(Group group) {
        this(group, AccessType.ADMINISTRATORS, AccessType.EVERYONE);
    }

    @Override
    public boolean canPost(User user) {
        return hasAccess(this.getPostAccess(), user);
    }

    @Override
    public boolean canComment(User user) {
        return hasAccess(this.getCommentAccess(), user);
    }

    private boolean hasAccess(AccessType access, User user) {
        if(this.group.getBannedUsers().contains(user)) return false;
        if(access == AccessType.EVERYONE) return true;
        if(access == AccessType.MEMBERS) return group.getMembers().contains(user);
        if(access == AccessType.ADMINISTRATORS) return group.getAdministrators().contains(user);
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof GroupWall)) return false;
        GroupWall w = (GroupWall) o;
        return this.group.equals(w.group);
    }
}
