package Application.Controllers.API.Exceptions;

public class WrongRequestException extends APIException {
    public WrongRequestException(String message) {
        super(message);
    }

    public WrongRequestException() {
        super("Wrong request. Please follow the API documentation.");
    }
}
