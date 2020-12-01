package Application.Exceptions;

import Application.Exceptions.APIException;

public abstract class ObjectIsNotPersistedException extends APIException {
    public ObjectIsNotPersistedException(String name) {
        super(name + " object is not persisted and thus cannot be refreshed/updated.");
    }
}
