package atlas.jdbc.support;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class BeanPropertiesHolder {
    private Map<String, String> columnToPropertyOverrides;
    private PropertyDescriptor[] props;

    public BeanPropertiesHolder() {}

    public PropertyDescriptor[] getProps() {
        return props;
    }

    public void setProps(PropertyDescriptor[] props) {
        this.props = props;
    }

    public Map<String, String> getColumnToPropertyOverrides() {
        return columnToPropertyOverrides;
    }

    public void setColumnToPropertyOverrides(Map<String, String> columnToPropertyOverrides) {
        this.columnToPropertyOverrides = columnToPropertyOverrides;
    }
}
