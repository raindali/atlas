package atlas.aop;

import java.lang.reflect.InvocationHandler;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public interface ProxyFactory {

    Class<?> createProxy(Class<?>[] interfaces, ClassLoader classLoader, InvocationHandler handler);
}
