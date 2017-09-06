package atlas.util;

import org.junit.Test;

/**
 * @author Ricky Fung
 */
public class AssertTest {

    @Test
    public void testIsTrue() {

        boolean success = true;
        Validator.isTrue(success);

        success = false;
        try {
            Validator.isTrue(success);
        } catch (Exception e) {

        }
    }

    @Test
    public void testIsNull() {

        String str = null;
        Validator.isNull(str);

        str = "";
        try {
            Validator.isNull(str);
        } catch (Exception e) {

        }
    }

    @Test
    public void testNotNull() {

        String str = "";
        Validator.notNull(str);

        str = null;
        try {
            Validator.notNull(str);
        } catch (Exception e) {

        }
    }

    @Test
    public void testNotEmpty() {

        String str = "aa";
        Validator.notEmpty(str);

        str = "";
        try {
            Validator.notEmpty(str);
        } catch (Exception e) {

        }
    }

    @Test
    public void testNotBlank() {

        String str = "aa";
        Validator.notBlank(str);

        str = " ";
        try {
            Validator.notBlank(str);
        } catch (Exception e) {

        }
    }
}
