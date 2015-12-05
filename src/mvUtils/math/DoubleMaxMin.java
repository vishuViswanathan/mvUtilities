package mvUtils.math;

/**
 * class DoubleMaxMin
 * keeps track of maximum and minimum value introduced
 * Even if max and min are directly set, they are checked
 * for their relative values and interchanged if required
 */

public class DoubleMaxMin
        extends DoubleRange {

    public DoubleMaxMin() {
        reset();
    }

    public DoubleMaxMin(double min, double max) {
        if (max > min) {
            this.min = min;
            this.max = max;
        } else {
            this.min = max;
            this.max = min;
        }
    }

    public DoubleMaxMin(DoubleRange range) {
        this(range.min, range.max);
    }

    public int takeNewValue(double value) {
        int retVal = 0;
        if (value > max) {
            max = value;
            retVal |= ITISMAX;
        }

        if (value < min) {
            min = value;
            retVal |= ITISMIN;
        }
        return retVal;
    }

    public void takeMaxValue(double value) {
        max = value;
        if (value < min) min = value;
    }

    public void takeMinValue(double value) {
        min = value;
        if (value > max) max = value;
    }

    public double mean() {
        return (max + min) / 2;
    }

    public void reset() {
        max = Double.NEGATIVE_INFINITY;
        min = Double.POSITIVE_INFINITY;
    }

}