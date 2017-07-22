package atlas.aop;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public interface InterceptorChain {

    void proceed(Invocation inv);
}
