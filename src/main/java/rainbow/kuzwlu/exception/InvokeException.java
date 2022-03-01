package rainbow.kuzwlu.exception;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2021/4/1 17:47
 * @Version 1.0
 */

public class InvokeException extends RuntimeException{

    public InvokeException(String msg) {
        super(msg);
    }

    public InvokeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvokeException(Throwable cause) {
        super(cause);
    }

}
