package tests.partitioners;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import main.partitioners.MSTPartitioner;
import main.partitioners.NNMSTPartitioner;
import main.utilities.Point;
import main.utilities.Trace;
import main.utilities.TraceGroup;

public class MSTPartitionerTest{

  /*
   * TODO
   * Maybe embed the neural network inside the test(e.g. hard code it like the trace group).
   */
  @Test
  public void testPartition(){
    String traceGroups = "<traceGroup xml:id=\"traceGroup1\" contextRef=\"#context1\"><trace>4.24 3.94, 4.06 3.6, 3.66 3.38, 3.1 3.36, 2.68 3.52, 2.68 3.98, 3.1 4.64, 3.68 5.02, 4.16 5.04, 4.34 4.56, 4.34 3.48, 4.06 2.5, 3.42 1.64, 2.82 1.48, 2.42 1.46, 3.32 1.64, 4.44 1.66, 5.16 1.32, 5.26 1.18</trace><trace>5.92 4.5, 6.28 4.86, 6.68 4.96, 7.06 4.92, 7.26 4.4, 7.24 3.68, 6.8 3.26, 6.42 3.24, 6.62 3.46, 7.38 3.46, 7.64 3.06, 7.66 2.48, 7.38 1.88, 6.9 1.7, 6.54 1.66, 6.36 1.64, 6.3 1.66, 6.3 1.74</trace><trace>9.86 5.16, 9.16 5.1, 8.46 5.1, 8.3 5, 8.56 4.22, 8.66 3.58, 8.78 3.5, 9.38 3.7, 9.86 3.66, 10 3.3, 10 2.76, 9.78 2.18, 9.58 1.96, 9.5 1.94, 9.42 1.94, 9.34 1.94</trace></traceGroup>";

    TraceGroup expression = new TraceGroup();
    int startOfTrace = traceGroups.indexOf("<trace>");
    int endOfTrace = traceGroups.indexOf("</trace>");
    while(startOfTrace != -1){
      String[] traceData = traceGroups.substring(startOfTrace + 7, endOfTrace).split(", "); // ("<trace>").length = 7.

      Trace trace = new Trace();
      for(int i = 0;i < traceData.length;i++){
        double x = Double.parseDouble(traceData[i].split(" ")[0]);
        double y = Double.parseDouble(traceData[i].split(" ")[1]);
        trace.add(new Point(x, y));
      }
      expression.add(trace);

      traceGroups = traceGroups.substring(endOfTrace + 8); // ("</trace>").length = 8.
      startOfTrace = traceGroups.indexOf("<trace>");
      endOfTrace = traceGroups.indexOf("</trace>");
    }

    try {
      NNMSTPartitioner partitioner = new NNMSTPartitioner(new int[] {784, 30, 10}, new String("data/test/partitioners/MSTPartitioner/testPartition/network_tr600_e200_b60_g025_mixed255-345_distort"));

      TraceGroup[] partition = partitioner.partition(expression);

      assertEquals(3, partition.length, 0);
    } catch (IOException exception){
      exception.printStackTrace();
    }
  }

  @Test
  public void testIsEligible() throws IllegalArgumentException,
                                      InvocationTargetException,
                                      IllegalAccessException,
                                      NoSuchMethodException,
                                      SecurityException{
    Class[] arguments = new Class[3];
    arguments[0] = int[].class;
    arguments[1] = int[][].class;
    arguments[2] = int.class;
    Method isEligible = MSTPartitioner.class.getDeclaredMethod("isEligible", arguments);
    isEligible.setAccessible(true);

    int[][] paths = new int[10][];
    int numberOfTraces = 5;
    paths[0] = new int[] {0, 1};
    paths[1] = new int[] {1, 2, 3, 4};
    paths[2] = new int[] {2, 4};
    paths[3] = new int[] {1, 4};
    paths[4] = new int[] {0, 1, 3};
    paths[5] = new int[] {0};
    paths[6] = new int[] {1};
    paths[7] = new int[] {2};
    paths[8] = new int[] {3};
    paths[9] = new int[] {4};


    int[] partition = new int[] {4, 2};

    NNMSTPartitioner partitioner = new NNMSTPartitioner(new int[] {1, 2, 3});
    assertTrue((boolean)(isEligible.invoke(partitioner, partition, paths, numberOfTraces)));

    partition = new int[] {0, 1};

    assertFalse((boolean)(isEligible.invoke(partitioner, partition, paths, numberOfTraces)));

    partition = new int[] {1, 5};

    assertTrue((boolean)(isEligible.invoke(partitioner, partition, paths, numberOfTraces)));

    partition = new int[] {5, 6, 7, 8, 9};

    assertTrue((boolean)(isEligible.invoke(partitioner, partition, paths, numberOfTraces)));
  }

  @Test
  public void testCalculateDistancesBetweenTraces() throws IllegalArgumentException,
                                                           InvocationTargetException,
                                                           IllegalAccessException,
                                                           NoSuchMethodException,
                                                           SecurityException{
    Class[] arguments = new Class[1];
    arguments[0] = TraceGroup.class;
    Method calculateDistancesBetweenTraces = MSTPartitioner.class.
       getDeclaredMethod("calculateDistancesBetweenTraces", arguments);

    calculateDistancesBetweenTraces.setAccessible(true);

    TraceGroup traceGroup = new TraceGroup();
    int numberOfTraces = 5;
    for(int i = 0;i < numberOfTraces;i++){
      traceGroup.add(new Trace());
    }

    NNMSTPartitioner partitioner = new NNMSTPartitioner(new int[] {1, 2, 3});
    double[] distances = (double[])(calculateDistancesBetweenTraces.invoke(partitioner, traceGroup));

    assertEquals(10, distances.length, 0);
  }

}
