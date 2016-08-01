package mvUtils.display;

import javax.swing.*;
import java.awt.*;

/**
 * User: M Viswanathan
 * Date: 02-Mar-16
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleDialog {
    static public void showMessage(Component parentComponent, String title, String msg) {
        JOptionPane op = new JOptionPane(msg, JOptionPane.INFORMATION_MESSAGE);
        JDialog dlg = op.createDialog(parentComponent, title);
//        if (parentComponent != null)
            dlg.setLocationRelativeTo(parentComponent);
//        else
//            dlg.setLocation(50, 50);
        dlg.toFront();
        dlg.setVisible(true);
    }

    static public void showError(Component parentComponent, String title, String msg) {
        JOptionPane op = new JOptionPane(msg, JOptionPane.ERROR_MESSAGE);
        JDialog dlg = op.createDialog(parentComponent, title);
//        if (parentComponent != null)
            dlg.setLocationRelativeTo(parentComponent);
//        else
//            dlg.setLocation(50, 50);
        dlg.toFront();
        dlg.setVisible(true);
    }

     static public int decide(Component parentComponent, String title, String msg) {
        return decide(parentComponent, title, msg,  true);
    }

    static public int decide(Component parentComponent, String title, String msg, boolean defaultOption) {
        String[] options = {UIManager.getString("OptionPane.yesButtonText"),
                UIManager.getString("OptionPane.noButtonText")};
        String defaultOptionString = (defaultOption) ? options[0] : options[1];
        return JOptionPane.showOptionDialog(parentComponent, msg, title, JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, defaultOptionString);
    }

    static public boolean decide(InputControl inputControl, String title, String msg, int forTime) {
        OneParameterDialog dlg = new OneParameterDialog(inputControl, title, "YES", "NO", forTime);
        dlg.setValue(msg);
        Component parentComponent = inputControl.parent();
        if (parentComponent != null)
            dlg.setLocationRelativeTo(parentComponent);
        else
            dlg.setLocation(50, 50);
        dlg.setVisible(true);
        double resp = dlg.getVal();
        if (resp == JOptionPane.YES_OPTION)
            return true;
        else
            return false;
    }
}
