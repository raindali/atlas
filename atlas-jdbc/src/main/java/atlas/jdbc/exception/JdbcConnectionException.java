package atlas.jdbc.exception;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class JdbcConnectionException extends DataAccessException {

    public JdbcConnectionException(String message) {
        super(message);
    }

    public JdbcConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
