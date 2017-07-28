package mvUtils.physics;


import mvUtils.mvXML.ValAndPos;
import mvUtils.mvXML.XMLmv;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.util.Vector;

/**
 * Created by M Viswanathan on 04 Mar 2017
 */
public class Torque extends Vector3dMV {
    public static int AboutX = 0;
    public static int AboutY = 1;
    public static int AboutZ = 2;

    PreciseTuple3d preciser;
    boolean precisorON = false;

    public Torque() {
        super();
    }

    public Torque(String xmlStr) {
        this();
        takeFromXML(xmlStr);
    }

    public Torque(PreciseTuple3d preciser) {
        super();
        this.preciser = preciser;
        precisorON = (preciser != null);
    }

    public Torque(ForceElement forceElement, Point3d aboutPointP0) {
        this(forceElement, aboutPointP0, null);
    }

    public Torque(ForceElement forceElement) {
        this(forceElement, new Point3d());
    }

    public Torque(ForceElement forceElement, Point3d aboutPointP0, PreciseTuple3d preciser) {
        this(preciser);
        // forcElement has the force Vector and the Point at which it acts
        Vector3d vForcePointToP0 = new Vector3d(); // prepare to point
        vForcePointToP0.sub(aboutPointP0, forceElement.getActingPoint()); // toPoint, fromPoint
        double vForcePointToP0Length = vForcePointToP0.length();
        if (vForcePointToP0Length != 0) {
            Vector3d force = forceElement.getForce();
            double forceMagnitude = force.length();
            if (forceMagnitude != 0) {
                double angleP0ToForce = vForcePointToP0.angle(force);
                double vP0PxLen = vForcePointToP0Length * angleP0ToForce;
                Vector3d vForcePointPx = new Vector3d(force); // prepare base
                vForcePointPx.scale(vP0PxLen / forceMagnitude);
                Vector3d vPxP0 = new Vector3d(vForcePointToP0);
                vPxP0.sub(vForcePointPx);
                cross(force, vPxP0);
                if (precisorON)
                    preciser.preciseIt(this);
            }
        }
    }

    public Torque set(Torque t) {
        set(t.x, t.y, t.z);
        preciser = t.preciser;
        return t;
    }

//    public StringBuilder dataInXML() {
//        StringBuffer xmlStr = new StringBuffer(XMLmv.putTag("tuple3d", dataInCSV()));
//        xmlStr.append(XMLmv.putTag("precisorON", precisorON)).append(XMLmv.putTag("preciser", preciser))
//    }

    public static void main(String[] args) {
        boolean withBigDecimal = true;
        for (int y = 0; y < 2; y++ ) {
            PreciseTuple3d precisor = (withBigDecimal) ? new PreciseTuple3d(15) : null;
            long nanoStart = System.nanoTime();
            for (int x = 0; x < 50; x++) {
                ForceElement f1 = new ForceElement(new Vector3d(10, 10, 0), new Point3d(0, 5, 0), precisor);
                Point3d torqueCenter = new Point3d(0, 7, 0);
                Torque t = new Torque(f1, torqueCenter, precisor);
//        System.out.println(t);
                ForceElement f2 = new ForceElement(new Vector3d(10, 10, 0), new Point3d(-0, 9.000000000000001, 0), precisor);
                t = new Torque(f2, torqueCenter, precisor);
//        System.out.println(t);
                Vector<ForceElement> set = new Vector<>();
                set.add(f1);
                set.add(f2);
                ForceElement f3 = new ForceElement(set, torqueCenter, precisor);
//        System.out.println(f3.getTorque());
//        System.out.println((new BigDecimal(1.0 - 0.8, new MathContext(15, RoundingMode.HALF_UP))).doubleValue());
            }
            System.out.println("Time spent " + ((withBigDecimal) ? "With BigDecimal = \t" : " No BigDecimal = \t") + (System.nanoTime() - nanoStart) + " ns");
            withBigDecimal = !withBigDecimal;
        }

    }

    public StringBuilder dataInXML() {
        StringBuilder xmlStr = new StringBuilder(XMLmv.putTag("Value", dataInCSV()));
        xmlStr.append(XMLmv.putTag("precisorON", precisorON));
        if (precisorON)
            xmlStr.append(XMLmv.putTag("preciser", preciser.dataInXML()));
        return xmlStr;
    }

    public boolean takeFromXML(String xmlStr) {
        boolean retVal = false;
        ValAndPos vp;
        vp = XMLmv.getTag(xmlStr, "Value", 0);
        set(vp.val);
        vp = XMLmv.getTag(xmlStr, "precisorON", vp.endPos);
        precisorON = (vp.val.equalsIgnoreCase("1"));
        if (precisorON) {
            vp = XMLmv.getTag(xmlStr, "preciser", vp.endPos);
            preciser = new PreciseTuple3d(vp.val);
        }
        return retVal;
    }
}
