package atlas.jdbc.mapper;

import atlas.jdbc.core.RowMapper;
import atlas.jdbc.util.MapperUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class ArrayMapper implements RowMapper<Object[]> {

    @Override
    public Object[] mapRow(ResultSet rs) throws SQLException {
        return MapperUtils.toArray(rs);
    }
}
