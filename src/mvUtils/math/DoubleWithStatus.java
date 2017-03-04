package mvUtils.math;

import mvUtils.display.DataStat;
import mvUtils.display.StatusWithMessage;

/**
 * Created by IntelliJ IDEA.
 * User: M Viswanathan
 * Date: 21-Dec-15
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class DoubleWithStatus extends StatusWithMessage {
    double data;

    public DoubleWithStatus(double value) {
        setValue(value);
    }

    public void setValue(double value) {
        this.data = value;
        dataStat = DataStat.Status.OK;
    }

    public void setValue(double value, String infoMsg) {
        if (addInfoMessage(infoMsg))
            this.data = value;
    }

    public double getValue() {
        return data;
    }
}
