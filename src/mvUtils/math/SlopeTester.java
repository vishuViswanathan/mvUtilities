package mvUtils.math;

import junit.framework.TestCase;

/**
 * Created by M Viswanathan on 05 Dec 2015
 */
public class SlopeTester extends TestCase{
    private double x1, x2, y1, y2, len, slope;

    public void setUp() {
        x1 = 2.0;
        x2 = 5.0;
        y1 = 5.0;
        y2 = 9.0;
        len = 5.0;
        slope = 4.0 / 3.0;
    }

    public void testSlope() {
        LineMv theLine = new LineMv(x1, y1, x2, y2);
        assertEquals("The Slope is Wrong" , slope + 0.1, theLine.slope, 0.01);
    }
}
