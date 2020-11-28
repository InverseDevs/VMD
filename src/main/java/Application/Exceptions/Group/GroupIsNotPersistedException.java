package Application.Exceptions.Group;

import Application.Exceptions.APIException;

public class GroupIsNotPersistedException extends APIException {
    public GroupIsNotPersistedException() {
        super("Group is not persisted and thus cannot be refreshed/updated.");
    }
}
