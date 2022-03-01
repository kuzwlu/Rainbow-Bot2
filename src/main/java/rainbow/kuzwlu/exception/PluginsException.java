package rainbow.kuzwlu.exception;

public class PluginsException extends RuntimeException{

    public PluginsException(String msg) {
        super(msg);
    }

    public PluginsException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PluginsException(Throwable cause) {
        super(cause);
    }

}
