package Application.Controllers.API.Exceptions.Group;

import Application.Controllers.API.Exceptions.APIException;

public class GroupNotFoundByLinkException extends APIException {
    public GroupNotFoundByLinkException() {
        super("Group with this personal link does not exist.");
    }
}
