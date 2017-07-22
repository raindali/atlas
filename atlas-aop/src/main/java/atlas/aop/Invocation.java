package atlas.aop;

import java.lang.reflect.Method;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class Invocation {
    private Object target;
    private Method method;
    private Object[] args;
    private Object result;

    public Invocation() {
    }

    public Invocation(Object target, Method method, Object[] args) {
        this.target = target;
        this.method = method;
        this.args = args;
    }

    public Object getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getResult() {
        return result;
    }
}
