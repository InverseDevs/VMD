package Application.Entities.Wall;

import Application.Entities.Content.WallPost;
import Application.Entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Entity
@NoArgsConstructor
@DiscriminatorValue("USER")
public class UserWall extends Wall {
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserWall(User user, AccessType postAccess, AccessType commentAccess) {
        super(postAccess, commentAccess);
        this.user = user;
    }

    public UserWall(User user) {
        this(user, AccessType.EVERYONE, AccessType.EVERYONE);
    }

    @Override
    public boolean canPost(User user) {
        return hasAccess(this.getPostAccess(), user);
    }

    @Override
    public boolean canComment(User user) {
        return hasAccess(this.getCommentAccess(), user);
    }

    // TODO на данный момент метод работает неправильно, переделать!
    private boolean hasAccess(AccessType access, User user) {
        if(this.user.equals(user)) return true;
        if(access.equals(AccessType.EVERYONE)) return true;
        else if(access.equals(AccessType.FRIENDS)) return this.user.getFriends().contains(user);
        else if(access.equals(AccessType.ADMINISTRATORS)) return false;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof UserWall)) return false;
        UserWall w = (UserWall) o;
        return this.user.equals(w.user);
    }
}