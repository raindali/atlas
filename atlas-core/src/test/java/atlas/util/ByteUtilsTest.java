package atlas.util;

import org.junit.*;

/**
 * @author Ricky Fung
 */
public class ByteUtilsTest {

    @Test
    public void testShort() {

        short i = 100;
        byte[] data = ByteUtils.shortToByteArray(i);
        org.junit.Assert.assertEquals(2, data.length);

        short num = ByteUtils.byteArrayToShort(data);
        org.junit.Assert.assertEquals(i, num);

        i = 1<<10;
        data = ByteUtils.shortToByteArray(i);
        org.junit.Assert.assertEquals(2, data.length);

        num = ByteUtils.byteArrayToShort(data);
        org.junit.Assert.assertEquals(i, num);
    }

    @Test
    public void testInt() {

        int i = 100;
        byte[] data = ByteUtils.intToByteArray(i);
        org.junit.Assert.assertEquals(4, data.length);

        int num = ByteUtils.byteArrayToInt(data);
        org.junit.Assert.assertEquals(i, num);

        i = 1<<20;
        data = ByteUtils.intToByteArray(i);
        org.junit.Assert.assertEquals(4, data.length);

        num = ByteUtils.byteArrayToInt(data);
        org.junit.Assert.assertEquals(i, num);

        num = ByteUtils.byteArrayToInt(data, 0, 4);
        org.junit.Assert.assertEquals(i, num);
    }

    @Test
    public void testLong() {

        long i = 100;
        byte[] data = ByteUtils.longToByteArray(i);
        org.junit.Assert.assertEquals(8, data.length);

        long num = ByteUtils.byteArrayToLong(data);
        org.junit.Assert.assertEquals(i, num);

        i = 1<<40;
        data = ByteUtils.longToByteArray(i);
        org.junit.Assert.assertEquals(8, data.length);

        num = ByteUtils.byteArrayToLong(data);
        org.junit.Assert.assertEquals(i, num);
    }
}
