package tests.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.utilities.Point;
import main.utilities.Trace;
import main.utilities.TraceGroup;

public class TraceGroupTest{

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
  public void testGetSymbol(){
    TraceGroup traceGroup = new TraceGroup();

    assertEquals(-1, traceGroup.getSymbol(), 0);
  }

  @Test
  public void testSetSymbol(){
    TraceGroup traceGroup = new TraceGroup();

    traceGroup.setSymbol(102);

    assertEquals(102, traceGroup.getSymbol(), 0);
  }
}
