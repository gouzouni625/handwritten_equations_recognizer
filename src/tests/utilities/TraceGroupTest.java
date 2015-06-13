package tests.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

import main.utilities.Point;
import main.utilities.Trace;
import main.utilities.TraceGroup;

public class TraceGroupTest{

  // Also tests size method.
  @Test
  public void testTraceGroup(){
    TraceGroup traceGroup = new TraceGroup();

    assertEquals(0, traceGroup.size(), 0);
  }

  @Test
  public void testAdd(){
    TraceGroup traceGroup = new TraceGroup();
    traceGroup.add(new Trace());

    assertEquals(1, traceGroup.size(), 0);
  }

  @Test
  public void testGet(){
    TraceGroup traceGroup = new TraceGroup();
    Trace trace = new Trace();
    trace.add(new Point(5, 6));

    traceGroup.add(trace);

    assertEquals(5, traceGroup.get(0).get(0).x_, 0);
    assertEquals(6, traceGroup.get(0).get(0).y_, 0);
  }

  @Test
  public void testTraceGroup2(){
    TraceGroup traceGroup1 = new TraceGroup();

    Trace trace1 = new Trace();
    trace1.add(new Point(1, 2));

    Trace trace2 = new Trace();
    trace2.add(new Point(3, 4));

    traceGroup1.add(trace1);
    traceGroup1.add(trace2);

    TraceGroup traceGroup2 = new TraceGroup(traceGroup1);

    assertEquals(1, traceGroup2.get(0).get(0).x_, 0);
    assertEquals(2, traceGroup2.get(0).get(0).y_, 0);
    assertEquals(3, traceGroup2.get(1).get(0).x_, 0);
    assertEquals(4, traceGroup2.get(1).get(0).y_, 0);

    traceGroup2.get(0).get(0).x_++;
    traceGroup2.get(1).get(0).x_++;

    assertEquals(1, traceGroup1.get(0).get(0).x_, 0);
    assertEquals(2, traceGroup2.get(0).get(0).x_, 0);
    assertEquals(3, traceGroup1.get(1).get(0).x_, 0);
    assertEquals(4, traceGroup2.get(1).get(0).x_, 0);
  }

  @Test
  public void testSubTraceGroup(){
    TraceGroup traceGroup = new TraceGroup();
    Trace trace1 = new Trace();
    Trace trace2 = new Trace();
    Trace trace3 = new Trace();

    int numberOfPoints = 10;
    for(int i = 0;i < numberOfPoints;i++){
      trace1.add(new Point(i, i));
      trace2.add(new Point(i, 1));
      trace3.add(new Point(1, i));
    }

    traceGroup.add(trace1);
    traceGroup.add(trace2);
    traceGroup.add(trace3);

    int[] tracesIndices = new int[] {0, 2};
    TraceGroup subTraceGroup = traceGroup.subTraceGroup(tracesIndices);

    for(int i = 0;i < numberOfPoints;i++){
      assertEquals(trace1.get(i).x_, subTraceGroup.get(0).get(i).x_, 0);
      assertEquals(trace1.get(i).y_, subTraceGroup.get(0).get(i).y_, 0);

      assertEquals(trace3.get(i).x_, subTraceGroup.get(1).get(i).x_, 0);
      assertEquals(trace3.get(i).y_, subTraceGroup.get(1).get(i).y_, 0);
    }
  }

  @Test
  public void testMultiplyBy(){
    TraceGroup traceGroup = new TraceGroup();

    int numberOfTraces = 10;
    int sizeOfTraces = 10;
    int multiplicationFactor = 10;
    for(int i = 0;i < numberOfTraces;i++){
      Trace trace = new Trace();

      for(int j = 0;j < sizeOfTraces;j++){
        trace.add(new Point(i, j));
      }

      traceGroup.add(trace);
    }

    traceGroup.multiplyBy(multiplicationFactor);

    for(int i = 0;i < numberOfTraces;i++){
      for(int j = 0;j < sizeOfTraces;j++){
        assertEquals(i * multiplicationFactor, traceGroup.get(i).get(j).x_, 0);
        assertEquals(j * multiplicationFactor, traceGroup.get(i).get(j).y_, 0);
      }
    }
  }

  @Test
  public void testSubtract(){
    TraceGroup traceGroup = new TraceGroup();

    int numberOfTraces = 10;
    int sizeOfTraces = 10;
    Point point2 = new Point(2, 3);
    for(int i = 0;i < numberOfTraces;i++){
      Trace trace = new Trace();

      for(int j = 0;j < sizeOfTraces;j++){
        trace.add(new Point(i, j));
      }

      traceGroup.add(trace);
    }

    traceGroup.subtract(point2);

    for(int i = 0;i < numberOfTraces;i++){
      for(int j = 0;j < sizeOfTraces;j++){
        assertEquals(i - point2.x_, traceGroup.get(i).get(j).x_, 0);
        assertEquals(j - point2.y_, traceGroup.get(i).get(j).y_, 0);
      }
    }
  }

  // Also tests getWidth, getHeight, getTopLefCorner, getBottomRightCorner.
  @Test
  public void testCalculateCorners(){
    TraceGroup traceGroup = new TraceGroup();

    int numberOfTraces = 10;
    int sizeOfTraces = 10;
    for(int i = 0;i < numberOfTraces;i++){
      Trace trace = new Trace();

      for(int j = 0;j < sizeOfTraces;j++){
        trace.add(new Point(i, j));
      }

      traceGroup.add(trace);
    }

    traceGroup.calculateCorners();

    assertEquals(0, traceGroup.getTopLeftCorner().x_, 0);
    assertEquals(sizeOfTraces - 1, traceGroup.getTopLeftCorner().y_, 0);
    assertEquals(numberOfTraces - 1, traceGroup.getBottomRightCorner().x_, 0);
    assertEquals(0, traceGroup.getBottomRightCorner().y_, 0);
    assertEquals(numberOfTraces - 1, traceGroup.getWidth(), 0);
    assertEquals(sizeOfTraces - 1, traceGroup.getHeight(), 0);
  }

  @Test
  public void testPrint(){
    TraceGroup traceGroup = new TraceGroup();

    int numberOfPoints = 100;

    Trace trace1 = new Trace();
    Trace trace2 = new Trace();

    for(int i = 0;i < numberOfPoints;i++){
      trace1.add(new Point(((double)i) / 100, 0.02 * i + 0.03));
      trace2.add(new Point(((double)i) / 100, -0.05 * i + 0.03));
    }

    traceGroup.add(trace1);
    traceGroup.add(trace2);

    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    Mat image = traceGroup.print(new Size(1000, 1000));

    // The image saved by the following command should show two lines with common beginning.
    //Highgui.imwrite("data/tests/utilities/TraceGroup/testPrint_image.tiff", image);
  }

}
