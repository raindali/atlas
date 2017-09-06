package atlas.beans.util;

import atlas.beans.exception.BeanException;
import atlas.beans.exception.BeanInitializationException;
import atlas.util.Validator;
import atlas.util.ReflectionUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Ricky Fung
 */
public abstract class BeanUtils {

    private static final ConcurrentMap<Class<?>, PropertyDescriptor[]> classCacheToUse = new ConcurrentHashMap(64);

    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws BeanException {
        PropertyDescriptor[] results = classCacheToUse.get(clazz);
        if(results!=null) {
            return results;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            results = beanInfo.getPropertyDescriptors();
            PropertyDescriptor[] existing = classCacheToUse.putIfAbsent(clazz, results);
            return existing != null?existing:results;
        } catch (IntrospectionException e) {
            throw new BeanException("get bean info caught error", e);
        }
    }


    public static <T> T instantiate(Class<T> clazz) throws BeanInitializationException {
        Validator.notNull(clazz, "Class must not be null");
        if(clazz.isInterface()) {
            throw new BeanInitializationException(clazz, "Specified class is an interface");
        } else {
            try {
                return clazz.newInstance();
            } catch (InstantiationException var2) {
                throw new BeanInitializationException(clazz, "Is it an abstract class?", var2);
            } catch (IllegalAccessException var3) {
                throw new BeanInitializationException(clazz, "Is the constructor accessible?", var3);
            }
        }
    }

    public static <T> T instantiateClass(Class<T> clazz) throws BeanInitializationException {
        Validator.notNull(clazz, "Class must not be null");
        if(clazz.isInterface()) {
            throw new BeanInitializationException(clazz, "Specified class is an interface");
        } else {
            try {
                return instantiateClass(clazz.getDeclaredConstructor(new Class[0]), new Object[0]);
            } catch (NoSuchMethodException e) {
                throw new BeanInitializationException(clazz, "No default constructor found", e);
            }
        }
    }

    public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws BeanInitializationException {
        Validator.notNull(ctor, "Constructor must not be null");
        try {
            ReflectionUtils.makeAccessible(ctor);
            return ctor.newInstance(args);
        } catch (InstantiationException e) {
            throw new BeanInitializationException(ctor, "Is it an abstract class?", e);
        } catch (IllegalAccessException e) {
            throw new BeanInitializationException(ctor, "Is the constructor accessible?", e);
        } catch (IllegalArgumentException e) {
            throw new BeanInitializationException(ctor, "Illegal arguments for constructor", e);
        } catch (InvocationTargetException e) {
            throw new BeanInitializationException(ctor, "Constructor threw exception", e.getTargetException());
        }
    }
}
