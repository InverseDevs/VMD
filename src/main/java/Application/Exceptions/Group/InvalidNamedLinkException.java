package Application.Exceptions.Group;

import Application.Exceptions.APIException;

public class InvalidNamedLinkException extends APIException {
    public InvalidNamedLinkException() {
        super("Invalid named link (either empty or contains only digits).");
    }
}
