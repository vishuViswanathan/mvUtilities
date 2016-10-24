package mvUtils.math;

/**
 * User: M Viswanathan
 * Date: 21-Oct-16
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class CheckInRange {
    public boolean inRange = false;
    public boolean aboveMax; // if not it is below Min
    public double limitVal;  // the upper or the lower limit which was violated

    public CheckInRange() {
        inRange = true;
    }

    public void maxViolated(double limitVal) {
        this.limitVal = limitVal;
        inRange = false;
        aboveMax = true;
    }

    public void minViolated(double limitVal) {
        this.limitVal = limitVal;
        inRange = false;
        aboveMax = false;
    }
}
