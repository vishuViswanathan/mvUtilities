package mvUtils.physics;

import mvUtils.display.SmartFormatter;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by M Viswanathan on 15 Jun 2014
 */
public class Vector3dMV extends Vector3d {
    public enum Axis {X, Y, Z};

    public Vector3dMV() {
        super();
    }

    public Vector3dMV(double x, double y, double z) {
        super(x, y, z);
    }

    public Vector3dMV(String strCSV) throws NumberFormatException {
        super();
        set(strCSV);
    }

    public Vector3dMV(Tuple3d tuple) {
        super(tuple);
    }

    public Vector3dMV(double scale, Tuple3d vecRef) {
        super();
        scale(scale, vecRef); // super call
    }

    static public Vector3dMV getRandomVector(double minLen, double maxLen, Tuple3d axes, Random rd) {
        double len = minLen + rd.nextDouble() * (maxLen - minLen);
        Vector3d direction = new Vector3d(axes.x * (rd.nextDouble() - 0.5),
                axes.y * (rd.nextDouble() - 0.5), axes.z * (rd.nextDouble() - 0.5));
        direction.normalize();
        return new Vector3dMV(len, direction);
    }

    public void scale(Tuple3d scale, Tuple3d vecRef) {
        set(scale.x * vecRef.x, scale.y * vecRef.y, scale.z * vecRef.z);
    }

    public void scale(Tuple3d scale) {
        set(scale.x * x, scale.y * y, scale.z * z);
    }

    public double projectionLength(Vector3d along) {
        return dot(along) / along.length();
    }

    public static Vector3dMV meanVector3dMV(Tuple3d vec1, Tuple3d vec2) {
        Vector3dMV vec = new Vector3dMV();
        vec.setMean(vec1, vec2);
        return vec;
    }

    /**
     * Projection if this Vector on a plane for which 'normal' is normal
     * @param normal the plane for which this is the normal
     * @return
     */
    public Vector3d projectionOnPlane(Vector3d normal) {
        double normLen = normal.length();
        if (normLen > 0) {
            Vector3d retVal = new Vector3d(this);
            Vector3d normalComponent = new Vector3dMV(normal);
            normalComponent.normalize();
            double lenOfProjectionOnNormal = dot(normalComponent);
            normalComponent.scale(lenOfProjectionOnNormal);
            retVal.sub(normalComponent);
            return retVal;
        }
        return null;
    }

    public Vector3dMV setMean(Tuple3d vec1, Tuple3d vec2) {
        set(vec1.x + vec2.x, vec1.y + vec2.y, vec1.z + vec2.z);
        scale(0.5);
        return this;
    }

    public void scaleAndAdd(Tuple3d scaleThis, double scale) {
       x += scaleThis.x * scale;
       y += scaleThis.y * scale;
       z += scaleThis.z * scale;
    }

    public boolean isNonZero() {
        return (x != 0 || y != 0 || z != 0);
    }

    public void set(String strCSV) throws NumberFormatException {
        String[] split = strCSV.split(",");
        if (split.length == 3)
            set(Double.valueOf(split[0]), Double.valueOf(split[1]), Double.valueOf(split[2]));
        else
            throw new NullPointerException("CSV elements are not 3");
    }

    public String dataInCSV() {
        return "" + x + ", " + y + ", " + z;
    }

    public String dataInCSV(String fmtStr) {
        DecimalFormat fmt = new DecimalFormat(fmtStr);
        return fmt.format(x)+ ", " + fmt.format(y) + ", " + fmt.format(z);
    }

    public static String dataInCSV(Tuple3d tuple) {
        return "" + tuple.x + ", " + tuple.y + ", " + tuple.z;
    }

    public static String dataInCSV(double[] data) {
        String csv = "";
        for (int i = 0; i < data.length; i++)
            csv += ((i > 0) ? ", " : "") + data[i];
        return csv;
    }

    public String dataInCSV(int significantDigits) {
        SmartFormatter fmt = new SmartFormatter(significantDigits);
        return fmt.format(x)+ ", " + fmt.format(y) + ", " + fmt.format(z);
    }

    public void negateOneAxis(Axis about) {
        switch(about) {
            case X:
                x = -x;
                break;
            case Y:
                y = -y;
                break;
            case Z:
                z = -z;
                break;
        }
    }

    public static void main(String[] arg) {
        Random rd =new Random();
        Vector3d v1 = new Vector3d(rd.nextDouble(), rd.nextDouble(), 0);

        for (int n = 1; n < 20; n++){
            Vector3dMV vect = Vector3dMV.getRandomVector(7,
                    7, v1, rd);
            System.out.println("vect = " + vect + ", " + vect.length());
        }
    }
}
