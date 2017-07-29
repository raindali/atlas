package atlas.jdbc.util;

import atlas.jdbc.annotation.Column;
import atlas.jdbc.support.BeanPropertiesHolder;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class MapperUtils {
    private static final int PROPERTY_NOT_FOUND = -1;

    private static final Map<Class<?>, Object> primitiveDefaults = new HashMap<>();

    private static final LoadingCache<Class<?>, BeanPropertiesHolder> holderLoadingCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(
                    new CacheLoader<Class<?>, BeanPropertiesHolder>() {
                        @Override
                        public BeanPropertiesHolder load(Class<?> type) throws Exception {
                            return getBeanPropertiesHolder(type);
                        }
                    });

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

    public static Object[] toArray(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();
        Object[] result = new Object[cols];
        for (int i = 0; i < cols; i++) {
            result[i] = rs.getObject(i + 1);
        }
        return result;
    }

    public static Map<String, Object> toMap(ResultSet rs) throws SQLException {
        if (!rs.next()) {
            return null;
        }
        Map<String, Object> result = new HashMap<String, Object>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();

        for (int i = 1; i <= cols; i++) {
            String columnName = rsmd.getColumnLabel(i);
            if (null == columnName || 0 == columnName.length()) {
                columnName = rsmd.getColumnName(i);
            }
            result.put(columnName, rs.getObject(i));
        }

        return result;
    }

    public static <T> T toBean(ResultSet rs, Class<? extends T> type) throws SQLException {
        if (!rs.next()) {
            return null;
        }

        BeanPropertiesHolder holder = null;
        try {
            holder = holderLoadingCache.get(type);
        } catch (ExecutionException e) {
            throw new IllegalArgumentException("caught exception when parse bean properties", e);
        }

        ResultSetMetaData rsmd = rs.getMetaData();
        int[] columnToProperty = mapColumnsToProperties(rsmd, holder.getProps(), holder.getColumnToPropertyOverrides());

        return createBean(rs, type, holder.getProps(), columnToProperty);
    }

    public static <T> List<T> toBeanList(ResultSet rs, Class<? extends T> type) throws SQLException {

        List<T> results = new ArrayList<T>();

        if (!rs.next()) {
            return results;
        }
        BeanPropertiesHolder holder = null;
        try {
            holder = holderLoadingCache.get(type);
        } catch (ExecutionException e) {
            throw new IllegalArgumentException("caught exception when parse bean properties", e);
        }

        ResultSetMetaData rsmd = rs.getMetaData();
        int[] columnToProperty = mapColumnsToProperties(rsmd, holder.getProps(), holder.getColumnToPropertyOverrides());

        do {
            results.add(createBean(rs, type, holder.getProps(), columnToProperty));
        } while (rs.next());

        return results;
    }

    private static <T> T createBean(ResultSet rs, Class<T> type,
                                    PropertyDescriptor[] props, int[] columnToProperty)
            throws SQLException {

        T bean = newInstance(type);
        for (int i = 1; i < columnToProperty.length; i++) {

            if (columnToProperty[i] == PROPERTY_NOT_FOUND) {
                continue;
            }

            PropertyDescriptor prop = props[columnToProperty[i]];
            Class<?> propType = prop.getPropertyType();

            Object value = null;
            if(propType != null) {
                value = processColumn(rs, i, propType);
                if (value == null && propType.isPrimitive()) {
                    value = primitiveDefaults.get(propType);
                }
                setValueToBean(bean, prop, value);
            }
        }
        return bean;
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

    protected static int[] mapColumnsToProperties(ResultSetMetaData rsmd,
                                                  PropertyDescriptor[] props,
                                                  Map<String, String> columnToPropertyOverrides) throws SQLException {

        int cols = rsmd.getColumnCount();
        int[] columnToProperty = new int[cols + 1];
        Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

        for (int col = 1; col <= cols; col++) {
            String columnName = rsmd.getColumnLabel(col);
            if (null == columnName || 0 == columnName.length()) {
                columnName = rsmd.getColumnName(col);
            }
            String propertyName = columnToPropertyOverrides.get(columnName);
            if (propertyName == null) {
                propertyName = columnName;
            }
            for (int i = 0; i < props.length; i++) {

                if (propertyName.equalsIgnoreCase(props[i].getName())) {
                    columnToProperty[col] = i;
                    break;
                }
            }
        }

        return columnToProperty;
    }

    private static BeanPropertiesHolder getBeanPropertiesHolder(Class<?> type) throws SQLException {
        BeanPropertiesHolder holder = new BeanPropertiesHolder();
        holder.setColumnToPropertyOverrides(getColumnToProperty(type));
        holder.setProps(getPropertyDescriptors(type));
        return holder;
    }

    private static PropertyDescriptor[] getPropertyDescriptors(Class<?> c)
            throws SQLException {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(c);

        } catch (IntrospectionException e) {
            throw new SQLException(
                    "Bean introspection failed: " + e.getMessage());
        }
        return beanInfo.getPropertyDescriptors();
    }

    protected static <T> T newInstance(Class<T> c) throws SQLException {
        try {
            return c.newInstance();
        } catch (InstantiationException e) {
            throw new SQLException(
                    "Cannot create " + c.getName() + ": " + e.getMessage());

        } catch (IllegalAccessException e) {
            throw new SQLException(
                    "Cannot create " + c.getName() + ": " + e.getMessage());
        }
    }

    protected static Object processColumn(ResultSet rs, int index, Class<?> propType)
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

    private static Map<String,String> getColumnToProperty(Class<?> type) {

        Map<String, String> columnToPropertyMap = new HashMap<String, String>();
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

}
