package atlas.jdbc.core;

import atlas.jdbc.exception.DataAccessException;
import atlas.jdbc.util.DbUtils;
import atlas.util.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;

/**
 *
 * @author Ricky Fung
 */
public class JdbcTemplate extends JdbcAccessor implements JdbcOperations {
    private final Logger logger = LoggerFactory.getLogger(JdbcTemplate.class);

    @Override
    public <T> T query(String sql, RowMapper<T> mapper, Object... params) throws DataAccessException {
        return query(this.prepareConnection(), true, sql, mapper, params);
    }

    @Override
    public <T> T query(Connection conn, String sql, RowMapper<T> mapper, Object... params) throws DataAccessException {
        return query(conn, false, sql, mapper, params);
    }

    @Override
    public <T> T insert(String sql, RowMapper<T> mapper, Object... params) throws DataAccessException {
        return insert(this.prepareConnection(), true, sql, mapper, params);
    }

    @Override
    public <T> T insert(Connection conn, String sql, RowMapper<T> mapper, Object... params) throws DataAccessException {
        return insert(conn, false, sql, mapper, params);
    }

    @Override
    public int update(String sql, Object... params) throws DataAccessException {
        return update(this.prepareConnection(), true, sql, params);
    }

    @Override
    public int update(Connection conn, String sql, Object... params) throws DataAccessException {
        return update(conn, false, sql, params);
    }

    private  <T> T query(Connection conn, boolean closeConn, String sql, RowMapper<T> mapper, Object... params) throws DataAccessException {
        Validator.notNull(conn, "Connection object must not be null");
        Validator.notEmpty(sql, "sql must not be null");
        Validator.notNull(mapper, "RowMapper must not be null");

        if (logger.isDebugEnabled()) {
            logger.debug("Executing SQL query [" + sql + "]");
        }

        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            stmt = this.preparedStatement(conn, sql, Statement.RETURN_GENERATED_KEYS);
            this.fillStatement(stmt, params);
            rs = stmt.executeQuery();
            return mapper.mapRow(rs);
        } catch (SQLException e) {
            logger.debug("query caught exception", e);
            throw new DataAccessException("query caught exception", e);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(stmt);
            if(closeConn) {
                DbUtils.closeQuietly(conn);
            }
        }
    }

    private <T> T insert(Connection conn, boolean closeConn, String sql, RowMapper<T> mapper, Object... params) throws DataAccessException {
        Validator.notNull(conn, "Connection object must not be null");
        Validator.notEmpty(sql, "sql must not be null");
        Validator.notNull(mapper, "RowMapper must not be null");

        if (logger.isDebugEnabled()) {
            logger.debug("Executing SQL insert [" + sql + "]");
        }

        T generatedKeys = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        try {
            stmt = this.preparedStatement(conn, sql, Statement.RETURN_GENERATED_KEYS);
            this.fillStatement(stmt, params);
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            generatedKeys = mapper.mapRow(rs);
        } catch (SQLException e) {
            logger.debug("insert caught exception", e);
            throw new DataAccessException("insert caught exception", e);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(stmt);
            if(closeConn) {
                DbUtils.closeQuietly(conn);
            }
        }
        return generatedKeys;
    }

    private int update(Connection conn, boolean closeConn, String sql, Object... params) throws DataAccessException {
        Validator.notNull(conn, "Connection object must not be null");
        Validator.notEmpty(sql, "sql must not be null");

        if (logger.isDebugEnabled()) {
            logger.debug("Executing SQL update [" + sql + "]");
        }

        PreparedStatement stmt = null;
        try {
            stmt = preparedStatement(conn, sql);
            this.fillStatement(stmt, params);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            logger.debug("update caught exception", e);
            throw new DataAccessException("update caught exception", e);
        } finally {
            DbUtils.closeQuietly(stmt);
            if(closeConn) {
                DbUtils.closeQuietly(conn);
            }
        }
    }

}
