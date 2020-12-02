package Application.Exceptions.User.Exist;

import Application.Exceptions.APIException;

public class UserAlreadyExistsByEmail extends UserAlreadyExists {
    public UserAlreadyExistsByEmail() {
        super("email");
    }
}
