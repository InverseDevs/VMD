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

    public UserWall(User user) {
        this.user = user;
    }

    @Override
    public boolean canPost(User user) {
        return hasAccess(this.user.getPostAccess(), user);
    }

    @Override
    public boolean canComment(User user) {
        return hasAccess(this.user.getCommentAccess(), user);
    }

    @Override
    public boolean canDeleteContent(User user) {
        return user.equals(this.user);
    }

    // TODO на данный момент метод работает неправильно, переделать!
    private boolean hasAccess(User.Access access, User user) {
        // TODO добавить поддержку администраторов
        if(this.user.equals(user)) return true;
        if(access.equals(User.Access.EVERYONE)) return true;
        else if(access.equals(User.Access.FRIENDS)) return this.user.getFriends().contains(user);
        else if(access.equals(User.Access.NOBODY)) return false;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof UserWall)) return false;
        UserWall w = (UserWall) o;
        return this.user.equals(w.user);
    }
}