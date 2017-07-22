package atlas.util;

import java.util.UUID;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public abstract class UUIDUtils {

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getPureUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
