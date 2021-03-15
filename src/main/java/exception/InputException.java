package exception;

/**
 * @author TYX
 * @name InputException
 * @description 输入异常（如空行、文法格式错误等）
 * @time 2021/3/21 19:21
 **/
public class InputException extends Exception {
    public InputException() {
        super();
    }

    /**
     * 带异常信息的构造器
     *
     * @param message 异常信息
     */
    public InputException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
