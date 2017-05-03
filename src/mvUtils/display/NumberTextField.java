package mvUtils.display;

//import directFiredHeating.DFHeating;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: M Viswanathan
 * Date: 5/16/12
 * Time: 11:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class NumberTextField extends JTextField implements ActionListener, FocusListener, XLcellData {
    boolean onlyInteger = true;
    public double min, max;
    public Color bgColor;
    public Color errColor = Color.red;
    public boolean inError;
    String textWithError = "";
    DecimalFormat format;
    String fmtStr;
    String errMsg;
//    public String title;
    boolean allowZero = false;
    boolean notify = true;
    InputControl controller;
    Window parent;
//    JLabel label;
    boolean bBold = false;
    char chDec;
    Vector<EditListener> editListeners;

    public NumberTextField(InputControl controller, double val, int size, boolean onlyInteger, double min, double max, String fmtStr,
                           String title) {
        super("", size);
        chDec = new DecimalFormatSymbols().getDecimalSeparator();
        this.controller = controller;
        if (controller != null)
            parent = controller.parent();
        this.onlyInteger = onlyInteger;
        this.setName(title);
        setFormat(fmtStr);
        setLimits(min, max);
//        this.fmtStr = fmtStr;
//        format = new DecimalFormat(fmtStr);
        DecimalFormatSymbols  dfs = format.getDecimalFormatSymbols();
        bgColor = getBackground();
        inError = false;
        addActionListener(this);
        addFocusListener(this);
        //        getDocument().addDocumentListener(this);
        setHorizontalAlignment(JTextField.RIGHT);
        setData(val);
        editListeners = new Vector<>();
        setDisabledTextColor(Color.BLUE);

//        setUndoAndRedo();
    }

    public NumberTextField(InputControl controller, double val, int size, boolean onlyInteger, double min, double max, String fmtStr,
                           String title, boolean allowZero) {
        this(controller, val, size, onlyInteger, min, max, fmtStr, title);
        this.allowZero = allowZero;
    }

    public void addEditListener(EditListener li) {
        if(!editListeners.contains(li))
            editListeners.add(li);
    }

    public void removedEditListener(EditListener li) {
        if(!editListeners.contains(li))
            editListeners.remove(li);
    }

    void setUndoAndRedo() {
        final UndoManager undo = new UndoManager();
        Document doc = getDocument();

        // Listen for undo and redo events
        doc.addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent evt) {
                undo.addEdit(evt.getEdit());
            }
        });

        // Create an undo action and add it to the text component
        getActionMap().put("Undo",
                new AbstractAction("Undo") {
                    public void actionPerformed(ActionEvent evt) {
                        try {
                            if (undo.canUndo()) {
                                undo.undo();
                            }
                        } catch (CannotUndoException e) {
                        }
                    }
                });

        // Bind the undo action to ctl-Z
        getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");

        // Create a redo action and add it to the text component
        getActionMap().put("Redo",
                new AbstractAction("Redo") {
                    public void actionPerformed(ActionEvent evt) {
                        try {
                            if (undo.canRedo()) {
                                undo.redo();
                            }
                        } catch (CannotRedoException e) {
                        }
                    }
                });

        // Bind the redo action to ctl-Y
        getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
    }

    public void setLimits(double min, double max) {
        this.min = min;
        this.max = max;
        setToolTip();
    }

    void setToolTip() {
        if (onlyInteger)
            errMsg = "Enter an INTEGER Value  between " + (int)min + " and " + (int)max;
        else
            errMsg = "Enter Value  between " + min + " and " + max;
        setToolTipText(errMsg);
    }

    public void setNormalBackground(Color color) {
        super.setBackground(color);
        bgColor = color;
    }

    public void setFormat(String fmtStr) {
        this.fmtStr = fmtStr;
        format = new DecimalFormat(fmtStr);
    }

    public String getFmtStr() {
        return fmtStr;
    }

    public void setTitle(String title) {
        setName(title);
//        label.setText(title);
    }

    public void setEnabled(boolean ena) {
        super.setEnabled(ena);
        enableToolTip(ena);
    }

    void enableToolTip(boolean ena) {
        if (ena)
            setToolTipText(errMsg);
        else
            setToolTipText(null);
    }

    public void setEditable(boolean bEdit) {
        super.setEditable(bEdit);
        if (bEdit)
            setForeground(Color.black);
        else
            setForeground(Color.blue);
        setBackground(Color.white);
        if (bEdit)
            setToolTipText(errMsg);
        else {
            setToolTipText(null);
        }
//        setDisabledTextColor(Color.MAGENTA);
    }

    public JLabel getLabel() {
        return new JLabel(getName());
    }

    public void showError() {
        if (inError)
            setBackground(errColor);
        else
            setBackground(bgColor);
    }

//    public void resetError() {
//        inError = false;
//        setBackground(bgColor);
//    }


    public boolean dataOK() {
        double val = getData();
        return (val >= min && val <= max);
    }

    public boolean isInError() {
        inError = false;
        if (isEditable()) {
            double val;
            textWithError = getText();
            String data = textWithError;
            if(chDec == ',')  {
                data = data.replace(".", "");
                data = data.replace(",", ".");
            }
            else
                data = getText().replace(",", "");
            try {
                if (onlyInteger) {
                    val = Integer.valueOf(data);
                } else {
                    val = Double.valueOf(data);
                }
            } catch (NumberFormatException e) {
                setText("0");
                val = 0;
                inError = true;
            }
            if (!inError) {
                if ((allowZero && (val == 0)) || (val >= min && val <= max)) {
                    setDataNoCheck(val);
                } else
                    inError = true;
            }
            showError();
        }
        return inError;
    }

    public void setData(double val) {
        notify = false;
        String txt = format.format(val);
        setText(txt);
        isInError();
        showError();
        notify = true;
    }

    void setDataNoCheck(double val) {
        String txt = format.format(val);
        setText(txt);
        showError();
        notify = true;

    }

    public String format(double val) {
        return format.format(val);
    }

    public double getData() {
        textWithError = getText();
        String data = textWithError;
        if(chDec == ',')  {
            data = data.replace(".", "");
            data = data.replace(",", ".");
        }
        else
            data = data.replace(",", "");
//        String val = getText();
        if (data.length() == 0)
            data = "0";
        return Double.valueOf(data);
    }

    public String titleAndVal() {
        return getName() + " <" + getText() + ">";
    }

    double oldVal = Double.NEGATIVE_INFINITY;

    public void addActionAndFocusListener(ActionAndFocusListener afl) {
        addActionListener(afl);
        addFocusListener(afl);
    }

    public void removeActionAndFocusListener(ActionAndFocusListener afl) {
        removeActionListener(afl);
        removeFocusListener(afl);
    }

    public void focusGained(FocusEvent fe) {
        oldVal = getData();
    }

    public void focusLost(FocusEvent fe) {
        if (controller != null)
            if (controller.canNotify())
                takeNote();
    }

    void informEditListeners() {
        for (EditListener eL: editListeners)
            eL.edited();
    }

    public void actionPerformed(ActionEvent te) {
        informEditListeners();
        if (controller != null)
            if (controller.canNotify())
                takeNote();
    }

//    @Override
//    public String getText() {
//        String text = super.getText();
//
//        return text.replace(",", "");
//    }

    void takeNote() {
        if (isInError()) {
            if (controller != null)
                controller.enableNotify(false);
            JOptionPane.showMessageDialog(this, errMsg + " [" + textWithError + "]", getName(), JOptionPane.ERROR_MESSAGE);
            if (controller != null)
                controller.enableNotify(true);
            if (!(parent == null))
                parent.toFront();
//          showError();
        }
    }

    public void resetValue() {
        if (oldVal != Double.NEGATIVE_INFINITY)
            if ((allowZero && (oldVal == 0)) || (oldVal >= min && oldVal <= max))
                setData(oldVal);
    }

    public boolean isChanged() {
         return getData() != oldVal;
    }

//    public String getName() {
//        return getName();
//    }

    public ValueForExcel getValueForExcel() {
//        String str = getText();
//        str.replace(",", "");
//        boolean bNumeric = str.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+");
//        double numVal = (bNumeric) ? Double.valueOf(str) : Double.NaN;
        double numVal = getData();
        boolean bNumeric = true;
        return new ValueForExcel(bBold, getText(), numVal, bNumeric, fmtStr);
    }

    public String toString() {
        return format.format(getData());
    }

    void debug(String msg) {
        System.out.println("NumberTextField [" + getText() + "]" + msg);
    }
}
