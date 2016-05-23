package mvUtils.display;

import javax.swing.*;
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
public class DataListEditorPanel extends FramedPanel {
    MultiPairColPanel mp;
    JButton save = new JButton("Save");
    JButton exit = new JButton("Exit");
    JButton delete = new JButton("Delete");
    JButton edit = new JButton("Edit");
    JButton reset = new JButton("Reset");
    EditResponse.Response response;
    boolean updated = false;
    DataHandler dataHandler;
    boolean allowEdit = false;
    boolean allowDelete = false;

    public DataListEditorPanel(String title, DataHandler dataHandler) {
        this(title, dataHandler, false);
    }

    public DataListEditorPanel(String title, DataHandler dataHandler, boolean allowEdit) {
        this(title, dataHandler, allowEdit, false);
    }
    public DataListEditorPanel(String title, DataHandler dataHandler, boolean allowEdit, boolean allowDelete) {
        super();
        this.dataHandler = dataHandler;
        this.allowEdit = allowEdit;
        this.allowDelete = allowDelete;
        init(title);
    }

    void init(String title) {
        mp = new MultiPairColPanel(title);
        ButtonListener bl = new ButtonListener();
        save.addActionListener(bl);
        exit.addActionListener(bl);
        edit.addActionListener(bl);
        reset.addActionListener(bl);
        if (allowEdit && allowDelete) {
            delete.setEnabled(false);
            delete.addActionListener(bl);
        }
    }

    public void setVisible(boolean bVisible) {
        setVisible(bVisible, false);
    }

    public void setVisible(boolean bVisible, boolean enableEdit) {
        if (bVisible) {
            JPanel outerPanel = new JPanel(new BorderLayout());
            mp.addBlank();
            outerPanel.add(mp,BorderLayout.CENTER);
            outerPanel.add(buttonPanel(), BorderLayout.SOUTH);
            add(outerPanel);
            setEditable(allowEdit && enableEdit);
        }
        super.setVisible(bVisible);
    }

    JPanel buttonPanel() {
        JPanel bP = new JPanel(new GridLayout());
        if (allowEdit) {
            if (allowDelete) {
                delete.setEnabled(false);
                bP.add(delete);
            }
            bP.add(new JPanel());
            bP.add(edit);
            reset.setEnabled(false);
            bP.add(reset);
            save.setEnabled(false);
            bP.add(save);
        }
        bP.add(exit);
        return bP;
    }

    public void resetAll() {
        setEditable(false);
    }

    public boolean isUpdated() {
        return updated;
    }

    public EditResponse.Response getResponse() {
        return response;
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

    public void addGroup() {
        mp.addGroup();
    }

    void setEditable(boolean ena) {
        mp.setEnabled(ena);
        if (allowEdit) {
            if (allowDelete)
                delete.setEnabled(ena);
            save.setEnabled(ena);
            reset.setEnabled(ena);
            edit.setEnabled(!ena);
        }
    }

    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            if (src == save) {
                ErrorStatAndMsg statAndMsg = dataHandler.checkData();
                if (statAndMsg.inError) {
                    showError(statAndMsg.msg);
                }
                else {
                    response = EditResponse.Response.SAVE;
                    if (dataHandler.saveData())
                        setEditable(false);
                }
            }
            else if (src == exit) {
                response = EditResponse.Response.EXIT;
                dataHandler.cancel();
            }
            else if (src == delete) {
                response = EditResponse.Response.DELETE;
                dataHandler.deleteData();
            }
            else if (src == edit) {
                setEditable(true);
            }
            else if (src == reset) {
                response = EditResponse.Response.RESET;
                dataHandler.resetData();
            }
        }
    }

    void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}
