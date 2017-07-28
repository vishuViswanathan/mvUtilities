package mvUtils.physics;

import mvUtils.mvXML.ValAndPos;
import mvUtils.mvXML.XMLmv;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.util.Vector;

/**
 * Created by M Viswanathan on 03 Mar 2017
 */
public class ForceElement {
    private Vector3d force;
    private Point3d actingPoint; // ert the object's center of mass
    private Torque torque;
    private boolean valid = false;
    private boolean preciserON = false;
    PreciseTuple3d preciser;

    public ForceElement(String xmlStr) {
        takeFromXML(xmlStr);
    }

    private ForceElement(Point3d actingPoint, PreciseTuple3d preciser) {
        this.preciser = preciser;
        preciserON = (preciser != null);
        force = new Vector3d();
        this.actingPoint = new Point3d(actingPoint);
        torque = new Torque(preciser);
        valid = true;
    }

    public ForceElement(double forceMagnitude, Vector3d direction, Point3d actingPoint) {
        this(actingPoint, null);
        set(forceMagnitude, direction, actingPoint);
    }

    /**
     *
     * @param force the force
     * @param actingPoint location wrt the objects center of mass
     */
    protected ForceElement(Vector3d force, Point3d actingPoint, PreciseTuple3d preciser) {
        this(actingPoint, preciser);
        this.force = new Vector3d(force);
    }

    public ForceElement(Vector3d force, Point3d actingPoint, Point3d basePoint, PreciseTuple3d preciser) {
        this(actingPoint, preciser);
        this.force = new Vector3d(force);
        torque.add(new Torque(this, basePoint));
    }

    /**
     *
      * @param forces ve
     * @param basePoint
     */
    public ForceElement(Vector<ForceElement> forces, Point3d basePoint, PreciseTuple3d preciser) {
        this(basePoint, preciser);
        for (ForceElement oneF: forces) {
            if (preciserON) {
                preciser.add(force, oneF.force);
                torque.add(preciser.preciseIt(new Torque(oneF, basePoint, preciser)));
            }
            else {
                force.add(oneF.force);
                torque.add(new Torque(oneF, basePoint));
            }
        }
    }

    public void set(double forceMagnitude, Vector3d direction, Point3d location) {
        actingPoint.set(location);
        force.set(direction);
        double len = force.length();
        if (len > 0) {
            force.scale(forceMagnitude / len);
            torque = new Torque(this);
        }
    }

    public Vector3d getForce() {
        return force;
    }

    public Vector3d getForce(Vector3d f) {
        f.set(force);
        return f;
    }

    public boolean isValid() {
        return valid;
    }

    public Point3d getActingPoint() {
        return actingPoint;
    }

    public Torque getTorque() {
        return torque;
    }

    public Torque getTorque(Torque t) {
        return t.set(torque);
    }

    public StringBuilder dataInXML() {
        StringBuilder xmlStr = new StringBuilder(XMLmv.putTag("force", Vector3dMV.dataInCSV(force)));
        xmlStr.append(XMLmv.putTag("actingPoint", Vector3dMV.dataInCSV(actingPoint)));
        xmlStr.append(XMLmv.putTag("torque", torque.dataInXML()));
        xmlStr.append(XMLmv.putTag("valid", valid));
        xmlStr.append(XMLmv.putTag("preciserON", preciserON));
        if (preciserON)
            xmlStr.append(XMLmv.putTag("preciser", preciser.dataInXML()));
        return xmlStr;
    }

    private boolean takeFromXML(String xmlStr) {
        ValAndPos vp;
        vp = XMLmv.getTag(xmlStr, "force", 0);
        force = new Vector3dMV(vp.val);
        vp = XMLmv.getTag(xmlStr, "actingPoint", 0);
        actingPoint = new Point3dMV(vp.val);
        vp = XMLmv.getTag(xmlStr, "torque", 0);
        torque = new Torque(vp.val);
        return true;
    }
}
