package atlas.jdbc.mapper;

import atlas.jdbc.core.RowMapper;
import atlas.jdbc.util.MapperUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class MapMapper implements RowMapper<Map<String, Object>> {

    @Override
    public Map<String, Object> mapRow(ResultSet rs) throws SQLException {
        return MapperUtils.toMap(rs);
    }
}
