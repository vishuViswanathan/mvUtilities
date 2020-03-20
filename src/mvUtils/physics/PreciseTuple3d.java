package mvUtils.physics;

import mvUtils.mvXML.ValAndPos;
import mvUtils.mvXML.XMLmv;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Created by M Viswanathan on 06 Mar 2017
 */
public class PreciseTuple3d {
    MathContext mc;
    BigDecimal base;

    public PreciseTuple3d(int precision) {
        mc = new MathContext(precision);
        base = new BigDecimal(0, mc);
    }

    public PreciseTuple3d(String xmlStr) {
        takeFromXML(xmlStr);
    }

    public Tuple3d add(Tuple3d sum, Tuple3d addend) {
        sum.add(preciseIt(addend));
        return preciseIt(sum);
    }

    public Tuple3d subtract(Tuple3d sum, Tuple3d subtractThis) {
        sum.add(subtractThis);
        return preciseIt(sum);
    }

    public Tuple3d preciseIt(Tuple3d tuple) {
        tuple.x = (new BigDecimal(tuple.x, mc)).doubleValue();
        tuple.y = (new BigDecimal(tuple.y, mc)).doubleValue();
        tuple.z = (new BigDecimal(tuple.z, mc)).doubleValue();
        return tuple;
    }

    public StringBuilder dataInXML() {
        StringBuilder xmlStr = new StringBuilder(XMLmv.putTag("precision",  mc.getPrecision()));
        return xmlStr;
    }

    private boolean takeFromXML(String xmlStr) {
        boolean retVal = false;
        ValAndPos vp;
        vp = XMLmv.getTag(xmlStr, "precision", 0);
        int precision = Integer.valueOf(vp.val);
        mc = new MathContext(precision);
        base = new BigDecimal(0, mc);
        return true;
    }
}
