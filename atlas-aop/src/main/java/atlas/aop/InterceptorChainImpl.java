package atlas.aop;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class InterceptorChainImpl implements InterceptorChain {
    private int pos;
    private List<Interceptor> interceptors;

    public InterceptorChainImpl(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
        this.pos = 0;
    }

    @Override
    public void proceed(Invocation inv) {

        if(pos<interceptors.size()){
            Interceptor interceptor = interceptors.get(pos++);
            interceptor.intercept(inv, this);
        } else {

        }
    }
}