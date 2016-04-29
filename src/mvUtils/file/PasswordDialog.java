package mvUtils.file;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import mvUtils.display.FramedPanel;
import mvUtils.display.MultiPairColPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.*;

/**
 * User: M Viswanathan
 * Date: 25-Apr-16
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class PasswordDialog extends JDialog {
    JButton okButt = new JButton("OK");
    JButton cancel = new JButton("Cancel");
    JTextField nameField = new JTextField(15);
    JPasswordField passwordField = new JPasswordField(15);
    String name;
    String password;
    boolean allOK = false;
    int minLength = 6;
//    String regx = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.@#$%&])[^\\s]{" + minLength + ",}$";
    String regx = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])[^\\s]{" + minLength + ",}$";
    String passwordToolTip = "<html>Password must of min " + minLength + " characters length<p>with at least one each of <p>" +
            "a number <p>" +
        "one upper case Letter <p>one lower case letter <p>and one of @ # $ % & ";

    public PasswordDialog(String title) {
        this(title, null, null);
      }

    public PasswordDialog(String title, String regx, String passwordToolTip) {
        super();
        setTitle(title);
        setModal(true);
        if (regx != null) {
            this.regx = regx;
            this.passwordToolTip = passwordToolTip;
        }
        init();
    }

    void init() {
        if (regx != null && passwordToolTip != null)
            passwordField.setToolTipText(passwordToolTip);
        ButtonListener li = new ButtonListener();
        okButt.addActionListener(li);
        cancel.addActionListener(li);
        MultiPairColPanel mp= new MultiPairColPanel("");
        mp.addItemPair("User Name", nameField);
        mp.addItemPair("Password", passwordField);
        mp.addBlank();
        MultiPairColPanel buttonPanel = new MultiPairColPanel("");
        buttonPanel.addItemPair(cancel, okButt);
        FramedPanel outerP = new FramedPanel(new BorderLayout());
        outerP.add(mp, BorderLayout.NORTH);
        outerP.add(buttonPanel, BorderLayout.SOUTH);
        add(outerP);
        pack();
        getRootPane().setDefaultButton(okButt);
        setLocationRelativeTo(null);
        setVisible(true);
        toFront();
    }

    public String getName() {
        if (allOK)
            return name;
        else
            return null;
    }

    public String getPassword() {
        if (allOK)
            return password;
        else
            return null;
    }

    boolean checkPassword(String pwd)  {
        if (regx == null) {
            if (pwd.length() < minLength) return false;
//            if (!pwd.substring(1).matches("[a-z]")) return false;
            if (!pwd.matches("[0-9]")) return false;
            if (!pwd.matches("[a-z]")) return false;
            if (!pwd.matches("[A-Z]")) return false;
            if (!pwd.matches("[%@$^]")) return false;
            if (pwd.indexOf(" ") >= 0) return false;
            return true;
        }
        else
            return pwd.matches(regx);
    }

    boolean takeData() {
        allOK = false;
        name = nameField.getText();
        password = new String(passwordField.getPassword());
        if ((name.length() > 2) && checkPassword(password))
            allOK = true;
        return allOK;
    }

    void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "ERROR in Name/Password", JOptionPane.ERROR_MESSAGE);
        this.toFront();
    }

    void closeThisWindow() {
        setVisible(false);
        dispose();
    }

    class ButtonListener implements ActionListener  {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            if (src == okButt) {
                if (takeData()) {
                    allOK = true;
//                    System.out.println(password + " password is acceptable");
                    closeThisWindow();
                }
                else
                    showError((passwordToolTip == null) ? "Name/ Password NOT acceptable" : passwordToolTip);
            }
            else if (src == cancel) {
                allOK = false;
                closeThisWindow();
            }
        }
    }



    public static void main(String[] args) {
        PasswordDialog pDlg = new PasswordDialog("Testing password Entry");
//        pDlg.setVisible(true);
        if (pDlg.allOK)
            System.out.println("Name = " + pDlg.getName() + ", password = " + pDlg.getPassword());
    }
}
