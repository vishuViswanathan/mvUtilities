package mvUtils.math;

import java.text.DecimalFormat;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: M Viswanathan
 * Date: 3/17/12
 * Time: 7:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class XYArray {
    public int arrLen;
    Vector<DoublePoint> xyVect;

    DoublePoint[] doublePoints;
    DoubleMaxMin xMaxMin, yMaxMin;
    boolean yAscending = true;
    boolean yConfused = false;
    boolean dirChecked = false;  // checking for y value direction
    public XYArray() {
        xMaxMin = new DoubleMaxMin();
        yMaxMin = new DoubleMaxMin();
        xyVect = new Vector<DoublePoint>();
    }

    public XYArray(String dataPairStrList) {
        this();
        setValues(dataPairStrList);
    }

    public XYArray(DoublePoint[] dataPairList) {
        this();
        setValues(dataPairList);
    }

    public XYArray(XYArray ref) {
        this();
        DoublePoint[] dpRef =  ref.getGraph();
        DoublePoint oneP;
        for (int i = 0; i < dpRef.length; i++)  {
            oneP = dpRef[i];
            add(new DoublePoint(oneP.x, oneP.y));
        }
        setDoublePoints();
        arrLen = dpRef.length;
     }

    public String valPairStr() {
        String valStr = "";
        for (DoublePoint dp: xyVect)
            valStr += ((valStr.length() > 0) ? ", " : "") + dp.x + ", " + dp.y;
        return valStr;
    }

    public int nElements() {
        return xyVect.size();
    }

    public int setValues(DoublePoint[] dataPairList) {
//        doublePoints = dataPairList;
        for (int i = 0; i < dataPairList.length; i++)  {
            add(dataPairList[i]);
//            xMaxMin.takeNewValue(doublePoints[i].x);
//            yMaxMin.takeNewValue(doublePoints[i].y);
        }
        setDoublePoints();
        arrLen =  dataPairList.length;
        return arrLen;
    }

    public int setValues(String dataPairStrList) {
        String[] dataArr = dataPairStrList.split(",");
        int len = dataArr.length;
        double lastY = 0;
        int lastDir = 0;
        double x, y;
        int i = 0;
        len = len / 2;
 //       len = len > arrLen ? arrLen : len;
        if (len > 0) {
            for (i = 0; i < len; i++) {
                try {
                    x = Double.valueOf(dataArr[i * 2]);
                    y = Double.valueOf(dataArr[i * 2 + 1]);
                    xMaxMin.takeNewValue(x);
                    yMaxMin.takeNewValue(y);
                    if (i > 0 && !yConfused) {
                        if (y > lastY)  {
                            if (lastDir < 0) {
                                yConfused = true;
                            }
                            lastDir = 1;
                        }
                        if (y < lastY) {
                            if (lastDir > 0) {
                                yConfused = true;
                            }
                            lastDir = -1;
                        }
                    }
                    lastY = y;
                } catch (NumberFormatException e) {
                    break;
                }
                 xyVect.add(new DoublePoint(x, y));
            }
            yAscending = (lastDir >= 0);
        }
        arrLen = i;
        setDoublePoints();
        return arrLen;
    }

    public int add(DoublePoint point) {
        xyVect.add(point);
        xMaxMin.takeMaxValue(point.x);
        yMaxMin.takeMinValue(point.y);
        arrLen = xyVect.size();
        return arrLen;
    }

    public DoublePoint getDataAt(int i) {
        if (i < arrLen)
            return (DoublePoint)xyVect.get(i);
        else
            return null;
    }

    public double getXat(int i) {
        return getDataAt(i).x;
    }

    public double getYat(int i) {
        return getDataAt(i).y;
    }

    public boolean setYat(int i, double yVal) {
        boolean bRetVal = false;
        if (i >=0 && i < arrLen) {
            getDataAt(i).setYval(yVal);
            bRetVal = true;
        }
        return bRetVal;
    }

    public boolean addToYat(int i, double yVal) {
         boolean bRetVal = false;
         if (i >=0 && i < arrLen) {
             getDataAt(i).addToYval(yVal);
             bRetVal = true;
         }
         return bRetVal;
     }


    private void setDoublePoints() {
        if (doublePoints == null)  {
            doublePoints = new DoublePoint[arrLen];
            for (int i = 0; i < arrLen; i++) {
                doublePoints[i] = getDataAt(i);
            }
        }
    }

    public DoublePoint[] getGraph() {
        if (doublePoints == null)
            setDoublePoints();
        return doublePoints;
    }

    public String getDataPairStr() {
        String retVal = "";
        DoublePoint pt;
        for (int r = 0; r < arrLen; r++) {
            pt = xyVect.get(r);
            retVal += ((r > 0)? "," : "") + pt.x + "," + pt.y;

        }
        return retVal;
    }

    public String getDataPairStr(String fmtX, String fmtY) {
        DecimalFormat dfX = new DecimalFormat(fmtX);
        DecimalFormat dfY = new DecimalFormat(fmtY);
         String retVal = "";
         DoublePoint pt;
         for (int r = 0; r < arrLen; r++) {
             pt = xyVect.get(r);
             retVal += ((r > 0)? "," : "") + dfX.format(pt.x) + "," + dfY.format(pt.y);

         }
         return retVal;
     }


    public double getXat(double dy) {
        setDoublePoints();
        if (!dirChecked)
            checkYdirection();
        if (yConfused)
            return Double.NaN;
        if (yAscending)
            return getXatYasc(dy);
        else
            return getXatYdsc(dy);
    }

    double getXatYasc(double dy) {
         double val;
        int iBase = -1;
         int i = 0;
        for (i = 0; i < arrLen; i++) {
            if (doublePoints[i].y > dy) {
                iBase = i;
                break;
            }
        }
        if (iBase < arrLen && iBase > 0) {
            val = doublePoints[iBase].x - (doublePoints[iBase].x - doublePoints[iBase - 1].x) /
                    (doublePoints[iBase].y - doublePoints[iBase - 1].y) *
                        (doublePoints[iBase].y - dy);
        }
        else {
            if (iBase == 0)
                val = doublePoints[0].x;
            else
                val = doublePoints[arrLen - 1].x;
        }
         return val;
    }

    double getXatYdsc(double dy) {
        setDoublePoints();
        double val;
        int iBase = -1;
         int i = arrLen - 1;
        for (i = arrLen - 1; i >= 0; i--) {
            if (doublePoints[i].y > dy) {
                iBase = i;
                break;
            }
        }
        if (iBase < (arrLen - 1) && iBase >= 0) {
            val = doublePoints[iBase].x - (doublePoints[iBase].x - doublePoints[iBase + 1].x) /
                    (doublePoints[iBase].y - doublePoints[iBase + 1].y) *
                        (doublePoints[iBase].y - dy);
        }
        else {
            if (iBase == (arrLen - 1))
                val = doublePoints[arrLen - 1].x;
            else
                val = doublePoints[0].x;
        }
         return val;
    }

     public double getYat(double dx) {
         setDoublePoints();
        // it is assumed here that the x is uni-directional  ASC
        double diff;
        double val;
        int iBase = -1;
         int i = 0;
        for (i = 0; i < arrLen; i++) {
            if (doublePoints[i].x > dx) {
                iBase = i;
                break;
            }
        }
        if (iBase < arrLen && iBase > 0) {
            val = doublePoints[iBase].y - (doublePoints[iBase].y - doublePoints[iBase - 1].y) /
                    (doublePoints[iBase].x - doublePoints[iBase - 1].x) *
                        (doublePoints[iBase].x - dx);
        }
        else {
            if (iBase == 0)
                val = doublePoints[0].y;
            else
                val = doublePoints[arrLen - 1].y;
        }
         return val;
    }

    void checkYdirection() {
        yConfused = false;
        double lastY, nowY;
        if (xyVect.size() > 1) {
            lastY = xyVect.get(0).y;
            nowY = xyVect.get(1).y;
            yAscending =( nowY > lastY);
            lastY = nowY;
            for (int p = 2; p < xyVect.size(); p++){
                nowY = xyVect.get(p).y;
                if (!(yAscending == ( nowY > lastY))) {
                    yConfused = true;
                    break;
                }
            }
        }
    }

     public double getXmax() {
        return xMaxMin.getMax();
    }

    public double getXmin() {
        return xMaxMin.getMin();
    }

    public double getYmax() {
        return yMaxMin.getMax();
    }

    public double getYmin() {
        return yMaxMin.getMin();
    }


    public boolean isXinRange(double dx) {
        if (dx >= getXmin() && dx <= getXmax())
            return true;
        else
            return false;
    }

    public boolean isYinRange(double dy) {
         if (dy >= getYmin() && dy <= getYmax())
             return true;
         else
             return false;
     }

 }