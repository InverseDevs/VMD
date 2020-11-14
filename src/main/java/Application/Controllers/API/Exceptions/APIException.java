package Application.Controllers.API.Exceptions;

public abstract class APIException extends RuntimeException {
    public APIException(String message) {
        super(message);
    }
}
