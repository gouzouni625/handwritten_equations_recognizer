package test.java.utilities.traces;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.java.utilities.traces.Point;

/** @class PointTest
 *
 *  @brief Class that contains tests for main.utilities.traces.Point class.
 *
 */
public class PointTest{

  /**
   *  @brief Tests the constructors of main.utilities.traces.Point class.
   */
  @Test
  public void testPoint(){
    // Test the default constructor.
    Point point = new Point();
    assertEquals(0, point.x_, 0);
    assertEquals(0, point.y_, 0);

    // Test the constructor the takes two doubles as input.
    double x = Math.random() * 100;
    double y = Math.random() * 100;
    point = new Point(x, y);
    assertEquals(x, point.x_, 0);
    assertEquals(y, point.y_, 0);

    // Test the constructor that takes another point as input.
    Point point2 = new Point(point);
    assertEquals(point.x_, point2.x_, 0);
    assertEquals(point.y_, point2.y_, 0);

    // Change the data of point2 and check that the data of point do not change.
    point2.x_++;
    point2.y_++;
    assertEquals(x, point.x_, 0);
    assertEquals(y, point.y_, 0);
    assertEquals(x + 1, point2.x_, 0);
    assertEquals(y + 1, point2.y_, 0);
  }

  /**
   *  @brief Tests add method of main.utilities.traces.Point class.
   */
  @Test
  public void testAdd(){
    double x1 = Math.random() * 100;
    double y1 = Math.random() * 100;
    double x2 = Math.random() * 100;
    double y2 = Math.random() * 100;

    Point point1 = new Point(x1, y1);
    Point point2 = new Point(x2, y2);
    point1.add(point2);

    assertEquals(x1 + x2, point1.x_, 0);
    assertEquals(y1 + y2, point1.y_, 0);
  }

  /**
   *  @brief Tests MultiplyBy method of main.utilities.traces.Point class.
   */
  @Test
  public void testMultiplyBy(){
    double x = Math.random() * 100;
    double y = Math.random() * 100;
    double factor = Math.random() * 100;

    Point point = new Point(x, y);

    point.multiplyBy(factor);

    assertEquals(x * factor, point.x_, 0);
    assertEquals(y * factor, point.y_, 0);

    point.multiplyBy(-1);

    assertEquals(-(x * factor), point.x_, 0);
    assertEquals(-(y * factor), point.y_, 0);

    point.multiplyBy(0);

    assertEquals(0, point.x_, 0);
    assertEquals(0, point.y_, 0);
  }

  /**
   *  @brief Tests divideBy method of main.utilities.traces.Point class.
   */
  @Test
  public void testDivideBy(){
    double x = Math.random() * 100;
    double y = Math.random() * 100;
    double factor = Math.random() * 100 + 1; // Make sure factor is not equal to zero.

    Point point = new Point(x, y);

    point.divideBy(factor);

    assertEquals(x / factor, point.x_, 0);
    assertEquals(y / factor, point.y_, 0);

    point.divideBy(-1);

    assertEquals(-(x / factor), point.x_, 0);
    assertEquals(-(y / factor), point.y_, 0);
  }

  /**
   *  @brief Tests divideBy method of main.utilities.traces.Point class.
   *
   *  Concretely, tests if the right exception is thrown in case factor == 0.
   */
  @Test(expected = ArithmeticException.class)
  public void testDivideByException(){
    double x = Math.random() * 100;
    double y = Math.random() * 100;
    double factor = 0;

    Point point = new Point(x, y);

    point.divideBy(factor);
  }

  /**
   *  @brief Tests subtract method of main.utilities.traces.Point class.
   */
  @Test
  public void testSubtract(){
    double x1 = Math.random() * 100;
    double y1 = Math.random() * 100;
    double x2 = Math.random() * 100;
    double y2 = Math.random() * 100;

    Point point1 = new Point(x1, y1);
    Point point2 = new Point(x2, y2);

    point1.subtract(point2);

    assertEquals(x1 - x2, point1.x_, 0);
    assertEquals(y1 - y2, point1.y_, 0);

    point1.subtract(point1);

    assertEquals(0, point1.x_, 0);
    assertEquals(0, point1.y_, 0);
  }

  /**
   *  @brief Tests distance method of main.utilities.traces.Point class.
   */
  @Test
  public void testDistance(){
    double x1 = Math.random() * 100;
    double y1 = Math.random() * 100;
    double x2 = Math.random() * 100;
    double y2 = Math.random() * 100;
    double distance = Math.pow((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2), (double)1/ 2);

    Point point1 = new Point(x1, y1);
    Point point2 = new Point(x2, y2);

    assertEquals(distance, Point.distance(point1, point2), 0);
    assertEquals(0, Point.distance(point1, point1), 0);
  }

  /**
   *  @brief Tests chain operations with main.utilities.traces.Point objects.
   */
  @Test
  public void testChainOperations(){
    double x1 = Math.random() * 100;
    double y1 = Math.random() * 100;
    double x2 = Math.random() * 100;
    double y2 = Math.random() * 100;
    double factor = Math.random() * 100;

    Point point1 = new Point(x1, y1);
    Point point2 = new Point(x2, y2);

    Point point3 = point1.multiplyBy(factor).subtract(point2);

    assertEquals(x1 * factor - x2, point3.x_, 0);
    assertEquals(y1 * factor - y2, point3.y_, 0);
  }

}
