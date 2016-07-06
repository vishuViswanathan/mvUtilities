package mvUtils.display;

/**
 * Created by IntelliJ IDEA.
 * User: M Viswanathan
 * Date: 21-Dec-15
 * Time: 1:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatusWithMessage {
    public enum DataStat1 {OK, WithInfoMsg, WithErrorMsg}
    public enum DataStat {OK, WithInfoMsg, WithErrorMsg;}
    protected DataStat dataStat = DataStat.OK;
    String msgSeparator;
    String errMsg = "";
    String infoMsg = "";

    public StatusWithMessage() {
        this(", ");
    }

    public StatusWithMessage(String msgSeparator) {
        this.msgSeparator = msgSeparator;
    }

    public DataStat getDataStatus() {
        return dataStat;
    }
    public String getErrorMessage() {
        return errMsg;
    }

    public String getInfoMessage() {
        return infoMsg;
    }

    private String addMsg(String toMsg, String msg) {
        toMsg += (((toMsg.length() > 0) ? msgSeparator : "") + msg);
        return toMsg;
    }

    public void setErrorMessage(String msg) {
        addErrorMessage(msg);
    }

    public void addErrorMessage(String msg) {
        errMsg = addMsg (errMsg, msg);
        dataStat = DataStat.WithErrorMsg;
    }

    public boolean setInfoMessage(String msg) {
        return addInfoMessage(msg);
    }

    public boolean addInfoMessage(String msg) {
        if (dataStat != DataStat.WithErrorMsg) {
            infoMsg = addMsg(infoMsg, msg);
            dataStat = DataStat.WithInfoMsg;
            return true;
        }
        else
            return false;
    }
}
