package Application.Controllers.API.Exceptions.Comment;

import Application.Controllers.API.Exceptions.APIException;

public class WallNoCommentAccessException extends APIException {
    public WallNoCommentAccessException() {
        super("No access to leave a comment on this wall.");
    }
}
