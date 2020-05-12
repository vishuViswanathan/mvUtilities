package mvUtils.forTesting;

import mvUtils.display.ListSelect;
import mvUtils.display.Selectable;

import java.util.Vector;

public class TestSelectList {
    Ca theList[];

    TestSelectList(int size) {
        theList = new Ca[size];
        for (int i = 0; i < theList.length; i++)
            theList[i] = (new Ca("Item " + i));
    }

    Boolean showDlg(boolean sorted) {
        ListSelect lS = new ListSelect(theList, null, sorted);
        lS.showSelectionDlg();
        lS.takeAction();
        return true;
    }

    class Ca implements Selectable {
        String name;
        Boolean bSelected = false;
        Ca(String name) {
            this.name = name;
        }

        @Override
        public void setSelected(Boolean selected) {
            if (selected)
                System.out.println(name + " Selected");
            bSelected = selected;
        }

        @Override
        public boolean isSelected() {
            return bSelected;
        }

        public String toString() {
            return name;
        }

    }


    public static void main(String[] arg) {
        TestSelectList tl = new TestSelectList(10);
        tl.showDlg(false);
        System.out.println("First time");
        tl.showDlg(true);
        System.out.println("Second time");

        TestSelectList tl1= new TestSelectList(20);
        tl1.showDlg(false);
        System.out.println("First time");
        tl1.showDlg(true);
        System.out.println("Second time");

    }
}
