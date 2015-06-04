package tests.utilities;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import main.utilities.Utilities;

/**
 *
 * @author Georgios Ouzounis
 *
 */
public class UtilitiesTest{

  @Test
  public void testSortArray(){
    double[] array = new double[] {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};

    int[] sortedIndices = Utilities.sortArray(array);

    for(int i = 0;i < sortedIndices.length;i++){
      assertEquals(9 - i, sortedIndices[i], 0);
    }
  }

  @Test
  public void testArrayContains(){
    int[] array = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    for(int i = 0;i < 10;i++){
      assertTrue(Utilities.arrayContains(array, i));
    }

    for(int i = 1;i < 10;i++){
      assertFalse(Utilities.arrayContains(array, -i));
    }
  }

  @Test
  public void testGetContext(){
    int numberOfVertices = (int)(Math.random());
    int[] vertices = new int[numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      vertices[i] = (int)(Math.random());
    }

    boolean[][] connections = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = (i == j);
      }
    }

    int[] context = Utilities.getContext(vertices, connections);
    assertEquals(0, context.length, 0);

    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = !(i == j);
      }
    }

    context = Utilities.getContext(vertices, connections);
    assertEquals(0, context.length, 0);

    numberOfVertices = 5;
    vertices = new int[] {2, 4};

    connections = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = !(i == j);
      }
    }

    context = Utilities.getContext(vertices, connections);
    assertEquals(3, context.length, 0);
    int correctCounter = 0;
    for(int i = 0;i < context.length;i++){
      if(context[i] == 0 || context[i] == 1 || context[i] == 3){
        correctCounter++;
      }
    }
    assertEquals(3, correctCounter, 0);
  }

  @Test
  public void testFindUniquePaths(){
    int numberOfVertices = 10;
    boolean[][] connections = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = (i == j);
      }
    }

    int[][] uniquePaths = Utilities.findUniquePaths(connections);

    assertEquals(numberOfVertices, uniquePaths.length, 0);

    numberOfVertices = 5;
    connections = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = (i >= j);
      }
    }

    uniquePaths = Utilities.findUniquePaths(connections);

    assertEquals(31, uniquePaths.length, 0);
  }

  @Test
  public void pathHashKey(){
    assertEquals(1, Utilities.pathHashKey(new int[] {0}), 0);
    assertEquals(2, Utilities.pathHashKey(new int[] {1}), 0);
    assertEquals(4, Utilities.pathHashKey(new int[] {2}), 0);
    assertEquals(8, Utilities.pathHashKey(new int[] {3}), 0);
    assertEquals(16, Utilities.pathHashKey(new int[] {4}), 0);
    assertEquals(3, Utilities.pathHashKey(new int[] {0, 1}), 0);
    assertEquals(7, Utilities.pathHashKey(new int[] {0, 1, 2}), 0);
    assertEquals(23, Utilities.pathHashKey(new int[] {0, 1, 2, 4}), 0);
    assertEquals(1024, Utilities.pathHashKey(new int[] {10}), 0);
    assertEquals(3073, Utilities.pathHashKey(new int[] {0, 10, 11}), 0);

    ArrayList<Integer> list = new ArrayList<Integer>();
    list.add(0);
    assertEquals(1, Utilities.pathHashKey(list), 0);
    list.clear();
    list.add(1);
    assertEquals(2, Utilities.pathHashKey(list), 0);
    list.clear();
    list.add(2);
    assertEquals(4, Utilities.pathHashKey(list), 0);
    list.clear();
    list.add(3);
    assertEquals(8, Utilities.pathHashKey(list), 0);
    list.clear();
    list.add(4);
    assertEquals(16, Utilities.pathHashKey(list), 0);
    list.clear();
    list.add(0);
    list.add(1);
    assertEquals(3, Utilities.pathHashKey(list), 0);
    list.add(2);
    assertEquals(7, Utilities.pathHashKey(list), 0);
    list.add(4);
    assertEquals(23, Utilities.pathHashKey(list), 0);
    list.clear();
    list.add(10);
    assertEquals(1024, Utilities.pathHashKey(list), 0);
    list.add(0);
    list.add(11);
    assertEquals(3073, Utilities.pathHashKey(list), 0);
  }

  @Test
  public void testIndexOfMax(){
    double[] array = new double[] {9, 8, 7, 6, 5, 4, 3, 2, 1};

    assertEquals(0, Utilities.indexOfMax(array));
  }

  @Test
  public void testMaxValue(){
    double[] array = new double[] {9, 8, 7, 6, 5, 4, 3, 2, 1};

    assertEquals(9, Utilities.maxValue(array), 0);
  }

}
