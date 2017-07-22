package atlas.aop;

import net.sf.cglib.proxy.Enhancer;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class AopManager {
    private final Enhancer enhancer = new Enhancer();

    private AopManager(){
    }

    public static AopManager getMgr(){
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final AopManager INSTANCE = new AopManager();
    }
}
