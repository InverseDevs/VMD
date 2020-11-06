package Application.Controllers.API.Exceptions;

public class WallPostNotFoundException extends APIException {
    public WallPostNotFoundException() { super("Post with this id does not exist."); }
}
