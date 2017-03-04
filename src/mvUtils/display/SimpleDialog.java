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
    static public void showMessage(Component parentComponent, String title, String msg, boolean bCopyable) {
        JOptionPane op;
        if (bCopyable) {
            JTextField tf = new JTextField(msg);
            tf.setEditable(false);
            op = new JOptionPane(tf, JOptionPane.INFORMATION_MESSAGE);
        }
        else
            op = new JOptionPane(msg, JOptionPane.INFORMATION_MESSAGE);
        JDialog dlg = op.createDialog(parentComponent, title);
        dlg.setLocationRelativeTo(parentComponent);
        dlg.toFront();
        dlg.setVisible(true);
    }

    static public void showMessage(String title, String msg, boolean bCopyable) {
        showMessage(null, title, msg, bCopyable);
    }

    static public void showMessage(Component parentComponent, String title, String msg) {
        showMessage(parentComponent, title, msg, false);
    }

    static public void showMessage(String title, String msg) {
        showMessage(null, title, msg);
    }

    static public void showError(String title, String msg) {
        showError(null, title, msg);
    }

    static public void showError(Component parentComponent, String title, String msg) {
        JOptionPane op = new JOptionPane(msg, JOptionPane.ERROR_MESSAGE);
        JDialog dlg = op.createDialog(parentComponent, title);
        dlg.setLocationRelativeTo(parentComponent);
        dlg.toFront();
        dlg.setVisible(true);
    }

    static public int decide(Component parentComponent, String title, String msg) {
        return decide(parentComponent, title, msg, true);
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
        dlg.setLocationRelativeTo(parentComponent);
        dlg.setVisible(true);
        double resp = dlg.getVal();
        return resp == JOptionPane.YES_OPTION;
    }

    public static void main(String[] args) {
        System.out.println("JOptionPane.YES_OPTION = "  + JOptionPane.YES_OPTION +
                ", JOptionPane.NO_OPTION = "  + JOptionPane.NO_OPTION);
        int response = decide(null, "test", "Yes or No");
        System.out.println("response with no defaultOption= " + response);
        response = decide(null, "test", "Yes or No", false);
        System.out.println("response with defaultOption as false = " + response);
        response = decide(null, "test", "Yes or No", true);
        System.out.println("response with defaultOption as true = " + response);
//        SimpleDialog.showMessage(null, "Copyable message", "This is copyable message", true);
//        SimpleDialog.showMessage(null, "Non-copyable message", "This is an NOT copyable message");
        System.exit(0);
    }
}