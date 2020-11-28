package Application.Exceptions.WallPost;

import Application.Exceptions.APIException;

public class WallNoPostAccessException extends APIException  {
    public WallNoPostAccessException() {
        super("No access to make a post on the wall.");
    }
}
