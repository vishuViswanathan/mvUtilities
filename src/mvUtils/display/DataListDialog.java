package mvUtils.display;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: M Viswanathan
 * Date: 14-May-15
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataListDialog extends JDialog {
    MultiPairColPanel mp;
    JButton ok = new JButton("Ok");
    JButton cancel = new JButton("Cancel");
    boolean updated = false;
    CheckDataList listChecker;

    public DataListDialog(String title, CheckDataList listChecker) {
        super();
        setTitle(title);
        setModal(true);
        this.listChecker = listChecker;
        init(title);
    }

    void init(String title) {
        mp = new MultiPairColPanel(title);
        ButtonListener bl = new ButtonListener();
        ok.addActionListener(bl);
        cancel.addActionListener(bl);
    }

    public void setVisible(boolean bVisible) {
        if (bVisible) {
            mp.addBlank();
            mp.addItemPair(cancel, ok);
            add(mp);
            pack();
        }
        super.setVisible(bVisible);
    }

    public boolean isUpdated() {
        return updated;
    }

    public void addItemPair(String name, Component comp, boolean bold) {
        mp.addItemPair(name, comp, bold);
    }

    public void addItemPair(String name, String val, boolean bold) {
        mp.addItemPair(name, val, bold);
    }

    public void addItemPair(String name, Component comp) {
        mp.addItemPair(name, comp);
    }

    public void addItemPair(String name, double val, String format, boolean bold) {
        mp.addItemPair(name, val, format, bold);
    }

    public void addItemPair(String name, double val, String format) {
        mp.addItemPair(name, val, format);
    }

    public void addItem(Component comp) {
        mp.addItem(comp);
    }

    public int getRowCount() {
        return mp.getRowCount();
    }

    public void addItemPair(Component compLeft, Component compRight) {
        addItemPair(compLeft, false, compRight, false);
 /*
         lastRow++;
         gbcL.gridx = 0;
         gbcL.gridy = lastRow;
         add(compLeft, gbcL);
         gbcR.gridx = 1;
         gbcR.gridy = lastRow;
         add(compRight, gbcR);
         compPairs.add(new ComponentPair(compLeft, compRight));
         rowCount++;
 */
    }

    public void addItemPair(Component compLeft, boolean bBoldLeft, Component compRight, boolean bBoldRight) {
        mp.addItemPair(compLeft, compRight);
    }

    public void addItemPair(Component ntf, boolean bold) {
        mp.addItemPair(ntf, bold);
    }

    public void addItemPair(Component ntf) {
        mp.addItemPair(ntf);
    }

    public void addItemPair(Component ntf, boolean bAllowEdit, boolean bold) {
        mp.addItemPair(ntf, bAllowEdit, bold);
    }

    public void addBlank() {
        mp.addBlank();
    }

    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            if (src == ok) {
                ErrorStatAndMsg statAndMsg = listChecker.isDataListOK();
                if (statAndMsg.inError) {
                    showError(statAndMsg.msg);
                }
                else {
                    updated = true;
                    dispose();
                }
            }
            else if (src == cancel) {
                updated = false;
                dispose();
            }
        }
    }

    void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "ERROR", JOptionPane.ERROR_MESSAGE);
        toFront();
    }
}
