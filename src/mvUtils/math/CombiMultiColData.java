package mvUtils.math;

import mvUtils.display.*;

/**
 * Created by IntelliJ IDEA.
 * User: M Viswanathan
 * Date: 8/30/12
 * Time: 5:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class CombiMultiColData extends GraphInfoAdapter {
    MultiColData one, two;
    int cols;
    CombinedGraphInfo combinedGraphInfo;

    public CombiMultiColData(MultiColData one, MultiColData two) {
        this.one = one;
        this.two = two;
        cols = one.cols + two.cols;
    }

    public double[][] getDataArray(int[] colList) {
        double[][] dataArr = null;
        boolean allAvailable = true;
        for (int c:colList) {
            if (c >= cols) {
                allAvailable = false;
                break;
            }
        }
        if (allAvailable) {
            int nRows = one.xVals.length;
            double nowX;
            dataArr = new double[nRows][colList.length + 1];
            for (int r = 0; r < nRows; r++) {
                nowX = one.xVals[r];
                dataArr[r][0] = nowX;
                for (int c = 0; c < colList.length; c++) {
                    dataArr[r][c + 1] = getYat(colList[c], nowX);
                }
            }
        }
        return dataArr;
    }

    public int addTraces(TrendsPanel tp, int count) {
        combinedGraphInfo = new CombinedGraphInfo();
        return combinedGraphInfo.addTraces(this, tp, count);
    }

    @Override
    public TraceHeader getTraceHeader(int trace) {
        return combinedGraphInfo.getTraceHeader(trace);
     }

    @Override
    public DoublePoint[] getGraph(int trace) {
        return combinedGraphInfo.getGraph(trace);
    }

    @Override
    public DoubleRange getXrange(int trace) {
        return getCommonXrange();
    }

    @Override
    public DoubleRange getYrange(int trace) {
        return getCommonYrange();
     }

    @Override
    public DoubleRange getCommonXrange() {
        return one.getCommonXrange();
    }

    @Override
    public DoubleRange getCommonYrange() {
        return one.getCommonYrange();
    }

    @Override
    public double getYat(int trace, double x) {
        return combinedGraphInfo.getYat(trace, x);
    }

    class CombinedGraphInfo extends GraphInfoAdapter {
        MultiColData combinedData;
        int fromOneSize;
        CombinedGraphInfo() {
            combinedData = new MultiColData(one.xName, one.xVals, one.xFormat, one.colWidth);
            for (VariableDataTrace vdt:one.colData.values()) {
                if (vdt.isTraceable())
                    combinedData.addColumn(vdt);
            }
            fromOneSize = combinedData.getColumns();
            for (VariableDataTrace vdt:two.colData.values()) {
                if (vdt.isTraceable())
                    combinedData.addColumn(vdt);
            }
        }

        int addTraces(GraphInfoAdapter grfInfo, TrendsPanel tp, int count){
            int nowCount = 0;
            VariableDataTrace vdt;
            for (int t = 0;t < combinedData.colData.size(); t++)   {
               vdt = combinedData.colData.get(t);
                if (t < fromOneSize)
                    tp.addTrace(grfInfo, t, vdt.color);
                else
                    tp.addTrace(grfInfo, t, vdt.color, GraphDisplay.LineStyle.DASHED);
                nowCount++;
            }
            return count + nowCount;
        }

        @Override
        public double getYat(int trace, double x) {
            return combinedData.getYat(trace, x);
        }

        @Override
        public TraceHeader getTraceHeader(int trace) {
            return combinedData.getTraceHeader(trace);
        }

        @Override
        public DoublePoint[] getGraph(int trace) {
            return combinedData.getGraph(trace);
        }

    }
 }
