package tests.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.utilities.Point;

public class PointTest{

  @Test
  public void testPoint(){
    Point point = new Point();

    assertEquals(0, point.x_, 0);
    assertEquals(0, point.y_, 0);

    point = new Point(5, 6);

    assertEquals(5, point.x_, 0);
    assertEquals(6, point.y_, 0);
  }

  @Test
  public void testMultiplyBy(){
    Point point = new Point(5, 6);

    point.multiplyBy(5);

    assertEquals(25, point.x_, 0);
    assertEquals(30, point.y_, 0);
  }

  @Test
  public void testSubstract(){
    Point point = new Point(5, 6);

    point.substract(4);

    assertEquals(1, point.x_, 0);
    assertEquals(2, point.y_, 0);

    Point point2 = new Point(4, 2);
    point.substract(point2);

    assertEquals(-3, point.x_, 0);
    assertEquals(0, point.y_, 0);
  }
}
