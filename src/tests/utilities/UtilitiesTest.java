package tests.utilities;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;

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

    int[][] uniquePaths = Utilities.findUniquePaths(connections, connections.length);

    assertEquals(numberOfVertices, uniquePaths.length, 0);

    numberOfVertices = 5;
    connections = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = (i >= j);
      }
    }

    uniquePaths = Utilities.findUniquePaths(connections, connections.length);
    assertEquals(31, uniquePaths.length, 0);

    uniquePaths = Utilities.findUniquePaths(connections, connections.length - 1);
    assertEquals(30, uniquePaths.length, 0);

    uniquePaths = Utilities.findUniquePaths(connections, connections.length - 2);
    assertEquals(25, uniquePaths.length, 0);

    uniquePaths = Utilities.findUniquePaths(connections, connections.length - 3);
    assertEquals(15, uniquePaths.length, 0);

    uniquePaths = Utilities.findUniquePaths(connections, connections.length - 4);
    assertEquals(5, uniquePaths.length, 0);
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

  @Test
  public void testImageToByteArray(){
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    int numberOfRows = 640;
    int numberOfColumns = 320;
    Mat image = Mat.zeros(new Size(numberOfRows, numberOfColumns), CvType.CV_32F);

    byte[] array = Utilities.imageToByteArray(image);

    assertEquals(array.length, numberOfRows * numberOfColumns, 0);

    for(int i = 0;i < numberOfRows * numberOfColumns;i++){
      assertEquals(0, array[i], 0);
    }
  }

  @Test
  public void testRelativeValues(){
    double[] array = new double[] {0.5, 0.3, 0.2};

    array = Utilities.relativeValues(array);

    assertEquals(50, array[0], 0);
    assertEquals(30, array[1], 0);
    assertEquals(20, array[2], 0);
  }

  @Test
  public void testVectorIndexToUpperTriangularIndeces(){
    int[] indices;

    indices = Utilities.vectorIndexToUpperTriangularIndeces(5,  3);
    assertEquals(0, indices[0], 0);
    assertEquals(4, indices[1], 0);

    indices = Utilities.vectorIndexToUpperTriangularIndeces(4, 4);
    assertEquals(1, indices[0], 0);
    assertEquals(3, indices[1], 0);
  }

  @Test
  public void testUpperTriangularIndecesToVectorIndex(){
    int index;

    index = Utilities.upperTriangularIndecesToVectorIndex(5, 0, 3);
    assertEquals(3, index, 0);

    index = Utilities.upperTriangularIndecesToVectorIndex(5, 1, 2);
    assertEquals(6, index, 0);

    index = Utilities.upperTriangularIndecesToVectorIndex(5, 2, 2);
    assertEquals(9, index, 0);
  }

  @Test
  public void testAreAllTrue(){
    int numberOfRows = 10;
    int numberOfColumns = 10;
    boolean[][] connections = new boolean[numberOfRows][numberOfColumns];
    for(int i = 0;i < numberOfRows;i++){
      for(int j = 0;j < numberOfColumns;j++){
        connections[i][j] = true;
      }
    }

    assertTrue(Utilities.areAllTrue(connections));

    connections[1][2] = false;

    assertFalse(Utilities.areAllTrue(connections));

    connections[1][2] = true;

    assertTrue(Utilities.areAllTrue(connections));
  }

  @Test
  public void testConcatenateArrays(){
    int size = 10;

    int[][] array1 = new int[size][size];
    int[][] array2 = new int[size][size];

    for(int i = 0;i < size;i++){
      for(int j = 0;j < size;j++){
        array1[i][j] = (int)(Math.random() * 1000);
        array2[i][j] = (int)(Math.random() * 1000);
      }
    }

    int[][] array = Utilities.concatenateArrays(array1, array2);

    for(int i = 0;i < size;i++){
      for(int j = 0; j < size;j++){
        assertEquals(array[i][j], array1[i][j], 0);
      }
    }

    for(int i = 0;i < size;i++){
      for(int j = 0;j < size;j++){
        assertEquals(array[size + i][j], array2[i][j], 0);
      }
    }
  }

  @Test
  public void testRemoveRows(){
    int size = 10;

    int[][] array = new int[size][size];
    for(int i = 0;i < size;i++){
      for(int j = 0;j < size;j++){
        array[i][j] = i * size + j;
      }
    }

    int[] rowsIndices = new int[] {0, 2, 7};
    int[][] newArray = Utilities.removeRows(array, rowsIndices);

    int newSize = size - rowsIndices.length;

    assertEquals(newSize, newArray.length, 0);

    Arrays.sort(rowsIndices);

    int currentRowInNewArray = 0;
    int currentIndexInArray = 0;
    for(int i = 0;i < size;i++){
      if(currentIndexInArray < rowsIndices.length && i == rowsIndices[currentIndexInArray]){
        currentIndexInArray++;
        continue;
      }

      for(int j = 0;j < newArray[currentRowInNewArray].length;j++){
        assertEquals(array[i][j], newArray[currentRowInNewArray][j], 0);
      }
      currentRowInNewArray++;
    }
  }

}
