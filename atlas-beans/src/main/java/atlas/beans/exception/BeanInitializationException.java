package atlas.beans.exception;

import java.lang.reflect.Constructor;

/**
 * @author Ricky Fung
 */
public class BeanInitializationException extends BeanException {
    private Class<?> beanClass;
    private Constructor<?> constructor;

    public BeanInitializationException(Class<?> beanClass, String msg) {
        this((Class)beanClass, msg, (Throwable)null);
    }

    public BeanInitializationException(Class<?> beanClass, String msg, Throwable cause) {
        super("Failed to instantiate [" + beanClass.getName() + "]: " + msg, cause);
        this.beanClass = beanClass;
    }

    public BeanInitializationException(Constructor<?> constructor, String msg, Throwable cause) {
        super("Failed to instantiate [" + constructor.getDeclaringClass().getName() + "]: " + msg, cause);
        this.beanClass = constructor.getDeclaringClass();
        this.constructor = constructor;
    }
}
