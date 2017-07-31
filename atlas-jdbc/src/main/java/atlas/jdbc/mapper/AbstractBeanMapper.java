package atlas.jdbc.mapper;

import atlas.beans.exception.BeanException;
import atlas.beans.util.BeanUtils;
import atlas.jdbc.annotation.Column;
import atlas.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class AbstractBeanMapper<T> {

    protected static final Map<Class<?>, Object> primitiveDefaults = new HashMap<>();

    protected Class<T> requiredType;

    /** Map of the fields we provide mapping for */
    protected Map<String, PropertyDescriptor> mappedFields;
    protected Map<String, String> columnToPropertyMap;

    static {
        primitiveDefaults.put(Integer.TYPE, Integer.valueOf(0));
        primitiveDefaults.put(Short.TYPE, Short.valueOf((short) 0));
        primitiveDefaults.put(Byte.TYPE, Byte.valueOf((byte) 0));
        primitiveDefaults.put(Float.TYPE, Float.valueOf(0f));
        primitiveDefaults.put(Double.TYPE, Double.valueOf(0d));
        primitiveDefaults.put(Long.TYPE, Long.valueOf(0L));
        primitiveDefaults.put(Boolean.TYPE, Boolean.FALSE);
        primitiveDefaults.put(Character.TYPE, Character.valueOf((char) 0));
    }

    public AbstractBeanMapper(Class<T> requiredType){
        this.init(requiredType);
    }

    protected T rsToBean(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }
        T obj = BeanUtils.instantiateClass(this.requiredType);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        for (int col = 1; col <= columnCount; col++) {
            String columnName = rsmd.getColumnLabel(col);
            if (null == columnName || 0 == columnName.length()) {
                columnName = rsmd.getColumnName(col);
            }

            String field = lowerCaseName(columnName.replaceAll(" ", ""));
            PropertyDescriptor pd = this.mappedFields.get(field);
            if (pd != null) {
                Object value = null;
                value = getColumnValue(rs, col, pd.getPropertyType());
                if (value == null && pd.getPropertyType().isPrimitive()) {
                    value = primitiveDefaults.get(pd.getPropertyType());
                }
                setValueToBean(obj, pd, value);
            }
        }
        return obj;
    }

    protected void init(Class<T> requiredType) {
        this.requiredType = requiredType;
        this.columnToPropertyMap = getColumnToProperty(requiredType);
        this.mappedFields = new HashMap<>();
        try {
            PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(requiredType);
            for (PropertyDescriptor pd : pds) {
                if (pd.getWriteMethod() != null) {
                    this.mappedFields.put(lowerCaseName(pd.getName()), pd);
                    String underscoredName = underscoreName(pd.getName());
                    if (!lowerCaseName(pd.getName()).equals(underscoredName)) {
                        this.mappedFields.put(underscoredName, pd);
                    }
                }
            }
        } catch (BeanException e) {
            throw new RuntimeException("can not get bean PropertyDescriptor:"+requiredType, e);
        }
    }

    protected Map<String,String> getColumnToProperty(Class<?> type) {
        Map<String, String> columnToPropertyMap = new HashMap<>();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields){
            String columnName = null;
            if(field.isAnnotationPresent(Column.class)){
                Column col = field.getAnnotation(Column.class);
                if(col!=null){
                    columnName = col.name();
                }
            }
            if(columnName==null || "".equals(columnName)){
                columnName = field.getName();
            }
            columnToPropertyMap.put(columnName, field.getName());
        }
        return columnToPropertyMap;
    }

    protected String underscoreName(String name) {
        if (StringUtils.isEmpty(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(lowerCaseName(name.substring(0, 1)));
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            String slc = lowerCaseName(s);
            if (!s.equals(slc)) {
                result.append("_").append(slc);
            } else {
                result.append(s);
            }
        }
        return result.toString();
    }

    protected String lowerCaseName(String name) {
        return name.toLowerCase(Locale.US);
    }

    private static void setValueToBean(Object target, PropertyDescriptor prop, Object value)
            throws SQLException {

        Method setter = prop.getWriteMethod();
        if (setter == null) {
            return;
        }
        Class<?>[] params = setter.getParameterTypes();
        try {
            // convert types for some popular ones
            if (value instanceof Date) {
                final String targetType = params[0].getName();
                if ("java.sql.Date".equals(targetType)) {
                    value = new java.sql.Date(((Date) value).getTime());
                } else if ("java.sql.Timestamp".equals(targetType)) {
                    Timestamp tsValue = (Timestamp) value;
                    int nanos = tsValue.getNanos();
                    value = new Timestamp(tsValue.getTime());
                    ((Timestamp) value).setNanos(nanos);
                }
            }

            // Don't call setter if the value object isn't the right type
            if (isCompatibleType(value, params[0])) {
                setter.invoke(target, new Object[]{value});
            } else {
                throw new SQLException(
                        "Cannot set " + prop.getName() + ": incompatible types, cannot convert "
                                + value.getClass().getName() + " to " + params[0].getName());
            }
        } catch (IllegalArgumentException e) {
            throw new SQLException(
                    "Cannot set " + prop.getName() + ": " + e.getMessage());

        } catch (IllegalAccessException e) {
            throw new SQLException(
                    "Cannot set " + prop.getName() + ": " + e.getMessage());

        } catch (InvocationTargetException e) {
            throw new SQLException(
                    "Cannot set " + prop.getName() + ": " + e.getMessage());
        }
    }

    private static boolean isCompatibleType(Object value, Class<?> type) {
        // Do object check first, then primitives
        if (value == null || type.isInstance(value)) {
            return true;

        } else if (type.equals(Integer.TYPE) && value instanceof Integer) {
            return true;

        } else if (type.equals(Long.TYPE) && value instanceof Long) {
            return true;

        } else if (type.equals(Double.TYPE) && value instanceof Double) {
            return true;

        } else if (type.equals(Float.TYPE) && value instanceof Float) {
            return true;

        } else if (type.equals(Short.TYPE) && value instanceof Short) {
            return true;

        } else if (type.equals(Byte.TYPE) && value instanceof Byte) {
            return true;

        } else if (type.equals(Character.TYPE) && value instanceof Character) {
            return true;

        } else if (type.equals(Boolean.TYPE) && value instanceof Boolean) {
            return true;
        }
        return false;
    }

    protected Object getColumnValue(ResultSet rs, int index, Class<?> propType)
            throws SQLException {

        if ( !propType.isPrimitive() && rs.getObject(index) == null ) {
            return null;
        }

        if (propType.equals(String.class)) {
            return rs.getString(index);
        } else if (
                propType.equals(Integer.TYPE) || propType.equals(Integer.class)) {
            return Integer.valueOf(rs.getInt(index));
        } else if (
                propType.equals(Boolean.TYPE) || propType.equals(Boolean.class)) {
            return Boolean.valueOf(rs.getBoolean(index));
        } else if (propType.equals(Long.TYPE) || propType.equals(Long.class)) {
            return Long.valueOf(rs.getLong(index));
        } else if (
                propType.equals(Double.TYPE) || propType.equals(Double.class)) {
            return Double.valueOf(rs.getDouble(index));
        } else if (
                propType.equals(Float.TYPE) || propType.equals(Float.class)) {
            return Float.valueOf(rs.getFloat(index));
        } else if (
                propType.equals(Short.TYPE) || propType.equals(Short.class)) {
            return Short.valueOf(rs.getShort(index));
        } else if (propType.equals(Byte.TYPE) || propType.equals(Byte.class)) {
            return Byte.valueOf(rs.getByte(index));
        } else if (propType.equals(Timestamp.class)) {
            return rs.getTimestamp(index);
        } else {
            return rs.getObject(index);
        }

    }
}
