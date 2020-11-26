package Application.Controllers.API.Exceptions.Group;

import Application.Controllers.API.Exceptions.APIException;

public class GroupAlreadyExistsByLinkException extends APIException {
    public GroupAlreadyExistsByLinkException() {
        super("Group with this personal link already exists.");
    }
}
