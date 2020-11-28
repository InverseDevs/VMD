package Application.Exceptions.Group;

import Application.Exceptions.APIException;

public class GroupNotFoundException extends APIException {
    public GroupNotFoundException() {
        super("Group with this id does not exist");
    }
}
