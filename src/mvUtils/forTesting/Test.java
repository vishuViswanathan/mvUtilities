package mvUtils.forTesting;

import mvUtils.physics.Vector3dMV;

/**
 * User: M Viswanathan
 * Date: 06-Jun-16
 * Time: 3:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void wasteTime(int milliseconds) {
        long sleepTime = milliseconds*1000000L; // convert to nanoseconds
        long startTime = System.nanoTime();
        while ((System.nanoTime() - startTime) < sleepTime) {}
    }

    static void debug(String title, Object val) {
        System.out.println(title + ": " + val);
    }

    public static void main (String[] argv) {
        Vector3dMV v = new Vector3dMV(6, -4, 0);
        debug("v Len", v.length());
        Vector3dMV distance = new Vector3dMV(1.9152, 2.8828, 1);
        debug("distance Len", distance.length());
        Vector3dMV cross = new Vector3dMV();
        debug("angle12", v.angle(distance));
        debug("angle21", distance.angle(v));
        cross.cross(v, distance);
        debug("cross 1" , cross);
        debug("len cross1", cross.length());
        cross.scale(1 / distance.lengthSquared());
        debug("cross 2" , cross);
        debug("len cross2", cross.length());
    }
}
