package atlas.jdbc.core;

import atlas.jdbc.exception.JdbcConnectionException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public abstract class JdbcAccessor {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    protected PreparedStatement preparedStatement(Connection conn, String sql, int autoGeneratedKeys) throws SQLException {
        return conn.prepareStatement(sql, autoGeneratedKeys);
    }

    protected PreparedStatement preparedStatement(Connection conn, String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    protected void fillStatement(PreparedStatement stmt, Object... params)
            throws SQLException {

        if(params!=null && params.length>0){    //设置参数
            for(int i=0; i<params.length; i++){
                int sqlType = Types.VARCHAR;
                if (params[i] != null) {
                    stmt.setObject(i + 1, params[i]);
                } else {
                    stmt.setNull(i + 1, sqlType);
                }
            }
        }
    }

    protected Connection prepareConnection() {
        if (this.getDataSource() == null) {
            throw new JdbcConnectionException("No DataSource specified");
        }
        try {
            return this.getDataSource().getConnection();
        } catch (SQLException e) {
            throw new JdbcConnectionException("Could not get JDBC Connection", e);
        }
    }

}