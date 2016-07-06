package mvUtils.forTesting;

/**
 * User: M Viswanathan
 * Date: 06-Jun-16
 * Time: 3:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void wasteTime(int milliseconds) {
        long sleepTime = milliseconds*1000000L; // convert to nanoseconds
        long startTime = System.nanoTime();
        while ((System.nanoTime() - startTime) < sleepTime) {}
    }
}
