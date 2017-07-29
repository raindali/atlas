package atlas.jdbc.mapper;

import atlas.jdbc.RowMapper;
import atlas.jdbc.util.MapperUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class BeanListMapper<T> implements RowMapper<List<T>> {
    private Class<? extends T> requiredType;

    public BeanListMapper(Class<? extends T> requiredType){
        this.requiredType = requiredType;
    }

    @Override
    public List<T> mapRow(ResultSet rs) throws SQLException {
        return MapperUtils.toBeanList(rs, requiredType);
    }
}
