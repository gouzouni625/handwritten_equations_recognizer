package tests.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.utilities.Trace;
import main.utilities.Point;

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

}
