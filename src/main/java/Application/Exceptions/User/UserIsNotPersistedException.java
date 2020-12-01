package Application.Exceptions.User;

import Application.Exceptions.ObjectIsNotPersistedException;

public class UserIsNotPersistedException extends ObjectIsNotPersistedException {
    public UserIsNotPersistedException() {
        super("User");
    }
}
