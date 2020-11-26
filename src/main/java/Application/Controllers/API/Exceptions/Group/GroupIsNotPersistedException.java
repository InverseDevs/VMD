package Application.Controllers.API.Exceptions.Group;

import Application.Controllers.API.Exceptions.APIException;

public class GroupIsNotPersistedException extends APIException {
    public GroupIsNotPersistedException() {
        super("Group is not persisted and thus cannot be refreshed/updated.");
    }
}
