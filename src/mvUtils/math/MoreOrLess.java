package mvUtils.math;

import java.util.GregorianCalendar;

/**
 * User: M Viswanathan
 * Date: 11-Jul-16
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class  MoreOrLess {
    public enum CompareResult {
        DATA1MORE,
        EQUALS,
        DATA1LESS}

    static double defaultTolerance = 1e-9;

    public static CompareResult compare(int data1, int data2) {
        CompareResult retVal = CompareResult.EQUALS;
        if (data1 > data2)
            retVal = CompareResult.DATA1MORE;
        else if (data1 < data2)
            retVal = CompareResult.DATA1LESS;
        return retVal;
    }

    public static CompareResult compare(double data1, double data2, double tolerance) {
        CompareResult retVal = CompareResult.EQUALS;
        double oneMinusTwo = data1 - data2;
        if (Math.abs(oneMinusTwo) > tolerance) {
            if (oneMinusTwo > 0)
                retVal = CompareResult.DATA1MORE;
            else
                retVal = CompareResult.DATA1LESS;
        }
        return retVal;

    }

    public static CompareResult compare(double data1, double data2) {
        return compare(data1, data2, defaultTolerance);
    }

    public static CompareResult compare(GregorianCalendar data1, GregorianCalendar data2) {
        int dateCompare = data1.compareTo(data2);
        CompareResult retVal = CompareResult.EQUALS;
        if (dateCompare > 0)
            retVal = CompareResult.DATA1MORE;
        else if (dateCompare < 0)
            retVal = CompareResult.DATA1LESS;
        return retVal;
    }
}
