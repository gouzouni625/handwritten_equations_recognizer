package org.hwer.engine.utilities.traces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

/** @class TraceGroupTest
 *
 *  @brief Class that contains tests for main.java.utilities.traces.TraceGroup class.
 */
public class TraceGroupTest{

  /**
   *  @brief Tests the constructors of main.java.utilities.traces.TraceGroup class.
   */
  @Test
  public void testTraceGroup(){
    TraceGroup traceGroup1 = new TraceGroup();
    TraceGroup traceGroup2 = new TraceGroup(traceGroup1);

    assertFalse(traceGroup1 == traceGroup2);
  }

  /**
   *  @brief Tests add and size methods of main.java.utilities.traces.TraceGroup class.
   */
  @Test
  public void testAddSize(){
    TraceGroup traceGroup = new TraceGroup();
    traceGroup.add(new Trace());

    assertEquals(1, traceGroup.size(), 0);

    traceGroup.add((new TraceGroup()).add(new Trace()).add(new Trace()));

    assertEquals(3, traceGroup.size(), 0);
  }

  /**
   *  @brief Tests get method of main.java.utilities.traces.TraceGroup class.
   */
  @Test
  public void testGet(){
    double x = Math.random() * 100;
    double y = Math.random() * 100;
    double factor = Math.random() * 100;

    TraceGroup traceGroup = new TraceGroup();
    traceGroup.add((new Trace()).add(new Point(x, y)));

    assertEquals(x, traceGroup.get(0).get(0).x_, 0);
    assertEquals(y, traceGroup.get(0).get(0).y_, 0);

    // Check that get returns the actual Trace and not a copy.
    Trace trace = traceGroup.get(0);

    trace.multiplyBy(factor);

    assertEquals(x * factor, traceGroup.get(0).get(0).x_, 0);
    assertEquals(y * factor, traceGroup.get(0).get(0).y_, 0);
  }

  /**
   *  @brief Tests subTraceGroup method of main.java.utilities.traces.TraceGroup class.
   */
  @Test
  public void testSubTraceGroup(){
    int numberOfTraces = 10;
    Trace[] traces = new Trace[numberOfTraces];
    TraceGroup traceGroup = new TraceGroup();

    for(int i = 0;i < numberOfTraces;i++){
      traces[i] = new Trace();

      traceGroup.add(traces[i]);
    }

    TraceGroup traceGroup2 = traceGroup.subTraceGroup(new int[] {2, 4, 7});

    assertEquals(3, traceGroup2.size(), 0);

    assertEquals(traces[1].toInkMLFormat(), traceGroup2.get(0).toInkMLFormat());
    assertEquals(traces[4].toInkMLFormat(), traceGroup2.get(1).toInkMLFormat());
    assertEquals(traces[7].toInkMLFormat(), traceGroup2.get(2).toInkMLFormat());
  }

  /**
   *  @brief Tests multiplyBy method of main.java.utilities.traces.TraceGroup class.
   */
  @Test
  public void testMultiplyBy(){
    double x11 = Math.random() * 100;
    double y11 = Math.random() * 100;
    double x21 = Math.random() * 100;
    double y21 = Math.random() * 100;
    double factor = Math.random() * 100;

    Trace trace1 = new Trace();
    trace1.add(new Point(x11, y11));

    Trace trace2 = new Trace();
    trace2.add(new Point(x21, y21));

    TraceGroup traceGroup = new TraceGroup();
    traceGroup.add(trace1).add(trace2);

    traceGroup.multiplyBy(factor);

    assertEquals(x11 * factor, traceGroup.get(0).get(0).x_, 0);
    assertEquals(y11 * factor, traceGroup.get(0).get(0).y_, 0);

    assertEquals(x21 * factor, traceGroup.get(1).get(0).x_, 0);
    assertEquals(y21 * factor, traceGroup.get(1).get(0).y_, 0);
  }

  /**
   *  @brief Tests subtract method of main.java.utilities.traces.TraceGroup class.
   */
  @Test
  public void testSubtract(){
    double x1 = Math.random() * 100;
    double y1 = Math.random() * 100;
    double x2 = Math.random() * 100;
    double y2 = Math.random() * 100;
    double x3 = Math.random() * 100;
    double y3 = Math.random() * 100;

    Trace trace1 = new Trace();
    trace1.add(new Point(x1, y1));

    Trace trace2 = new Trace();
    trace2.add(new Point(x2, y2));

    TraceGroup traceGroup = new TraceGroup();
    traceGroup.add(trace1).add(trace2);

    traceGroup.subtract(new Point(x3, y3));

    assertEquals(x1 - x3, traceGroup.get(0).get(0).x_, 0);
    assertEquals(y1 - y3, traceGroup.get(0).get(0).y_, 0);

    assertEquals(x2 - x3, traceGroup.get(1).get(0).x_, 0);
    assertEquals(y2 - y3, traceGroup.get(1).get(0).y_, 0);
  }

  /**
   *  @brief Tests calculateCorners, getToLeftCorner, getBottomRightCorner, getBottomLeftCorner, getTopRightCorner,
   *  getWidth, getHeight and getArea methods of main.java.utilities.traces.TraceGroup class.
   */
  @Test
  public void testCalculateCorners(){
    int numberOfPoints = 10;
    ArrayList<Double> xList = new ArrayList<Double>();
    ArrayList<Double> yList = new ArrayList<Double>();

    TraceGroup traceGroup = new TraceGroup();

    for(int i = 0;i < numberOfPoints;i++){
      double x = Math.random() * 100;
      double y = Math.random() * 100;

      xList.add(x);
      yList.add(y);

      traceGroup.add((new Trace()).add(new Point(x, y)));
    }

    double minX = Collections.min(xList);
    double maxX = Collections.max(xList);
    double minY = Collections.min(yList);
    double maxY = Collections.max(yList);

    traceGroup.calculateCorners();

    Point topLeftCorner = traceGroup.getTopLeftCorner();
    Point bottomRightCorner = traceGroup.getBottomRightCorner();
    Point bottomLeftCorner = traceGroup.getBottomLeftCorner();
    Point topRightCorner = traceGroup.getTopRightCorner();

    assertEquals(minX, topLeftCorner.x_, 0);
    assertEquals(maxY, topLeftCorner.y_, 0);

    assertEquals(maxX, bottomRightCorner.x_, 0);
    assertEquals(minY, bottomRightCorner.y_, 0);

    assertEquals(minX, bottomLeftCorner.x_, 0);
    assertEquals(minY, bottomLeftCorner.y_, 0);

    assertEquals(maxX, topRightCorner.x_, 0);
    assertEquals(maxY, topRightCorner.y_, 0);

    assertEquals(maxX - minX, traceGroup.getWidth(), 0);
    assertEquals(maxY - minY, traceGroup.getHeight(), 0);
    assertEquals((maxX - minX) * (maxY - minY), traceGroup.getArea(), 0);
  }

  /**
   *  @brief Tests getCentroid method of main.java.utilities.traces.TraceGroup class.
   */
  @Test
  public void testGetCentroid(){
    TraceGroup traceGroup = new TraceGroup();
    traceGroup.add((new Trace()).add(new Point(0, 0)));
    traceGroup.add((new Trace()).add(new Point(0, 1)));
    traceGroup.add((new Trace()).add(new Point(1, 0)));
    traceGroup.add((new Trace()).add(new Point(1, 1)));

    Point centroid = traceGroup.getCentroid();

    assertEquals(0.5, centroid.x_, 0);
    assertEquals(0.5, centroid.y_, 0);
  }

  /**
   *  @brief Tests getCenterOfMass method of main.java.utilities.traces.TraceGroup class.
   */
  @Test
  public void testGetCenterOfMass(){
    TraceGroup traceGroup = new TraceGroup();
    traceGroup.add((new Trace()).add(new Point(0, 0)));
    traceGroup.add((new Trace()).add(new Point(0, 1)));
    traceGroup.add((new Trace()).add(new Point(1, 0)));
    traceGroup.add((new Trace()).add(new Point(1, 1)));

    Point centroid = traceGroup.getCenterOfMass();

    assertEquals(0.5, centroid.x_, 0);
    assertEquals(0.5, centroid.y_, 0);
  }

  /**
   *  @brief Tests minimumDistance method of main.java.utilities.traces.TraceGroup class.
   */
  @Test
  public void testMinimumDistance(){
    TraceGroup traceGroup1 = new TraceGroup();
    traceGroup1.add((new Trace()).add(new Point(0, 0)));
    traceGroup1.add((new Trace()).add(new Point(0, 1)));

    TraceGroup traceGroup2 = new TraceGroup();
    traceGroup2.add((new Trace()).add(new Point(1, 0)));
    traceGroup2.add((new Trace()).add(new Point(1, 1)));

    assertEquals(1, TraceGroup.minimumDinstance(traceGroup1, traceGroup2), 0);

    traceGroup2.add((new Trace()).add(new Point(0, 1)));

    assertEquals(0, TraceGroup.minimumDinstance(traceGroup1, traceGroup2), 0);
  }

  /**
   *  @brief Tests closestTraces method of main.java.utilities.traces.TraceGroup class.
   */
  @Test
  public void testClosestTraces(){
    TraceGroup traceGroup1 = new TraceGroup();
    traceGroup1.add((new Trace()).add(new Point(0, 0)));
    traceGroup1.add((new Trace()).add(new Point(0, 1)));

    TraceGroup traceGroup2 = new TraceGroup();
    traceGroup2.add((new Trace()).add(new Point(2, 2)));
    traceGroup2.add((new Trace()).add(new Point(1, 0)));

    Trace[] closestPoints = TraceGroup.closestTraces(traceGroup1, traceGroup2);

    assertEquals(traceGroup1.get(0), closestPoints[0]);
    assertEquals(traceGroup2.get(1), closestPoints[1]);
  }

  /**
   *  @brief Tests closestPoints method of main.java.utilities.traces.TraceGroup class.
   */
  @Test
  public void testClosestPoints(){
    TraceGroup traceGroup1 = new TraceGroup();
    traceGroup1.add((new Trace()).add(new Point(0, 0)));
    traceGroup1.add((new Trace()).add(new Point(0, 1)));

    TraceGroup traceGroup2 = new TraceGroup();
    traceGroup2.add((new Trace()).add(new Point(2, 2)));
    traceGroup2.add((new Trace()).add(new Point(1, 0)));

    Point[] closestPoints = TraceGroup.closestPoints(traceGroup1, traceGroup2);

    assertEquals(traceGroup1.get(0).get(0), closestPoints[0]);
    assertEquals(traceGroup2.get(1).get(0), closestPoints[1]);
  }

}
