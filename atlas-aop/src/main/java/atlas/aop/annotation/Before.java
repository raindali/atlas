package atlas.aop.annotation;

import java.lang.annotation.*;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface Before {
    String value();
}
