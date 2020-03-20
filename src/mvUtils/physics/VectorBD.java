package mvUtils.physics;

import javax.vecmath.Tuple3d;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by mviswanathan on 28-07-2017.
 */
public class VectorBD extends Vector3dMV {
    BigDecimal xBD;
    BigDecimal yBD;
    BigDecimal zBD;
    MathContext mc = MathContext.DECIMAL128;

    public VectorBD() {
        super();
        setBDValues(x, y, z);
    }

    public VectorBD(double x, double y, double z) {
        super(x, y, z);
        setBDValues(x, y, z);
    }

    public VectorBD(String strCSV) throws NumberFormatException {
        super();
        set(strCSV);
        setBDValues(x, y, z);
    }

    public VectorBD(Tuple3d tuple) {
        super(tuple);
        setBDValues(x, y, z);
    }

    public VectorBD(double scale, Tuple3d vecRef) {
        super();
        scale(scale, vecRef); // super call
        setBDValues(x, y, z);
    }

    private void setBDValues(double x, double y, double z) {
        xBD = new BigDecimal(x, mc);
        yBD = new BigDecimal(y, mc);
        zBD = new BigDecimal(z, mc);
        update3dValues();
    }

    public void addTuple(Tuple3d addThis) {
        xBD = xBD.add(new BigDecimal(addThis.x, mc));
        yBD = yBD.add(new BigDecimal(addThis.y, mc));
        zBD = zBD.add(new BigDecimal(addThis.z, mc));
//        update3dValues();
    }

    public void subtractTuple(Tuple3d subThis) {
        xBD = xBD.subtract(new BigDecimal(subThis.x, mc));
        yBD = yBD.subtract(new BigDecimal(subThis.y, mc));
        zBD = zBD.subtract(new BigDecimal(subThis.z, mc));
//        update3dValues();
    }

    public void setTuple(double x, double y, double z) {
        setBDValues(x, y, z);
    }

    public void update3dValues() {
        set(xBD.doubleValue(), yBD.doubleValue(), zBD.doubleValue());
    }

}
