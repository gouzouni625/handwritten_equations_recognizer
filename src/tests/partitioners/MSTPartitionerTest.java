package tests.partitioners;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import main.partitioners.MSTPartitioner;
import main.partitioners.NNMSTPartitioner;
import main.utilities.traces.Trace;
import main.utilities.traces.TraceGroup;

public class MSTPartitionerTest{

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
