package Application.Controllers.API.Exceptions.Group;

import Application.Controllers.API.Exceptions.APIException;

public class GroupNotFoundException extends APIException {
    public GroupNotFoundException() {
        super("Group with this id does not exist");
    }
}
