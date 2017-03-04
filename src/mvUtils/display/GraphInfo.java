package mvUtils.display;

import mvUtils.math.DoublePoint;
import mvUtils.math.DoubleRange;

import javax.swing.*;
import java.io.Serializable;

public interface GraphInfo
        extends Serializable {
    TraceHeader[] getTraceHeader();

    TraceHeader getTraceHeader(int trace);

    DoubleRange getXrange(int trace);

    DoubleRange getYrange(int trace);

    DoubleRange getCommonXrange();

    DoubleRange getCommonYrange();

    DoubleRange getXrange(int trace, DoubleRange xRange);

    double getYat(int trace, double x);

    DoublePoint[] getGraph(int trace);

    DoubleRange[] getGraph(int trace, double step);

    DoubleRange[] getGraph(int trace, DoubleRange xRange);

    DoubleRange[] getGraph(int trace,
                           DoubleRange xRange, double step);

    DoublePoint[] getReactions();

    String[] getTabularResults(int trace);

    int traceCount();

    JTable getResultTable();

    String getYFormat(int trace);

    String getXFormat();

    String getXFormat(int trace);

}