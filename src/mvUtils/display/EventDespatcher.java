package mvUtils.display;

import java.awt.event.ComponentListener;
import java.awt.event.FocusListener;
import java.awt.event.MouseListener;

/**
 * an inteface for despatching events of the container to
 * components
 */

public interface EventDespatcher {
	public void addFocusListener(FocusListener fl);
	public void addMouseListener(MouseListener ml);
	public void addComponentListener(ComponentListener cl);
}