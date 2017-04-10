package mvUtils.math;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * User: M Viswanathan
 * Date: 06-Mar-17
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class BigDecimalExample1 {
    public static void main(String[] args) {
        double pi = 3.14159265358979323846;
        System.out.println(pi);
        BigDecimal bigPi = new BigDecimal("3.14159265358979323846");
        System.out.println(bigPi);
        System.out.println(bigPi.multiply(new BigDecimal(1.4)));
        BigDecimal result =  bigPi.multiply(new BigDecimal(1.4));
        result = result.setScale(10, BigDecimal.ROUND_HALF_UP);
        System.out.println(result);
        double dResult = result.doubleValue();
        System.out.println("double val = " + dResult);
        MathContext mc = new MathContext(14, RoundingMode.HALF_UP);
        System.out.println((new BigDecimal( 1.0 + 1.0/3000000, mc)).doubleValue());
        BigDecimal bd = new BigDecimal(Math.PI, mc);
        System.out.println(bd.multiply(bd));
        System.out.println((bd.multiply(bd)).doubleValue());
        System.out.println((bd.multiply(bd)).floatValue());

    }

}

