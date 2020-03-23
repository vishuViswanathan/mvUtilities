package mvUtils.display;

//import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer11_OmitComments;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Vector;

/**
 * User: M Viswanathan
 * Date: 02-Dec-16
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiColDataPanel extends FramedPanel{
    int nColumns;
    int lastRow = 0;
    String title = "";
    boolean bHasTitle = false;
    Vector<JPanel> groupBoxes;
    Insets insetL = new Insets(1, 1, 1, 2);
    Insets insetR = new Insets(1, 2, 1, 1);
    Insets insetLR = new Insets(1, 2, 1, 2);
    Insets insetLBlank = new Insets(5, 1, 5, 2);
    GridBagConstraints gbcL =
            new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.EAST, 0, insetL, 0, 0);
    GridBagConstraints gbcR =
            new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.EAST, 0, insetR, 0, 0);
    GridBagConstraints gbcLR =
            new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, 0, insetLR, 0, 0);
    int labelWidth = 0, dataWidth = 0;
    Font boldFont;
    int rowCount = 0;
    FramedPanel box = null;
    GridBagConstraints gbcBoxL =
            new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.EAST, 0, insetL, 0, 0);
    GridBagConstraints gbcBoxR =
            new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.EAST, 0, insetR, 0, 0);
    GridBagConstraints gbcBoxLR =
            new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, 0, insetLR, 0, 0);

    Vector<ComponentSet> componentSets;

    public MultiColDataPanel(String title, int nColumns, int labelWidth, int dataWidth ) {
        super(new GridBagLayout());
        this.nColumns = nColumns;
        this.labelWidth = labelWidth;
        this.dataWidth = dataWidth;
        this.title = title;
        if (title.length() > 0)  {
            setTitle(title);
            bHasTitle = true;
        }
        boldFont = (new JLabel()).getFont().deriveFont(Font.BOLD);
        componentSets = new Vector<>();
        groupBoxes = new Vector<>();
    }

    public MultiColDataPanel(String title, int nColumns ) {
        this(title, nColumns, 0, 0);
    }

    public void addGroup() {
        box = new FramedPanel(new GridBagLayout());
        lastRow++;
        gbcL.gridx = 0;
        gbcL.gridwidth = 2;
        gbcL.gridy = lastRow;
        gbcL.anchor = GridBagConstraints.CENTER;
        add(box, gbcL);
        gbcBoxL.gridy = -1;
        gbcBoxR.gridy = -1;
        groupBoxes.add(box);
    }

    public void removeAll() {
        super.removeAll();
        if (title.length()  > 0)
            setTitle(title);
        groupBoxes.removeAllElements();
    }

    public void setTitle(String title) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 1, 5, 1);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = nColumns;
        JLabel tL  = new JLabel(title);
        Font f = tL.getFont();
        tL.setFont(f.deriveFont(Font.BOLD));
        add(tL, gbc);
    }

    JComponent getItemName(String name, boolean bold) {
        JLabel lName = new JLabel(name);
        if (bold)
            lName.setFont(boldFont);
        if (labelWidth > 0)
            lName.setPreferredSize(new Dimension(labelWidth, 20));
        return lName;
    }

    public void addItemSet(Vector<Component> comp) {
        addItemSet(comp, false, false);
    }

    public void addBlank() {
        gbcL.insets = insetLBlank;
        JPanel p1 = new JPanel();
        addItem(p1);
        gbcL.insets = insetL;
    }

    public void addItem(String str) {
        addItem(getItemName(str, false));
    }

    public void addItem(Component comp) {
        if (box == null) {
            lastRow++;
            gbcLR.gridx = 0;
            gbcLR.gridy = lastRow;
            gbcLR.gridwidth = nColumns;
            add(comp, gbcLR);

            gbcL.gridy = lastRow;
            gbcR.gridy = lastRow;
            rowCount++;
        }
        else {
            gbcBoxL.gridx = 0;
            gbcBoxL.gridy++;
            gbcBoxR.gridy++;
            gbcBoxLR.gridx = 0;
            gbcBoxLR.gridwidth = nColumns;
            gbcBoxLR.gridy = gbcBoxL.gridy;
            box.add(comp, gbcBoxLR);
            rowCount++;
        }
    }

    public int getRowCount() {
        return rowCount;
    }

    public void addItemSet(Vector<Component> comp, boolean bBoldLeft, boolean bBoldRest, int leftItemHorzPos,
                           int restHorzPos ) {
        addItemSet(comp.toArray(new Component[]{null}), bBoldLeft, bBoldRest, leftItemHorzPos,
                                        restHorzPos);
    }

    public void addItemSet(Vector<Component> comp, boolean bBoldLeft, boolean bBoldRest) {
        addItemSet(comp, bBoldLeft, bBoldRest, 0, 0 );
    }

    public void addItemSet(String[] strings, boolean bBold) {
        addItemSet(strings, bBold, 0);
    }

    public void addItemSet(String[] strings, boolean bBold, int horizPos) {
        addItemSet(strings, bBold, bBold, horizPos, horizPos);
    }

    public void addItemSet(String[] strings, boolean bBoldLeft, boolean bBoldRest) {
        addItemSet(strings, bBoldLeft, bBoldRest, 0, 0);
    }

    public void addItemSet(String[] strings, boolean bBoldLeft, boolean bBoldRest, int leftItemHorzPos,
                           int restHorzPos ) {
        Vector<Component> compVect = new Vector<>();
        compVect.add(getItemName(strings[0], bBoldLeft));
        for (int c = 1; c < strings.length; c++)
            compVect.add(getItemName(strings[c], bBoldRest));
        addItemSet(compVect, bBoldLeft, bBoldRest, leftItemHorzPos, restHorzPos );

    }

    public void addItemSet(Component[] comp) {
        addItemSet(comp, false, false, 0, 0);
    }

    public void addItemSet(Component[] comp, boolean bBoldLeft, boolean bBoldRest, int leftItemHorzPos,
                           int restHorzPos) {
        if (box == null) {
            lastRow++;
            int gbcLAnchor = gbcL.anchor;
            if (leftItemHorzPos > 0)
                gbcL.anchor = leftItemHorzPos;
            gbcL.gridx = 0;
            gbcL.gridy = lastRow;
            add(comp[0], gbcL);
            gbcL.anchor = gbcLAnchor;  // reset
            int gbcRAnchor = gbcR.anchor;
            if (restHorzPos > 0)
                gbcR.anchor = restHorzPos;
            for (int i = 1; i < comp.length; i++) {
                gbcR.gridx = i;
                gbcR.gridy = lastRow;
                add(comp[i], gbcR);
            }
            gbcR.anchor = gbcRAnchor;  // reset
            componentSets.add(new ComponentSet(comp, bBoldLeft, bBoldRest));
            rowCount++;
        }
        else {
            int gbcLAnchor = gbcBoxL.anchor;
            if (leftItemHorzPos > 0)
                gbcBoxL.anchor = leftItemHorzPos;
            gbcBoxL.gridx = 0;
            gbcBoxL.gridy++;
            box.add(comp[0], gbcBoxL);
            gbcBoxL.anchor = gbcLAnchor;  // reset
            int gbcRAnchor = gbcBoxR.anchor;
            if (restHorzPos > 0)
                gbcBoxR.anchor = restHorzPos;
            for (int i = 1; i < comp.length; i++) {
                gbcBoxR.gridx = 1;
                gbcBoxR.gridy++;
                box.add(comp[i], gbcBoxR);
            }
            gbcBoxR.anchor = gbcRAnchor; // reset
            componentSets.add(new ComponentSet(comp, bBoldLeft, bBoldRest));
            rowCount++;
        }
    }

    public void setEnabled(boolean bEna) {
        super.setEnabled(bEna);
        for (JPanel p: groupBoxes)
            enableComponents(p, bEna);
    }

    ComponentSet getComponentPair(int row) {
        if (row < rowCount) {
            return (componentSets.get(row));
        }
        else
            return null;
    }

}
