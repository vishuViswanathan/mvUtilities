package mvUtils.display;

/**
 * User: M Viswanathan
 * Date: 14-Oct-16
 * Time: 12:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataWithStatus<T> {
    private T value;
    DataStat.Status status = DataStat.Status.OK;
    public boolean valid = false;
    public String errorMessage = "";
    public String infoMessage = "";

    public void setValue(T value) {
        this.value = value;
        valid = (value != null);
        if (valid) status = DataStat.Status.OK;
    }

    public void setErrorMsg(String msg) {
        errorMessage = msg;
        status = DataStat.Status.WithErrorMsg;
        valid = false;
    }

    public void setValue(T value, String infoMsg) {
        infoMessage = infoMsg;
        this.value = value;
        status = DataStat.Status.WithInfoMsg;
        valid = (value != null);
    }

    public DataStat.Status getStatus() {
        return status;
    }

    public T getValue() {
        return value;
    }

    public static void main(String[] args) {
        DataWithStatus<String> stringDat = new DataWithStatus<>();
        System.out.println("" + stringDat.valid);
        stringDat.setValue("There is data");
        if (stringDat.valid)
            System.out.println("the data = " + stringDat.getValue() );
        DataWithStatus<Double> doubleDat = new DataWithStatus<>();
        System.out.println("" + doubleDat.valid);
        doubleDat.setValue(45.55);
        if (stringDat.valid)
            System.out.println("the data = " + doubleDat.getValue() );
    }
}
