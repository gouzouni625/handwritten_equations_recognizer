package tests.utilities.traces;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.utilities.traces.Point;

public class PointTest{

  @Test
  public void testPoint(){
    // Test the default constructor.
    Point point = new Point();

    assertEquals(0, point.x_, 0);
    assertEquals(0, point.y_, 0);

    // Test the constructor the takes two doubles as input.
    point = new Point(5, 6);

    assertEquals(5, point.x_, 0);
    assertEquals(6, point.y_, 0);

    // Test the constructor that takes another point as input.
    Point point2 = new Point(point);

    assertEquals(point.x_, point2.x_, 0);
    assertEquals(point.y_, point2.y_, 0);

    point2.x_++;
    point2.y_++;

    assertEquals(5, point.x_, 0);
    assertEquals(6, point.y_, 0);
    assertEquals(6, point2.x_, 0);
    assertEquals(7, point2.y_, 0);
  }

  @Test
  public void testMultiplyBy(){
    Point point = new Point(5, 6);

    point.multiplyBy(5);

    assertEquals(25, point.x_, 0);
    assertEquals(30, point.y_, 0);

    point.multiplyBy(-1);

    assertEquals(-25, point.x_, 0);
    assertEquals(-30, point.y_, 0);

    point.multiplyBy(0);

    assertEquals(0, point.x_, 0);
    assertEquals(0, point.y_, 0);
  }

  @Test
  public void testSubtract(){
    Point point = new Point(5, 6);
    Point point2 = new Point(4, 2);
    point.subtract(point2);

    assertEquals(1, point.x_, 0);
    assertEquals(4, point.y_, 0);

    point.subtract((new Point(point)).multiplyBy(2));

    assertEquals(-1, point.x_, 0);
    assertEquals(-4, point.y_, 0);

    point.subtract(point);

    assertEquals(0, point.x_, 0);
    assertEquals(0, point.y_, 0);
  }

  @Test
  public void testDistance(){
    Point point1 = new Point(5, 6);
    Point point2 = new Point(8, 10);

    assertEquals(5, Point.distance(point1, point2), 0);
    assertEquals(0, Point.distance(point1, point1), 0);

    Point point3 = new Point(3, 4);
    Point zero = new Point();

    assertEquals(5, Point.distance(point3, zero), 0);
  }

  @Test
  public void testChainOperations(){
    Point point = new Point(10, 15);

    point = point.multiplyBy(2).subtract(new Point(1, 1));

    assertEquals(19, point.x_, 0);
    assertEquals(29, point.y_, 0);
  }

}
