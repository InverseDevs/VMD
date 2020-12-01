package Application.Exceptions.User.Exist;

import Application.Exceptions.APIException;

public class UserAlreadyExists extends APIException {
    public UserAlreadyExists() {
        super("User already exists");
    }

    public UserAlreadyExists(String parameter) {
        super("User with such " + parameter + " already exists.");
    }
}
