package mvUtils.display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.TimerTask;

/**
 * User: M Viswanathan
 * Date: 29-Dec-16
 * Time: 4:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class OneComponentDialog extends JDialog  {
    JButton okButt = new JButton("OK");
    JButton cancel = new JButton("Cancel");
    String title;
    InputControl control;
    boolean ok = false;
    Component comp;
    DataHandler dataHandler;
    ErrorStatAndMsg stat;
    Window parent = null;
    public OneComponentDialog(InputControl control, String title, Component comp, DataHandler dataHandler) {
        super(control.parent(), Dialog.ModalityType.DOCUMENT_MODAL);
        this.control = control;
        if (control != null)
            parent = control.parent();
        setLocationRelativeTo(parent);
        this.title = title;
        this.comp = comp;
        setDataHandler(dataHandler);
        jbInit();
    }

    public OneComponentDialog(InputControl control, String title, Component comp) {
        this(control, title, comp, null);
    }
    
    void jbInit() {
        JPanel inP = new JPanel();
        inP.add(comp);
        Dimension d1 = okButt.getPreferredSize();
        Dimension d2 = cancel.getPreferredSize();
        Dimension d = new Dimension(Math.max(d1.width, d2.width), Math.max(d1.height, d2.height));
        okButt.setPreferredSize(d);
        cancel.setPreferredSize(d);
        ActionListener li = e -> {
            Object src = e.getSource();
            if (src == okButt) {
                if (dataHandler != null)
                    ok = !dataHandler.checkData().inError;
                else
                    ok = true;
                if (ok)
                    closeThisWindow();
            } else if (src == cancel) {
                closeThisWindow();
            }
        };
        okButt.addActionListener(li);
        cancel.addActionListener(li);
        JPanel outerP = new JPanel(new BorderLayout());
        MultiPairColPanel jp = new MultiPairColPanel(title);
        jp.addItem(comp);
        jp.addItemPair(cancel, okButt);
        outerP.add(jp, BorderLayout.SOUTH);
        Container dlgP = getContentPane();
        dlgP.add(outerP);
        getRootPane().setDefaultButton(okButt);
        pack();
    }

    public boolean isOk() {
        return ok;
    }

    public void setDataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    void closeThisWindow() {
        setVisible(false);
        dispose();
        if (parent != null)
            parent.setVisible(true);
    }

    public static void main(String[] args) {
        JComboBox jcb = new JComboBox(new String[]{"One", "Two"});
        OneComponentDialog dlg = new OneComponentDialog(new InputControl() {
            @Override
            public boolean canNotify() {
                return false;
            }

            @Override
            public void enableNotify(boolean ena) {

            }

            @Override
            public Window parent() {
                return null;
            }
        }, "Choose from the list", jcb );
        dlg.setVisible(true);
        if (dlg.isOk())
            System.out.println("Selected item = " + jcb.getSelectedItem());
    }
}
