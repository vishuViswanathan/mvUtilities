package mvUtils.display;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: M Viswanathan
 * Date: 12/10/12
 * Time: 12:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class XLTextField extends JTextField implements XLcellData {
    boolean bBold = false;
    public XLTextField(String text, int columns) {
        super(text, columns);
        setDefaultDisabledColor();
    }

    public XLTextField(String text) {
        super(text);
        setDefaultDisabledColor();
    }

    void setDefaultDisabledColor() {
        setDisabledTextColor(Color.BLUE);
    }

    public ValueForExcel getValueForExcel() {
        return new ValueForExcel(bBold, getText());
    }

    public String getFmtStr() {
        return "";
    }
}
