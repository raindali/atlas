package atlas.aop;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public interface Interceptor {
    void intercept(Invocation inv, InterceptorChain chain);
}
