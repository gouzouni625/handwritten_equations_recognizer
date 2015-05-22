package tests.utilities;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import main.utilities.Utilities;

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
  public void testConcatenateLists(){
    ArrayList<ArrayList<Integer> > list1 = new ArrayList<ArrayList<Integer> >();
    ArrayList<ArrayList<Integer> > list2 = new ArrayList<ArrayList<Integer> >();

    int size1 = 5;
    int size11 = 10;
    int size2 = 15;
    int size22 = 20;

    for(int i = 0;i < size1;i++){
      ArrayList<Integer> temp = new ArrayList<Integer>();
      for(int j = 0;j < size11;j++){
        temp.add(new Integer((int)(Math.random())));
      }

      list1.add(temp);
    }

    for(int i = 0;i < size2;i++){
      ArrayList<Integer> temp = new ArrayList<Integer>();
      for(int j = 0;j < size22;j++){
        temp.add(new Integer((int)(Math.random())));
      }

      list2.add(temp);
    }

    ArrayList<ArrayList<Integer> > list = Utilities.concatenateLists(list1, list2);

    assertEquals(list1.size() + list2.size(), list.size(), 0);

    for(int i = 0;i < size1;i++){
      for(int j = 0;j < size11;j++){
        assertEquals(list1.get(i).get(j).intValue(), list.get(i).get(j).intValue(), 0);
      }
    }

    for(int i = 0;i < size2;i++){
      for(int j = 0;j < size22;j++){
        assertEquals(list2.get(i).get(j).intValue(), list.get(size1 + i).get(j).intValue(), 0);
      }
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
  public void testFindPaths(){

  }

}
