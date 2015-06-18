package tests.utilities.traces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

import main.utilities.traces.Point;
import main.utilities.traces.Trace;

public class TraceTest{

  // Also tests method size.
  @Test
  public void testTrace(){
    // Test default constructor.
    Trace trace = new Trace();

    assertEquals(0, trace.size(), 0);
  }

  @Test
  public void testAdd(){
    Trace trace = new Trace();
    trace.add(new Point(5, 6));

    assertEquals(1, trace.size(), 0);
  }

  @Test
  public void testGet(){
    Trace trace = new Trace();
    trace.add(new Point(5, 6));

    assertEquals(5, trace.get(0).x_, 0);
    assertEquals(6, trace.get(0).y_, 0);
  }

  @Test
  public void testTrace2(){
    Trace trace1 = new Trace();
    trace1.add(new Point(1, 2));
    trace1.add(new Point(3, 4));

    Trace trace2 = new Trace(trace1);

    assertEquals(1, trace2.get(0).x_, 0);
    assertEquals(2, trace2.get(0).y_, 0);
    assertEquals(3, trace2.get(1).x_, 0);
    assertEquals(4, trace2.get(1).y_, 0);

    trace2.get(0).x_++;
    trace2.get(1).x_++;

    assertEquals(1, trace1.get(0).x_, 0);
    assertEquals(2, trace2.get(0).x_, 0);
    assertEquals(3, trace1.get(1).x_, 0);
    assertEquals(4, trace2.get(1).x_, 0);
  }

  @Test
  public void testMultiplyBy(){
    Trace trace = new Trace();

    int numberOfPoints = 10;
    double multiplicationFactor = 10;
    for(int i = 0;i < numberOfPoints;i++){
      trace.add(new Point(i, i + 10));
    }

    trace.multiplyBy(multiplicationFactor);

    for(int i = 0;i < numberOfPoints;i++){
      assertEquals(i * multiplicationFactor, trace.get(i).x_, 0);
      assertEquals((i + 10) * multiplicationFactor, trace.get(i).y_, 0);
    }
  }

  @Test
  public void testSubtract(){
    Trace trace = new Trace();

    int numberOfPoints = 10;
    Point point2 = new Point(2, 3);
    for(int i = 0;i < numberOfPoints;i++){
      trace.add(new Point(i, i + 10));
    }

    trace.subtract(point2);

    for(int i = 0;i < numberOfPoints;i++){
      assertEquals(i - point2.x_, trace.get(i).x_, 0);
      assertEquals(i + 10 - point2.y_, trace.get(i).y_, 0);
    }
  }

  // Also tests getWidth, getHeight, getTopLeftCorner, getBottomRightCorner.
  @Test
  public void testCalculateCorners(){
    Trace trace = new Trace();

    int numberOfPoints = 10;
    for(int i = 0;i < numberOfPoints;i++){
      trace.add(new Point(i, i + 10));
    }

    trace.calculateCorners();

    assertEquals(0, trace.getTopLeftCorner().x_, 0);
    assertEquals(numberOfPoints - 1 + 10, trace.getTopLeftCorner().y_, 0);
    assertEquals(numberOfPoints - 1, trace.getBottomRightCorner().x_, 0);
    assertEquals(10, trace.getBottomRightCorner().y_, 0);
    assertEquals(numberOfPoints - 1, trace.getWidth(), 0);
    assertEquals(numberOfPoints - 1, trace.getHeight(), 0);
  }

  @Test
  public void testPrint(){
    Trace trace = new Trace();

    int numberOfPoints = 100;
    int thickness = 10;
    for(int i = 0;i < numberOfPoints;i++){
      trace.add(new Point(i, 2 * i + 3));
    }

    trace.calculateCorners();

    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    Mat image = Mat.zeros(new Size(trace.getWidth(), trace.getHeight()), CvType.CV_32F);
    trace.print(image, thickness);

    // The image saved by the following command should be a line with positive slope.
    //Highgui.imwrite("data/tests/utilities/traces/Trace/testPrint_image.tiff", image);
  }

  @Test
  public void testGetCentroid(){
    Trace trace = new Trace();

    int numberOfPoints = 100;
    for(int i = 0;i < numberOfPoints;i++){
      trace.add(new Point(i, 2 * i + 3));
    }

    Point centroid = trace.getCentroid();

    assertEquals(49.5, centroid.x_, 0);
    assertEquals(102, centroid.y_, 0);
  }

  @Test
  public void testAreOverlapped(){
    int numberOfPoints = 100;

    Trace trace1 = new Trace();
    Trace trace2 = new Trace();

    for(int i = 0;i < numberOfPoints;i++){
      trace1.add(new Point(i, 2 * i + 3));
      trace2.add(new Point(i, -0.5 * i - 10));
    }

    assertFalse(Trace.areOverlapped(trace1, trace2));

    trace1 = new Trace();
    trace2 = new Trace();

    for(int i = 0;i < numberOfPoints;i++){
      trace1.add(new Point(i, 2 * i + 3));
      trace2.add(new Point(i - numberOfPoints / 2, -0.5 * i + 6));
    }

    assertTrue(Trace.areOverlapped(trace1, trace2));
  }

}
