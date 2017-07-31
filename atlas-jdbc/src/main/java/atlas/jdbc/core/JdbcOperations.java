package atlas.jdbc.core;

import atlas.jdbc.exception.DataAccessException;
import java.sql.Connection;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public interface JdbcOperations {

    <T> T query(String sql, RowMapper<T> mapper, Object... params) throws DataAccessException;

    <T> T query(Connection conn, String sql, RowMapper<T> mapper, Object... params) throws DataAccessException;

    <T> T insert(String sql, RowMapper<T> mapper, Object... params) throws DataAccessException;

    <T> T insert(Connection conn, String sql, RowMapper<T> mapper, Object... params) throws DataAccessException;

    int update(String sql, Object... params) throws DataAccessException;

    int update(Connection conn, String sql, Object... params) throws DataAccessException;
}
