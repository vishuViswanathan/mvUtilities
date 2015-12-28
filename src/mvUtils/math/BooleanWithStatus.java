package mvUtils.math;

import mvUtils.display.StatusWithMessage;

/**
 * Created by IntelliJ IDEA.
 * User: M Viswanathan
 * Date: 21-Dec-15
 * Time: 3:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class BooleanWithStatus extends StatusWithMessage {
    boolean data = false;

    public BooleanWithStatus(boolean value) {
        setValue(value);
    }

    public void setValue(boolean value) {
        this.data = value;
        dataStat = StatusWithMessage.DataStat.OK;
    }

    public void setValue(boolean value, String infoMsg) {
        if (addInfoMessage(infoMsg))
            this.data = value;
    }

    public boolean getValue() {
        return data;
    }
}
