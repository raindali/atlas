package atlas.util;

import org.junit.Test;

/**
 * @author Ricky Fung
 */
public class AssertTest {

    @Test
    public void testIsTrue() {

        boolean success = true;
        Assert.isTrue(success);

        success = false;
        try {
            Assert.isTrue(success);
        } catch (Exception e) {

        }
    }

    @Test
    public void testIsNull() {

        String str = null;
        Assert.isNull(str);

        str = "";
        try {
            Assert.isNull(str);
        } catch (Exception e) {

        }
    }

    @Test
    public void testNotNull() {

        String str = "";
        Assert.notNull(str);

        str = null;
        try {
            Assert.notNull(str);
        } catch (Exception e) {

        }
    }

    @Test
    public void testNotEmpty() {

        String str = "aa";
        Assert.notEmpty(str);

        str = "";
        try {
            Assert.notEmpty(str);
        } catch (Exception e) {

        }
    }

    @Test
    public void testNotBlank() {

        String str = "aa";
        Assert.notBlank(str);

        str = " ";
        try {
            Assert.notBlank(str);
        } catch (Exception e) {

        }
    }
}
