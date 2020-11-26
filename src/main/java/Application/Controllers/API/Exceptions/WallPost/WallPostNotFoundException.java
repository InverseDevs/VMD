package Application.Controllers.API.Exceptions.WallPost;

import Application.Controllers.API.Exceptions.APIException;

public class WallPostNotFoundException extends APIException {
    public WallPostNotFoundException() {
        super("Post with this id does not exist.");
    }

    public WallPostNotFoundException(String message) {
        super(message);
    }
}
