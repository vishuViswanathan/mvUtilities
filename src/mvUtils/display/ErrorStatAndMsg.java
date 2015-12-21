package mvUtils.display;

/**
 * Created by IntelliJ IDEA.
 * User: M Viswanathan
 * Date: 15-May-15
 * Time: 10:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class ErrorStatAndMsg {
    static final public String nlSpace = "\n   ";
    public boolean inError = false;
    public String msg;

    public ErrorStatAndMsg() {
        msg = "";
        inError = false;
    }
    public ErrorStatAndMsg(boolean isError, String msg) {
        this.inError = isError;
        this.msg = msg;
    }

    public ErrorStatAndMsg add(ErrorStatAndMsg errorStat) {
        if (errorStat.inError)
            addErrorMsg(errorStat.msg);
        return this;
    }

    public ErrorStatAndMsg addErrorMsg(String addMsg) {
        if (msg.length() > 0)
            msg += " and " + addMsg;
        else
            msg = addMsg;
        inError |= true;
        return this;
    }
}
