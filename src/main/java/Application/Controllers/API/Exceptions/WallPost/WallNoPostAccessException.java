package Application.Controllers.API.Exceptions.WallPost;

import Application.Controllers.API.Exceptions.APIException;

public class WallNoPostAccessException extends APIException  {
    public WallNoPostAccessException() {
        super("No access to make a post on the wall.");
    }
}
