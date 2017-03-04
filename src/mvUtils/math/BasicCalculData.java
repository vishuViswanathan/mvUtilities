package mvUtils.math;
import java.awt.*;
import java.io.Serializable;



/**
 * interface BasicCalculData
 * The interfce for details of data entered for calculation,
 * some base drawing, title etc.
 */
 
public interface BasicCalculData extends Serializable{
	String title();
	void drawBasePic(Graphics g, Rectangle area,
					 Point origin);
	
}