package mvUtils.display;

/**
 * User: M Viswanathan
 * Date: 04-Mar-16
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: M Viswanathan
 * Date: 4/23/13
 * Time: 11:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class OneParameterDialog extends JDialog {
    JButton okButt = new JButton("OK");
    JButton cancel = new JButton("Cancel");
    NumberTextField tfVal = null;
    boolean bTextData = false;
    JTextField textField;
    ActionListener li;
    double val;
    String textVal;
    String title;
    String prompt;
    String fmt;
    InputControl control;
    boolean ok = false;
    long forTime = 0;
    java.util.Timer timer;
    JPanel dataP = new JPanel();
    boolean bYesNo = false;
    public OneParameterDialog(InputControl control, String title, String okName, String cancelName, long forTime) {
        super(control.parent(), title, Dialog.ModalityType.DOCUMENT_MODAL);
        this.control = control;
        this.title = title;
        okButt = new JButton(okName);
        cancel = new JButton(cancelName);
        this.forTime = forTime;
        if (forTime > 0) {
            timer = new java.util.Timer();
            timer.schedule(new CloseDialogTask(this), forTime);
        }
    }

    public OneParameterDialog(InputControl control, String title, String okName, String cancelName) {
        this(control, title, okName, cancelName, 0);
    }

    public OneParameterDialog(InputControl control, String title, long forTime) {
        this(control, title, "OK", "Cancel", forTime);
    }

    public OneParameterDialog(InputControl control, String title, boolean bTextData) {
        this(control, title, 0);
    }

    public OneParameterDialog(InputControl control, String title) {
        this(control, title, 0);
    }

    public void setValue(String prompt, String val, int len) {
        bTextData = true;
        this.prompt = prompt;
        textField = new JTextField(val, len);
        MultiPairColPanel jp = new MultiPairColPanel("");
        jp.addItemPair(prompt, textField);
        dataP.add(jp);
        proceed();
    }

    public void setValue(String prompt, double val, String fmt, double min, double max) {
        this.prompt = prompt;
        this.val = val;
        this.fmt = fmt;
        tfVal = new NumberTextField(control, val, 6, false, min, max, fmt, prompt);
        tfVal.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {

            }

            public void keyPressed(KeyEvent e) {
                purgeTimer();
            }

            public void keyReleased(KeyEvent e) {

            }
        });
        MultiPairColPanel jp = new MultiPairColPanel("");
        jp.addItemPair(tfVal);
        dataP.add(jp);
        proceed();
    }

    void purgeTimer() {
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }
    }

    private void proceed() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                purgeTimer();
            }
        });
        jbInit();
        pack();

    }

    public void setValue(String msg) {
        tfVal = null;
        val = JOptionPane.YES_OPTION;
        bYesNo = true;
        dataP.add(new JLabel(msg));
        proceed();
    }

    void jbInit() {
        Dimension d1 = okButt.getPreferredSize();
        Dimension d2 = cancel.getPreferredSize();
        Dimension d = new Dimension(Math.max(d1.width, d2.width), Math.max(d1.height, d2.height));
        okButt.setPreferredSize(d);
        cancel.setPreferredSize(d);
        li = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object src = e.getSource();
                if (src == okButt) {
                    if (bYesNo)
                        val = JOptionPane.YES_OPTION;
                    else
                        noteValuesFromUI();
                    ok = true;
                    closeThisWindow();
                } else if (src == cancel) {
                    if (bYesNo)
                        val = JOptionPane.NO_OPTION;
                    closeThisWindow();
                }
            }
        };
        okButt.addActionListener(li);
        cancel.addActionListener(li);
        Container dlgP = getContentPane();
        if (bTextData) {
            if (textField != null)
                textField.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        NumberTextField src = (NumberTextField) e.getSource();
                        if (!src.isInError())
                            okButt.doClick();
                    }
                });
        }
        else {
            if (tfVal != null)
                tfVal.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        NumberTextField src = (NumberTextField) e.getSource();
                        if (!src.isInError())
                            okButt.doClick();
                    }
                });
        }
        JPanel outerP = new JPanel(new BorderLayout());
        outerP.add(dataP, BorderLayout.NORTH);
        MultiPairColPanel jp = new MultiPairColPanel("");
        jp.addItemPair(cancel, okButt);
        outerP.add(jp, BorderLayout.SOUTH);
        dlgP.add(outerP);
        getRootPane().setDefaultButton(okButt);
    }

    void noteValuesFromUI() {
        if (bTextData)
            textVal = textField.getText();
        else
            val = tfVal.getData();
    }

    public double getVal() {
        return val;
    }

    public String getTextVal() {
        return textVal;
    }

    public boolean isOk() {
        return ok;
    }

    void closeThisWindow() {
        setVisible(false);
        dispose();
        control.parent().setVisible(true);
    }

    class CloseDialogTask extends TimerTask {
        JDialog dlg;
        CloseDialogTask(JDialog dlg) {
            this.dlg = dlg;
        }

        public void run() {
            timer.cancel();
            timer.purge();
            okButt.doClick();
        }
    }
}
