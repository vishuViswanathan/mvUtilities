package mvUtils.jsp;

/**
 * User: M Viswanathan
 * Date: 02-Sep-16
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */
public interface JSPObject {
    boolean isDataCollected();
    boolean collectData(JSPConnection jspConnection);
    void unCollectData();
}
