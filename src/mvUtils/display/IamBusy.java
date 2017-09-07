package mvUtils.display;

import javax.swing.*;
import java.awt.*;

/**
 * Created by viswanathanm on 07-09-2017.
 */
public class IamBusy {
    JDialog dlgWait;

    IamBusy(Frame parent, String msg) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        dlgWait = new JDialog(parent, msg, true);
                        dlgWait.setSize(new Dimension(msg.length() * 10, 20));
                        dlgWait.setLocationRelativeTo(parent);
                        dlgWait.setVisible(true);
                    }
                },
                "IamBusy").start();
    }

    void close() {
        dlgWait.dispose();
    }
}
