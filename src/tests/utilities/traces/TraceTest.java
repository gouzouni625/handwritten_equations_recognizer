package tests.utilities.traces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

import main.utilities.traces.Point;
import main.utilities.traces.Trace;

/** @class TraceTest
 *
 *  @brief Class that contains tests for Trace class.
 *
 */
public class TraceTest{

  /**
   *  @brief Tests the constructors of Trace class.
   */
  @Test
  public void testTrace(){
    // Test default constructor.
    Trace trace1 = new Trace();
    Trace trace2 = new Trace(trace1);

    assertFalse(trace1 == trace2);
  }

  /**
   *  @brief Tests add and size methods of Trace class.
   */
  @Test
  public void testAddSize(){
    double x = Math.random() * 100;
    double y = Math.random() * 100;

    Trace trace = new Trace();
    trace.add(new Point(x, y));

    assertEquals(1, trace.size(), 0);
  }

  /**
   *  @brief Tests get method of Trace class.
   */
  @Test
  public void testGet(){
    double x = Math.random() * 100;
    double y = Math.random() * 100;

    Trace trace = new Trace();
    trace.add(new Point(x, y));

    assertEquals(x, trace.get(0).x_, 0);
    assertEquals(y, trace.get(0).y_, 0);

    // Check that get returns the actual Point and not a copy.
    Point point = trace.get(0);

    point.x_++;
    point.y_++;

    assertEquals(x + 1, trace.get(0).x_, 0);
    assertEquals(y + 1, trace.get(0).y_, 0);
  }

  /**
   *  @brief Tests multiplyBy method of Trace class.
   */
  @Test
  public void testMultiplyBy(){
    double x1 = Math.random() * 100;
    double y1 = Math.random() * 100;
    double x2 = Math.random() * 100;
    double y2 = Math.random() * 100;
    double factor = Math.random() * 100;

    Trace trace = new Trace();
    trace.add(new Point(x1, y1));
    trace.add(new Point(x2, y2));

    trace.multiplyBy(factor);

    assertEquals(x1 * factor, trace.get(0).x_, 0);
    assertEquals(y1 * factor, trace.get(0).y_, 0);
    assertEquals(x2 * factor, trace.get(1).x_, 0);
    assertEquals(y2 * factor, trace.get(1).y_, 0);
  }

  /**
   *  @brief Tests subtract method of Trace class.
   */
  @Test
  public void testSubtract(){
    double x1 = Math.random() * 100;
    double y1 = Math.random() * 100;
    double x2 = Math.random() * 100;
    double y2 = Math.random() * 100;
    double x3 = Math.random() * 100;
    double y3 = Math.random() * 100;

    Trace trace = new Trace();
    trace.add(new Point(x1, y1));
    trace.add(new Point(x2, y2));

    trace.subtract(new Point(x3, y3));

    assertEquals(x1 - x3, trace.get(0).x_, 0);
    assertEquals(y1 - y3, trace.get(0).y_, 0);
    assertEquals(x2 - x3, trace.get(1).x_, 0);
    assertEquals(y2 - y3, trace.get(1).y_, 0);
  }

  /**
   *  @brief Tests calculateCorners, getTopLeftCorner, getBottomRightCorner, getBottomLeftCorner, getTopRightCorner,
   *  getWidth and getHeight methods of Trace class.
   */
  @Test
  public void testCalculateCorners(){
    int numberOfPoints = 10;
    ArrayList<Double> xList = new ArrayList<Double>();
    ArrayList<Double> yList = new ArrayList<Double>();

    Trace trace = new Trace();

    for(int i = 0;i < numberOfPoints;i++){
      double x = Math.random() * 100;
      double y = Math.random() * 100;

      xList.add(x);
      yList.add(y);

      trace.add(new Point(x, y));
    }

    double minX = Collections.min(xList);
    double maxX = Collections.max(xList);
    double minY = Collections.min(yList);
    double maxY = Collections.max(yList);

    trace.calculateCorners();

    Point topLeftCorner = trace.getTopLeftCorner();
    Point bottomRightCorner = trace.getBottomRightCorner();
    Point bottomLeftCorner = trace.getBottomLeftCorner();
    Point topRightCorner = trace.getTopRightCorner();

    assertEquals(minX, topLeftCorner.x_, 0);
    assertEquals(maxY, topLeftCorner.y_, 0);

    assertEquals(maxX, bottomRightCorner.x_, 0);
    assertEquals(minY, bottomRightCorner.y_, 0);

    assertEquals(minX, bottomLeftCorner.x_, 0);
    assertEquals(minY, bottomLeftCorner.y_, 0);

    assertEquals(maxX, topRightCorner.x_, 0);
    assertEquals(maxY, topRightCorner.y_, 0);

    assertEquals(maxX - minX, trace.getWidth(), 0);
    assertEquals(maxY - minY, trace.getHeight(), 0);
  }

  /**
   *  @brief Tests getCentroid method of Trace class.
   */
  @Test
  public void testGetCentroid(){
    Trace trace = new Trace();
    trace.add(new Point(0, 0));
    trace.add(new Point(0, 1));
    trace.add(new Point(1, 0));
    trace.add(new Point(1, 1));

    Point centroid = trace.getCentroid();

    assertEquals(0.5, centroid.x_, 0);
    assertEquals(0.5, centroid.y_, 0);
  }

  /**
   *  @brief Tests getCenterOfMass method of Trace class.
   */
  @Test
  public void testGetCenterOfMass(){
    Trace trace = new Trace();
    trace.add(new Point(0, 0));
    trace.add(new Point(0, 1));
    trace.add(new Point(1, 0));
    trace.add(new Point(1, 1));

    Point centerOfMass = trace.getCenterOfMass();

    assertEquals(0.5, centerOfMass.x_, 0);
    assertEquals(0.5, centerOfMass.y_, 0);
  }

  /**
   *  @brief Tests minimumDistance method of Trace class.
   */
  @Test
  public void testMinimumDistance(){
    Trace trace1 = new Trace();
    trace1.add(new Point(0, 0));
    trace1.add(new Point(0, 1));

    Trace trace2 = new Trace();
    trace2.add(new Point(1, 0));
    trace2.add(new Point(1, 1));

    assertEquals(1, Trace.minimumDistance(trace1, trace2), 0);

    trace2.add(new Point(0, 1));

    assertEquals(0, Trace.minimumDistance(trace1, trace2), 0);
  }

  /**
   *  @brief Tests closestPoints method of Trace class.
   */
  @Test
  public void testClosestPoints(){
    Trace trace1 = new Trace();
    trace1.add(new Point(0, 0));
    trace1.add(new Point(0, 1));

    Trace trace2 = new Trace();
    trace2.add(new Point(2, 2));
    trace2.add(new Point(1, 0));

    Point[] closestPoints = Trace.closestPoints(trace1, trace2);

    assertEquals(trace1.get(0), closestPoints[0]);
    assertEquals(trace2.get(1), closestPoints[1]);
  }

  /**
   *  @brief Tests closestPoint method of Trace class.
   */
  @Test
  public void testClosestPoint(){
    Trace trace = new Trace();
    trace.add(new Point(0, 0));
    trace.add(new Point(0, 1));

    Point point = new Point(1, 0);

    Point closestPoint = trace.closestPoint(point);

    assertEquals(trace.get(0), closestPoint);
  }

  /**
   *  @brief Tests toInkMLFormat method of Trace class.
   */
  @Test
  public void testToInkMLFormat(){
    int numberOfPoints = 10;
    Point[] points = new Point[numberOfPoints];
    Trace trace = new Trace();

    String inkML = new String("<trace>");
    for(int i = 0;i < numberOfPoints;i++){
      double x = Math.random() * 100;
      double y = Math.random() * 100;

      points[i] = new Point(x, y);

      trace.add(new Point(x, y));

      inkML += x + " " + y + ", ";
    }
    inkML = inkML.substring(0, inkML.length() - 2);
    inkML += "</trace>";

    assertEquals(inkML, trace.toInkMLFormat());
  }
}
