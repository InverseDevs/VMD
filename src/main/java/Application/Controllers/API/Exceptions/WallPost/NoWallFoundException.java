package Application.Controllers.API.Exceptions.WallPost;

import Application.Controllers.API.Exceptions.APIException;

public class NoWallFoundException extends APIException {
    public NoWallFoundException() { super("Wall is not found."); }
}