package atlas.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Ricky Fung
 */
public interface RowMapper<T> {

    T mapRow(ResultSet rs) throws SQLException;

}
