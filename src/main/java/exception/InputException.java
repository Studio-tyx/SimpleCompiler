package exception;

/**
 * @author TYX
 * @name InputException
 * @description
 * @time
 **/
public class InputException extends Exception{
    public InputException() {
        super();
    }

    public InputException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
