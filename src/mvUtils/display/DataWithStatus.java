package mvUtils.display;

/**
 * User: M Viswanathan
 * Date: 14-Oct-16
 * Time: 12:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataWithStatus<T> extends StatusWithMessage{
    private T value;
    DataStat.Status dataStat = DataStat.Status.OK;
    public boolean valid = false;
    public String errorMessage = "";
    public String infoMessage = "";

    public DataWithStatus() {
        setValue(null);
    }

    public DataWithStatus (T value) {
        setValue(value);
    }

    public void setValue(T value) {
        this.value = value;
        valid = (value != null);
        if (valid)
            dataStat = DataStat.Status.OK;
        else
            setErrorMessage("Data is null");
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setErrorMsg(String msg) {
        errorMessage = msg;
        dataStat = DataStat.Status.WithErrorMsg;
        valid = false;
    }

    public void setInfoMsg(String msg) {
        infoMessage  = msg;
        dataStat = DataStat.Status.WithInfoMsg;
        valid = (value != null);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getInfoMessage() {
        return infoMessage;
    }

    public void setValue(T value, String infoMsg) {
        infoMessage = infoMsg;
        this.value = value;
        dataStat = DataStat.Status.WithInfoMsg;
        valid = (value != null);
    }

    public DataStat.Status getStatus() {
        return dataStat;
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
        DataWithStatus<Double> doubleValDat = new DataWithStatus<>(76.33);
        System.out.println("" + doubleValDat.valid);
        System.out.println("the data = " + doubleValDat.getValue() );
    }
}
