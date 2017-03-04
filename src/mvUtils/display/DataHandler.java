package mvUtils.display;

/**
 * Created by IntelliJ IDEA.
 * User: M Viswanathan
 * Date: 14-May-15
 * Time: 4:03 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataHandler {
    ErrorStatAndMsg checkData();
    boolean saveData();
    void deleteData();
    void resetData();
    void cancel();
}
