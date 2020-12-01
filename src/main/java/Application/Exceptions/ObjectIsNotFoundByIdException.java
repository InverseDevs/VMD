package Application.Exceptions;

public abstract class ObjectIsNotFoundByIdException extends APIException {
    public ObjectIsNotFoundByIdException(String name) {
        super(name + "with this id does not exist.");
    }
}
