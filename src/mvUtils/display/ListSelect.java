package mvUtils.display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class ListSelect {
    List<Selectable> theList;
    HashMap<Selectable, JCheckBox> listAndAction;
    boolean sorted = false;
    int rows = 10;
    boolean allSelected = true;
    Component parent;

    public ListSelect(Selectable[] list, Component parent, boolean sorted) {
        listAndAction = new HashMap<Selectable, JCheckBox>();
        this.sorted = sorted;
        this.parent = parent;
        this. rows = list.length;
        for (Selectable s: list) {
            allSelected &= s.isSelected();
            listAndAction.put(s, new JCheckBox(s.toString(), s.isSelected()));
        }
    }

    public ListSelect(Selectable[] list) {
        this(list, null, false);
    }

    public void takeAction() {
        for (Selectable s: listAndAction.keySet())
            s.setSelected(listAndAction.get(s).isSelected());
    }

    public boolean showSelectionDlg() {
        SelectionDlg selDlg = new SelectionDlg(listAndAction);
        if (parent == null)
            selDlg.setLocation(100, 50);
        else
            selDlg.setLocationRelativeTo(parent);
        selDlg.setVisible(true);
        return true;
    }

    class SelectionDlg extends JDialog {
        JCheckBox cbAll;
        JCheckBox[] boxes;

        SelectionDlg(HashMap<Selectable, JCheckBox> list) {
            setModal(true);
            Container outer;
            if (rows > 10) {
                outer = new ScrollPane();
                outer.setPreferredSize(new Dimension(200, 400));
            }
            else
                outer = new JPanel();
            JPanel jp = new JPanel();
            jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
            cbAll = new JCheckBox("Select All");
            jp.add(cbAll);
            cbAll.setSelected(allSelected);
            cbAll.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean sel = cbAll.isSelected();
                    for (Selectable s: list.keySet())  {
                        list.get(s).setSelected(sel);
                    }
                }
            });

            if (sorted) {
                TreeMap<String, JCheckBox> tree = new TreeMap<>();
                for (Selectable s : list.keySet()) {
                    tree.put(s.toString(), list.get(s));
                }
                for (String s : tree.keySet()) {
                    jp.add(tree.get(s));
                }
            }
            else {
                for (Selectable s : list.keySet()) {
                    jp.add(list.get(s));
                }
            }
            outer.add(jp);
            add(outer);
            pack();
        }
    }

}
