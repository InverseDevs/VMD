package Application.Exceptions;

public class NotEnoughPermissionsException extends APIException {
    public NotEnoughPermissionsException() {
        super("You don't have permission to do this.");
    }
}
