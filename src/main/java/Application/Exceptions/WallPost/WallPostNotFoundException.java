package Application.Exceptions.WallPost;

import Application.Exceptions.APIException;

public class WallPostNotFoundException extends APIException {
    public WallPostNotFoundException() {
        super("Post with this id does not exist.");
    }

    public WallPostNotFoundException(String message) {
        super(message);
    }
}
