package Application.Exceptions.Comment;

import Application.Exceptions.APIException;

public class WallNoCommentAccessException extends APIException {
    public WallNoCommentAccessException() {
        super("No access to leave a comment on this wall.");
    }
}
