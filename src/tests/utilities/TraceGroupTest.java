package tests.utilities;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import main.utilities.Point;
import main.utilities.Trace;
import main.utilities.TraceGroup;

import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

public class TraceGroupTest{

  @Test
  public void testTraceGroupAddTraceGetSize(){
    TraceGroup traceGroup = new TraceGroup();

    assertEquals(0, traceGroup.size(), 0);

    ArrayList<Trace> traces = new ArrayList<Trace>();
    ArrayList<Point> points;

    int numberOfPoints = 10;
    int numberOfTraces = 10;
    for(int i = 0;i < numberOfTraces;i++){

      points = new ArrayList<Point>();
      for(int j = 0;j < numberOfPoints;j++){
        points.add(new Point(i, 2 * j + 3));
      }

      traces.add(new Trace(points));
    }

    traceGroup = new TraceGroup(traces);

    assertEquals(10, traceGroup.size(), 0);

    for(int i = 0;i < numberOfTraces;i++){
      for(int j = 0;j < numberOfPoints;j++){
        assertEquals(i, traceGroup.get(i).get(j).x_, 0);
        assertEquals(2 * j + 3, traceGroup.get(i).get(j).y_, 0);
      }
    }

  }

  @Test
  public void testIndexOf(){
    TraceGroup traceGroup = new TraceGroup();
    Trace trace1 = new Trace();
    Trace trace2 = new Trace();
    Trace trace3 = new Trace();

    int numberOfPoints = 10;
    for(int i = 0;i < numberOfPoints;i++){
      trace1.addPoint(new Point(i, i));
      trace2.addPoint(new Point(i, 1));
      trace3.addPoint(new Point(1, i));
    }

    traceGroup.addTrace(trace1);
    traceGroup.addTrace(trace2);

    assertEquals(0, traceGroup.indexOf(trace1), 0);
    assertEquals(1, traceGroup.indexOf(trace2), 0);
    assertEquals(-1, traceGroup.indexOf(trace3), 0);
  }

  /** \brief The following test will save the image in
   *         data/test/utilities/testToImage.jpg.
   *         Testing should be done visually.
   */
  @Test
  public void testToImage(){
    TraceGroup traceGroup = new TraceGroup();
    ArrayList<Point> points = new ArrayList<Point>();

    int numberOfPoints = 100;
    for(int i = 0;i < numberOfPoints;i++){
      points.add(new Point(i, 0.2 * i + 3));
    }

    traceGroup.addTrace(new Trace(points));
    points = new ArrayList<Point>();

    for(int i = 0;i < numberOfPoints;i++){
      points.add(new Point(i, -0.5 * i + 3));
    }

    traceGroup.addTrace(new Trace(points));

    Mat image = traceGroup.toImage(new Size(1000, 1000));

    Highgui.imwrite("data/test/utilities/testToImage.jpg", image);
  }

  @Test
  public void testSubTraceGroup(){
    TraceGroup traceGroup = new TraceGroup();
    Trace trace1 = new Trace();
    Trace trace2 = new Trace();
    Trace trace3 = new Trace();

    int numberOfPoints = 10;
    for(int i = 0;i < numberOfPoints;i++){
      trace1.addPoint(new Point(i, i));
      trace2.addPoint(new Point(i, 1));
      trace3.addPoint(new Point(1, i));
    }

    traceGroup.addTrace(trace1);
    traceGroup.addTrace(trace2);
    traceGroup.addTrace(trace3);

    int[] tracesIndices = new int[] {0, 2};
    TraceGroup subTraceGroup = traceGroup.subTraceGroup(tracesIndices);

    for(int i = 0;i < numberOfPoints;i++){
      assertEquals(trace1.get(i).x_, subTraceGroup.get(0).get(i).x_, 0);
      assertEquals(trace1.get(i).y_, subTraceGroup.get(0).get(i).y_, 0);

      assertEquals(trace3.get(i).x_, subTraceGroup.get(1).get(i).x_, 0);
      assertEquals(trace3.get(i).y_, subTraceGroup.get(1).get(i).y_, 0);
    }
  }

}
