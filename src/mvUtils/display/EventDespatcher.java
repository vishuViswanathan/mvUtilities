package mvUtils.display;

import java.awt.event.ComponentListener;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;

/**
 * an inteface for despatching events of the container to
 * components
 */

public interface EventDespatcher {
	void addFocusListener(FocusListener fl);
	void addMouseListener(MouseListener ml);
	void addComponentListener(ComponentListener cl);
}