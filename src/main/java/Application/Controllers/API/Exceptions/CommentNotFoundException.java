package Application.Controllers.API.Exceptions;

public class CommentNotFoundException extends APIException {
    public CommentNotFoundException() {
        super("Comment with this id does not exist.");
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}