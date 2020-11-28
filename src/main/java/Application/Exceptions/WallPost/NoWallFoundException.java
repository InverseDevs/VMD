package Application.Exceptions.WallPost;

import Application.Exceptions.APIException;

public class NoWallFoundException extends APIException {
    public NoWallFoundException() { super("Wall is not found."); }
}