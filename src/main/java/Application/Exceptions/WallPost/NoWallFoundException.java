package Application.Exceptions.WallPost;

import Application.Exceptions.ObjectIsNotFoundByIdException;

public class NoWallFoundException extends ObjectIsNotFoundByIdException {
    public NoWallFoundException() { super("Wall"); }
}