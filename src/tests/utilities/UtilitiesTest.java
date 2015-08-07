package tests.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import main.utilities.Utilities;

/** @class UtilitiesTest
 *
 *  @brief Class that contains tests for Utilities class.
 */
public class UtilitiesTest{
  /**
   *  @brief Tests sortArray method of Utilities class.
   */
  @Test
  public void testSortArray(){
    double[] array = new double[] {9, 8, 7, 6, 5, 4, 3, 2, 1, 0};

    int[] sortedIndices = Utilities.sortArray(array);

    for(int i = 0;i < sortedIndices.length;i++){
      assertEquals(9 - i, sortedIndices[i], 0);
    }
  }

  /**
   *  @brief Tests arrayContains method of Utilities class.
   */
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

  /**
   *  @brief Tests getContext methods of Utilities class.
   */
  @Test
  public void testGetContext(){
    int numberOfVertices = 10;
    int[] vertices = new int[] {0, 2, 6, 7};

    // Check the context when there are no connections between the vertices.
    boolean[][] connections = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = false;
      }
    }

    int[] context = Utilities.getContext(vertices, connections);
    assertEquals(0, context.length, 0);

    // Check the context when every vertex is connected with all other vertices.
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = true;
      }
    }

    context = Utilities.getContext(vertices, connections);
    assertEquals(numberOfVertices - vertices.length, context.length, 0);

    connections = new boolean[][] {{true , false, false, true },
                                   {false, true , true , false},
                                   {false, true , true , false},
                                   {true , false, false, true }};
    vertices = new int[] {0, 1};

    context = Utilities.getContext(vertices, connections);

    assertEquals(2, context.length, 0);

    assertEquals(2, context[0], 0);
    assertEquals(3, context[1], 0);

    ArrayList<Integer> verticesList = new ArrayList<Integer>();
    verticesList.add(0);
    verticesList.add(1);

    context = Utilities.getContext(verticesList, connections);

    assertEquals(2, context.length, 0);

    assertEquals(2, context[0], 0);
    assertEquals(3, context[1], 0);

  }

  /**
   *  @brief Tests findUniquePaths method of Utilities class.
   */
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

  /**
   *  @brief Tests pathHashKey method of Utilities class.
   */
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

  /**
   *  @brief Tests indexOfMax method of Utilities class.
   */
  @Test
  public void testIndexOfMax(){
    double[] array = new double[] {9, 8, 7, 6, 5, 4, 3, 2, 1};

    assertEquals(0, Utilities.indexOfMax(array));
  }

  /**
   *  @brief Tests maxValue method of Utilities class.
   */
  @Test
  public void testMaxValue(){
    double[] array = new double[] {9, 8, 7, 6, 5, 4, 3, 2, 1};

    assertEquals(9, Utilities.maxValue(array), 0);
  }

  /**
   *  @brief Tests imageToByteArray method of Utilities class.
   */
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

  /**
   *  @brief Tests normalizeArray method of Utilities class.
   */
  @Test
  public void testNormalizeArray(){
    double[] array = new double[] {0.5, 0.3, 0.2};

    array = Utilities.normalizeArray(array);

    assertEquals(50, array[0], 0);
    assertEquals(30, array[1], 0);
    assertEquals(20, array[2], 0);
  }

  /**
   *  @brief Tests vectorIndexToUpperTriangularIndeces method of Utilities class.
   */
  @Test
  public void testVectorIndexToUpperTriangularIndeces(){
    int[] indices;

    indices = Utilities.vectorIndexToUpperTriangularIndices(5,  3);
    assertEquals(0, indices[0], 0);
    assertEquals(4, indices[1], 0);

    indices = Utilities.vectorIndexToUpperTriangularIndices(4, 4);
    assertEquals(1, indices[0], 0);
    assertEquals(3, indices[1], 0);
  }

  /**
   *  @brief Tests upperTriangularIndicesToVectorIndex method of Utilities class.
   */
  @Test
  public void testUpperTriangularIndecesToVectorIndex(){
    int index;

    index = Utilities.upperTriangularIndicesToVectorIndex(5, 0, 3);
    assertEquals(3, index, 0);

    index = Utilities.upperTriangularIndicesToVectorIndex(5, 1, 2);
    assertEquals(6, index, 0);

    index = Utilities.upperTriangularIndicesToVectorIndex(5, 2, 2);
    assertEquals(9, index, 0);
  }

  /**
   *  @brief Tests areAllTrue method of Utilities class.
   */
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

  /**
   *  @brief Tests concatenateArrays method of Utilities class.
   */
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

  /**
   *  @brief Tests removeRows method of Utilities class.
   */
  @Test
  public void testRemoveRows(){
    int size = 10;

    int[][] array = new int[size][size];
    for(int i = 0;i < size;i++){
      for(int j = 0;j < size;j++){
        array[i][j] = i * size + j;
      }
    }

    ArrayList<Integer> rowsIndices = new ArrayList<Integer>();
    rowsIndices.add(0);
    rowsIndices.add(2);
    rowsIndices.add(7);
    int[][] newArray = Utilities.removeRows(array, rowsIndices);

    int newSize = size - rowsIndices.size();

    assertEquals(newSize, newArray.length, 0);

    Collections.sort(rowsIndices);

    int currentRowInNewArray = 0;
    int currentIndexInArray = 0;
    for(int i = 0;i < size;i++){
      if(currentIndexInArray < rowsIndices.size() && i == rowsIndices.get(currentIndexInArray)){
        currentIndexInArray++;
        continue;
      }

      for(int j = 0;j < newArray[currentRowInNewArray].length;j++){
        assertEquals(array[i][j], newArray[currentRowInNewArray][j], 0);
      }
      currentRowInNewArray++;
    }
  }

  /**
   *  @brief Tests rowInArray method of Utilities class.
   */
  @Test
  public void testRowInArray(){
    int numberOfRows = 100;
    int numberOfColumns = 100;
    int[][] array = new int[numberOfRows][numberOfColumns];

    for(int i = 0;i < numberOfRows;i++){
      for(int j = 0;j < numberOfColumns;j++){
        array[i][j] = i * numberOfColumns + j;
      }
    }

    for(int i = 0;i < numberOfRows;i++){
      assertTrue(Utilities.rowInArray(array, array[i], true));
    }

    int[] row = new int[numberOfColumns - 1];
    for(int i = 0;i < numberOfColumns - 1;i++){
      row[i] = (int)Math.random();
    }

    assertFalse(Utilities.rowInArray(array, row, false));
  }

}
