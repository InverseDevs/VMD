package Application.Exceptions.Group;

import Application.Exceptions.APIException;

public class GroupNotFoundByLinkException extends APIException {
    public GroupNotFoundByLinkException() {
        super("Group with this personal link does not exist.");
    }
}
