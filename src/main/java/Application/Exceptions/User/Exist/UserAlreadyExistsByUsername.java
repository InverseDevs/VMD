package Application.Exceptions.User.Exist;

public class UserAlreadyExistsByUsername extends UserAlreadyExists {
    public UserAlreadyExistsByUsername() {
        super("username");
    }
}
