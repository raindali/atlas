package atlas.beans.exception;

/**
 * @author Ricky Fung
 */
public class BeanException extends RuntimeException {

    private static final long serialVersionUID = -5662312540877918279L;

    public BeanException() {
    }

    public BeanException(String message) {
        super(message);
    }

    public BeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanException(Throwable cause) {
        super(cause);
    }

    public BeanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
