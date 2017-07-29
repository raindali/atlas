package atlas.jdbc.mapper;

import atlas.jdbc.RowMapper;
import atlas.jdbc.util.MapperUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class BeanMapper<T> implements RowMapper<T> {
    private Class<? extends T> requiredType;

    public BeanMapper(Class<? extends T> requiredType){
        this.requiredType = requiredType;
    }

    @Override
    public T mapRow(ResultSet rs) throws SQLException {
        return MapperUtils.toBean(rs, requiredType);
    }
}
