package Application.Exceptions.Group;

import Application.Exceptions.ObjectIsNotPersistedException;

public class GroupIsNotPersistedException extends ObjectIsNotPersistedException {
    public GroupIsNotPersistedException() {
        super("Group");
    }
}
