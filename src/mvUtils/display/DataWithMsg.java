package mvUtils.display;

/**
 * Created by IntelliJ IDEA.
 * User: M Viswanathan
 * Date: 14-Dec-15
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataWithMsg {
    public enum DataStat {OK, WithDebugMsg, WithInfoMsg, WithErrorMsg;}
    public double doubleValue = 0.0D / 0.0;
    public float floatValue = 0.0f / 0.0f;
    public boolean booleanValue = false;
    public String stringValue = "";
    DataStat status = DataStat.OK;
    public boolean valid = false;
    public String errorMessage = "";
    public String infoMessage = "";

    public DataWithMsg() {
    }

    public void setErrorMsg(String msg) {
        errorMessage = msg;
        status = DataStat.WithErrorMsg;
        valid = false;
    }

    public void setInfoMsg(String msg) {
        infoMessage = msg;
        status = DataStat.WithInfoMsg;
        valid = true;
    }

    public DataStat getStatus() {
        return status;
    }

    public void setData(double data) {
        doubleValue = data;
        status = DataStat.OK;
        valid = true;
    }

    public void setData(float data) {
        floatValue = data;
        status = DataStat.OK;
        valid = true;
    }

    public void setData(boolean data) {
        booleanValue = data;
        status = DataStat.OK;
        valid = true;
    }

    public void setData(double data, String infoMessage) {
        doubleValue = data;
        this.infoMessage = infoMessage;
        status = DataStat.WithInfoMsg;
        valid = true;
    }

    public void setData(float data, String infoMessage) {
        floatValue = data;
        this.infoMessage = infoMessage;
        status = DataStat.WithInfoMsg;
        valid = true;
    }

    public void setData(boolean data, String infoMessage) {
        booleanValue = data;
        this.infoMessage = infoMessage;
        status = DataStat.WithInfoMsg;
        valid = true;
    }
}
