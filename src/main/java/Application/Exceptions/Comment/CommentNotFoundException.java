package Application.Exceptions.Comment;

import Application.Exceptions.APIException;

public class CommentNotFoundException extends APIException {
    public CommentNotFoundException() {
        super("Comment with this id does not exist.");
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}