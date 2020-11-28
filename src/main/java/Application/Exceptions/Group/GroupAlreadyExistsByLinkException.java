package Application.Exceptions.Group;

import Application.Exceptions.APIException;

public class GroupAlreadyExistsByLinkException extends APIException {
    public GroupAlreadyExistsByLinkException() {
        super("Group with this personal link already exists.");
    }
}
