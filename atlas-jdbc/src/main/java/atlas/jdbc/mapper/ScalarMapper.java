package atlas.jdbc.mapper;

import atlas.jdbc.core.RowMapper;
import atlas.util.StringUtils;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class ScalarMapper<T> implements RowMapper<T> {
    private int columnIndex;
    private String columnName;

    public ScalarMapper(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public ScalarMapper(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public T mapRow(ResultSet rs) throws SQLException {
        if (rs!=null && rs.next()) {
            if(StringUtils.isNotBlank(columnName)){
                return (T) rs.getObject(columnName);
            } else {
                return (T) rs.getObject(columnIndex);
            }
        }
        return null;
    }
}
