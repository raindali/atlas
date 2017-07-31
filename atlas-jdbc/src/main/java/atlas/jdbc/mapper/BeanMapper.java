package atlas.jdbc.mapper;

import atlas.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class BeanMapper<T> extends AbstractBeanMapper<T> implements RowMapper<T> {

    public BeanMapper(Class<T> requiredType){
        super(requiredType);
    }

    @Override
    public T mapRow(ResultSet rs) throws SQLException {
        return rsToBean(rs);
    }

}
