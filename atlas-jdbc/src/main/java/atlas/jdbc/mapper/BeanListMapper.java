package atlas.jdbc.mapper;

import atlas.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class BeanListMapper<T> extends AbstractBeanMapper<T> implements RowMapper<List<T>> {

    public BeanListMapper(Class<T> requiredType){
        super(requiredType);
    }

    @Override
    public List<T> mapRow(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return Collections.emptyList();
        }

        List<T> results = new ArrayList<T>();
        do {
            results.add(rsToBean(rs));
        } while (rs.next());

        return results;
    }
}
