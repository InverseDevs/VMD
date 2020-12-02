package Application.Exceptions.Group;

import Application.Exceptions.ObjectIsNotFoundByIdException;

public class GroupNotFoundException extends ObjectIsNotFoundByIdException {
    public GroupNotFoundException() {
        super("Group");
    }
}
