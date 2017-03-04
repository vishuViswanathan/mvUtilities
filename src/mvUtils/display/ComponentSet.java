package mvUtils.display;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

/**
 * User: M Viswanathan
 * Date: 02-Dec-16
 * Time: 2:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class ComponentSet {
    Component[] components;
    boolean bCompLBold, bCompOthersBold;
    public ComponentSet( Component[] components, boolean bCompLBold, boolean bCompOthersBold) {
        this.components = components;
        this.bCompLBold = bCompLBold;
        this.bCompOthersBold = bCompOthersBold;
    }

    public int size() {
        return components.length;
    }

    public ComponentSet( Component[] components) {
        this(components, false, false);
    }

    public String compString(int column) {
        Component comp = components[column];
        if (comp instanceof JLabel)
            return (((JLabel) comp).getText());
        else if (comp instanceof JTextField)
            return (((JTextField)comp).getText());
        else
            return "";
    }

    public boolean isBold(boolean bLeft) {
        return ((bLeft) ? bCompLBold : bCompOthersBold);
    }

    public Component getComponent(int column) {
        return components[column];
    }
}
