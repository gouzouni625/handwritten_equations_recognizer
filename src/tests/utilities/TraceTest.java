package tests.utilities;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

import main.utilities.Trace;
import main.utilities.Point;

public class TraceTest{

  @Test
  public void testTraceAddPointGetSize(){
    Trace trace = new Trace();

    assertEquals(0, trace.size(), 0);

    ArrayList<Point> points = new ArrayList<Point>();
    int numberOfPoints = 10;
    for(int i = 0;i < numberOfPoints;i++){
      points.add(new Point(i, 2 * i + 3));
    }

    trace = new Trace(points);

    for(int i = 0;i < numberOfPoints;i++){
      assertEquals(i, trace.get(i).x_, 0);
      assertEquals(2 * i + 3, trace.get(i).y_, 0);
    }
  }

  /** \brief The following test will save the image in
   *         data/test/utilities/testToImage.jpg.
   *         Testing is done visually.
   */
  @Test
  public void testToImage(){
    Trace trace = new Trace();

    int numberOfPoints = 100;
    for(int i = 0;i < numberOfPoints;i++){
      trace.addPoint(new Point(i, 2 * i + 3));
    }

    Mat image = trace.toImage(new Size(100, 100));

    Highgui.imwrite("data/test/utilities/testToImage.jpg", image);
  }
}
