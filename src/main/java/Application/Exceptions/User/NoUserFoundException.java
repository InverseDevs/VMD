package Application.Exceptions.User;

import Application.Exceptions.ObjectIsNotFoundByIdException;

public class NoUserFoundException extends ObjectIsNotFoundByIdException {
    public NoUserFoundException() {
        super("User");
    }
}
