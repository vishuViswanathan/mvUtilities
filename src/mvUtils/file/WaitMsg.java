package mvUtils.file;

import jdk.nashorn.internal.scripts.JD;
import mvUtils.display.FramedPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * User: M Viswanathan
 * Date: 03-Jun-16
 * Time: 1:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class WaitMsg {
    JDialog dialog;
    public WaitMsg(Frame parent, String msg, ActInBackground actInBackground) {
       dialog = new WaitDlg(parent, msg);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                actInBackground.doInBackground();
                return null;
            }

            @Override
            protected void done() {
                dialog.dispose();
                actInBackground.done();
            }
        };
        worker.execute();
        dialog.setVisible(true);
    }

    public void dispose() {
        dialog.dispose();
    }

    class WaitDlg extends JDialog {
        WaitDlg(Frame parent, String msg) {
            super(parent, true); // modal
            setUndecorated(true);
            FramedPanel fp = new FramedPanel(new BorderLayout());
            JProgressBar bar = new JProgressBar();
            bar.setIndeterminate(true);
            bar.setStringPainted(true);
            bar.setString("   " + msg + "   ");
            fp.add(bar, BorderLayout.CENTER);
            add(fp);
            pack();
            setLocationRelativeTo(parent);
        }
    }
}
