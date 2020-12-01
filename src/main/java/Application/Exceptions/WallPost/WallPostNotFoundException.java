package Application.Exceptions.WallPost;

import Application.Exceptions.ObjectIsNotFoundByIdException;

public class WallPostNotFoundException extends ObjectIsNotFoundByIdException {
    public WallPostNotFoundException() {
        super("Post");
    }
}
