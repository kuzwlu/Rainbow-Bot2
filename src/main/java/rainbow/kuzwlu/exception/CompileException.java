package rainbow.kuzwlu.exception;

/**
 * @Author kuzwlu
 * @Description TODO
 * @Date 2021/4/1 17:47
 * @Version 1.0
 */

public class CompileException extends RuntimeException{

    public CompileException(String msg) {
        super(msg);
    }

    public CompileException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CompileException(Throwable cause) {
        super(cause);
    }

}
