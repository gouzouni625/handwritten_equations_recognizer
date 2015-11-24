package org.hwer.utilities;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

//import org.opencv.core.Mat;

/** @class Utilities
*
*  @brief Contains general methods for Arrays, ArrayList and data.
*
*  @todo Many of these methods could be templates. It will be done with very low priority.
*/
public class Utilities{
  /**
   *  @brief Sorts an array and returns the sorted indices.
   *
   *  Sorts a copy of the given array and returns an array with the sorted indices. That is, if the i-th value of the
   *  returned array is j, then, the object that was in the i-th position before sorting, should be in the j-th
   *  position for the array to be sorted. The sorting is ascending.
   *
   *  @param array The array to be sorted.
   *
   *  @return Returns the sorted indices for the given array.
   */
  public static int[] sortArray(double[] array){
    int length = array.length;
    double[] arrayClone = array.clone();

    int[] indices = new int[length];
    for(int i = 0;i < length;i++){
      indices[i] = i;
    }

    // TODO Maybe use something faster that Bubble Sort.
    for(int i = 0;i < length;i++){
      for(int j = 1;j < length - i;j++){
        if(arrayClone[j - 1] > arrayClone[j]){
          double temp = arrayClone[j - 1];
          arrayClone[j - 1] = arrayClone[j];
          arrayClone[j] = temp;

          int tempIndex = indices[j - 1];
          indices[j - 1] = indices[j];
          indices[j] = tempIndex;
        }
      }
    }

    return indices;
  }

  /**
   *  @brief Checks if an array contains a specific value.
   *
   *  @param array The array.
   *  @param value The value to be search within the array.
   *
   *  @return Returns true if the array contains the specified value.
   */
  public static boolean arrayContains(int[] array, int value){
    for(int i = 0;i < array.length;i++){
      if(array[i] == value){
        return true;
      }
    }

    return false;
  }

  /**
   *  @brief Given an array of vertices and the connections between them, find the context of these vertices.
   *
   *  A graph is composed by vertices and edges. Edges connect vertices. Given the connections of a graph and an array
   *  of vertices, this function will return the vertices that are connected to the given vertices on this graph. Each
   *  one of the returned vertices will be connected to, at least, one of the given vertices. A usage example would be:
   *  @code
   *  boolean[][] connections = new boolean[][] {{true , false, false, true },
   *                                             {false, true , true , false},
   *                                             {false, true , true , false},
   *                                             {true , false, false, true }};
   *  int[] vertices = new int[] {0, 1};
   *
   *  int[] context = Utilities.getContext(vertices, connections);
   *  @endcode
   *  In the above example there are four(4) indices. Index 0 is connected with index 3 only and index 1 is connected
   *  with index 2 only. The context of vertices 0, 1 is 2, 3.
   *
   *  @param vertices The vertices the context of which should be found.
   *  @param connections The connections between the vertices.
   *
   *  @return Returns the context indices of the given indices.
   */
  public static int[] getContext(int[] vertices, boolean[][] connections){
    // Use a hash set to avoid including the same vertex twice.
    // For example, if an existing path contains 5,3 and they both
    // connect to 4, then adding 4 twice must be avoided.
    HashSet<Integer> uniqueContext = new HashSet<Integer>();
    for(int i = 0;i < vertices.length;i++){
      for(int j = 0;j < connections.length;j++){
        // Don't add the given vertices to context.
        if(Utilities.arrayContains(vertices, j)){
          continue;
        }
        if(connections[vertices[i]][j]){
          uniqueContext.add(j);
        }
      }
    }

    // Convert the hash set to an array of integers.
    int[] context = new int[uniqueContext.size()];
    int index = 0;
    Iterator<Integer> iterator = uniqueContext.iterator();
    while(iterator.hasNext()){
      context[index] = iterator.next().intValue();
      index++;
    }

    return context;
  }

  /**
   *  @brief Given an array list of vertices and the connections between them, find the context of these vertices.
   *
   *  @param vertices The vertices the context of which should be found.
   *  @param connections The connections between the vertices.
   *
   *  @return Returns the context indices of the given indices.
   *
   *  @sa getContext
   */
  public static int[] getContext(ArrayList<Integer> vertices, boolean[][] connections){
    int length = vertices.size();
    int[] verticesInt = new int[length];

    for(int i = 0;i < length;i++){
      verticesInt[i] = vertices.get(i).intValue();
    }

    return (Utilities.getContext(verticesInt, connections));
  }

  /**
   *  @brief Finds unique paths of given length, on a graph. These paths should comply with some checks.
   *
   *  @param connections The connections of the indices of the graph.
   *  @param maxPathLength The maximum length of the paths.
   *  @param checks The checks that every path must comply with in order to be chosen.
   *
   *  @return Returns the paths found upon the graph.
   */
  @SuppressWarnings("unchecked")
  public static int[][] findUniquePaths(boolean[][] connections, int maxPathLength, PathExtentionCheck... checks){
    // The hash table that will hold the paths. Use a hash table so that the same path is not included twice.
    Hashtable<Integer, ArrayList<Integer> > hashTable = new Hashtable<Integer, ArrayList<Integer> >();
    int hashTableOldSize = hashTable.size();

    // Add every single vertex to the hash table.
    for(int vertex = 0;vertex < connections.length;vertex++){
      ArrayList<Integer> path = new ArrayList<Integer>();
      path.add(vertex);

      hashTable.put(pathHashKey(path), path);
    }

    int currentPathLength = 1;
    if(maxPathLength > currentPathLength){
      do{
        Iterator<ArrayList<Integer> > existingPathsIterator = hashTable.values().iterator();

        // Find all possible new paths(even duplicates).
        ArrayList<ArrayList<Integer> > newPaths = new ArrayList<ArrayList<Integer> >();
        while(existingPathsIterator.hasNext()){
          ArrayList<Integer> currentPath = existingPathsIterator.next();

          int[] context = Utilities.getContext(currentPath, connections);

          for(int neighbour = 0;neighbour < context.length;neighbour++){
            ArrayList<Integer> currentPathClone = (ArrayList<Integer>)(currentPath.clone());
            currentPathClone.add(context[neighbour]);

            boolean checksPassed = true;
            for(int check = 0;check < checks.length;check++){
              if(!checks[check].check(currentPathClone)){
                checksPassed = false;
                break;
              }
            }

            if(checksPassed){
              newPaths.add(currentPathClone);
            }
          }
        }

        hashTableOldSize = hashTable.size();

        // Add new paths to the hash table to eliminate duplicates.
        for(int newPath = 0;newPath < newPaths.size();newPath++){
          hashTable.put(Utilities.pathHashKey(newPaths.get(newPath)), newPaths.get(newPath));
        }

        currentPathLength++;
      }while(hashTableOldSize != hashTable.size() && currentPathLength < maxPathLength);
    }

    // Convert the hash table to an array of arrays of integers.
    int[][] uniquePaths = new int[hashTable.size()][];
    int uniquePathsIndex = 0;

    Iterator<ArrayList<Integer> > iterator = hashTable.values().iterator();
    while(iterator.hasNext()){
      ArrayList<Integer> currentPath = (ArrayList<Integer>)(iterator.next());

      uniquePaths[uniquePathsIndex] = new int[currentPath.size()];
      for(int vertex = 0;vertex < currentPath.size();vertex++){
        uniquePaths[uniquePathsIndex][vertex] = currentPath.get(vertex);
      }

      uniquePathsIndex++;
    }

    return uniquePaths;
  }

  /**
   *  @brief Computes and returns a unique hash key for a path.
   *
   *  @param path The path.
   *
   *  @return Returns the unique hash key of the path.
   */
  public static int pathHashKey(int[] path){
    int key = 0;

    for(int i = 0;i < path.length;i++){
      key ^= (int)(Math.pow(2, path[i]));
    }

    return key;
  }

  /**
   *  @brief Computes and returns a unique hash key for a path.
   *
   *  @param path The path.
   *
   *  @return Returns the unique hash key of the path.
   *
   *  @sa pathHashKey
   */
  public static int pathHashKey(ArrayList<Integer> path){
    int numberOfVertices = path.size();

    int[] array = new int[numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      array[i] = path.get(i).intValue();
    }

    return pathHashKey(array);
  }

  /**
   *  @brief Finds the position of the maximum value inside an array.
   *
   *  @param array The array.
   *
   *  @return Returns the position of the maximum value inside the given array.
   */
  public static int indexOfMax(double[] array){
    if(array.length == 0){
      return -1;
    }

    double maxValue = array[0];
    int maxValueIndex = 0;
    for(int i = 1;i < array.length;i++){
      if(array[i] > maxValue){
        maxValue = array[i];
        maxValueIndex = i;
      }
    }

    return maxValueIndex;
  }

  /**
   *  @brief Finds the maximum value of an array.
   *
   *  @param array The array.
   *
   *  @return Returns the maximum value of the given array.
   */
  public static double maxValue(double[] array){
    return (array[Utilities.indexOfMax(array)]);
  }

  /**
   *  @brief Converts an image to an array o bytes.
   *
   *  The image is an OpenCV Mat object and contains values from 0 to 255.
   *
   *  @param image The image to be converted to a byte array.
   *
   *  @return Returns the byte array conversion of the image.
   */
  public static byte[] imageToByteVector(BufferedImage image) {
    byte[] reversedPixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();

    int numberOfPixels = reversedPixels.length;
    int imageWidth = image.getWidth();
    int imageHeight = image.getHeight();

    byte[] pixels = new byte[numberOfPixels];

    int x = 0;
    int y = imageHeight - 1;
    for(int i = 0;i < numberOfPixels;i++){
      pixels[y * imageWidth + x] = (byte)(reversedPixels[i] & 0xFF);

      x++;
      if(x == imageWidth){
        x = 0;
        y--;
      }
    }

    return pixels;
  }

  /**
   *  @brief Normalizes the values of an array.
   *
   *  The values of the given array are mapped to [0, 100]. Each value is divided by the sum of the initial values and
   *  multiplied by 100. The initial values should be positive. The given array is not changed. The returned array is
   *  a new array not bound to the given one.
   *
   *  @param array The array to be normalized.
   *
   *  @return Returns an array with the normalized values of the given array.
   */
  public static double[] normalizeArray(double[] array){
    int length = array.length;

    double sum = 0;
    for(int i = 0;i < length;i++){
      sum += array[i];
    }

    double[] normalizedArray = new double[length];
    for(int i = 0;i < length;i++){
      normalizedArray[i] = array[i] / sum * 100;
    }

    return normalizedArray;
  }

  /**
   *  @brief Transforms an index to its corresponding upper triangular indices.
   *
   *  Let A = {{11, 12, 13, 14},
   *           {21, 22, 23, 24},
   *           {31, 32, 33, 34},
   *           {41, 42, 43, 44}}
   *
   *  be an array and b = {12, 13, 14, 23, 24, 34} be the row-major representation of the upper triangular part of A,
   *  excluding the main diagonal. This method will translate indices on vector b, to indices on matrix A.
   *
   *  @param numberOfRows The numberOfRows of the matrix.
   *  @param index The index on the row-major vector.
   *
   *  @return Returns the corresponding row and column indices on the matrix.
   *
   *  @sa upperTriangularIndicesToVectorIndex
   */
  public static int[] vectorIndexToUpperTriangularIndices(int numberOfRows, int index){
    int rowIndex = 0;
    int columnIndex = 0;

    while(index >= numberOfRows - 1 - rowIndex){
      index -= numberOfRows - 1 - rowIndex;
      rowIndex++;
    }
    columnIndex = rowIndex + 1 + index;

    return (new int[] {rowIndex, columnIndex});
  }

  /**
   *  @brief Transforms upper triangular indices to their corresponding row-major index.
   *
   *  Let A = {{11, 12, 13, 14},
   *           {21, 22, 23, 24},
   *           {31, 32, 33, 34},
   *           {41, 42, 43, 44}}
   *
   *  be an array and b = {12, 13, 14, 23, 24, 34} be the row-major representation of the upper triangular part of A,
   *  excluding the main diagonal. This method will translate indices on matrix A, to indices on vector b.
   *
   *  @param numberOfRows The numberOfRows of the matrix.
   *  @param row The row index on the upper triangular matrix.
   *  @param column The column index on the upper triangular matrix.
   *
   *  @return Returns the corresponding index on the row-major vector.
   *
   *  @sa vectorIndexToUpperTriangularIndices
   */
  public static int upperTriangularIndicesToVectorIndex(int numberOfRows, int row, int column){
    int index = 0;
    for(int i = 0;i < row;i++){
      index += numberOfRows - 1 - i;
    }
    index += column;

    return index;
  }

  /**
   *  @brief Checks of all the values of an array are true.
   *
   *  @param array The array.
   *
   *  @return Returns true if all the values of the given array are true.
   */
  public static boolean areAllTrue(boolean[][] array){
    for(int i = 0;i < array.length;i++){
      for(int j = 0;j < array[i].length;j++){
        if(!array[i][j]){
          return false;
        }
      }
    }

    return true;
  }

  /**
   *  @brief Concatenates two arrays.
   *
   *  The second array is appended to the end of the first. The given arrays are not changed. The returned array is a new
   *  array.
   *
   *  @param array1 The first array.
   *  @param array2 The array to be appended.
   *
   *  @return Returns the concatenated array.
   */
  public static int[][] concatenateArrays(int[][] array1, int[][] array2){
    int[][] array = new int[array1.length + array2.length][];

    for(int i = 0;i < array1.length;i++){
      array[i] = array1[i].clone();
    }

    for(int i = 0;i < array2.length;i++){
      array[array1.length + i] = array2[i].clone();
    }

    return array;
  }

  /**
   *  @brief Removes specified rows from an array.
   *
   *  The given array is not changed. The array returned is a copy of the given array.
   *
   *  @param array The array.
   *  @param rowsIndices The rows to be removed.
   *
   *  @return Returns the array with the specified rows removed.
   */
  public static int[][] removeRows(int[][] array, ArrayList<Integer> rowsIndices){
    int newLength = array.length - rowsIndices.size();
    int[][] newArray = new int[newLength][];

    Collections.sort(rowsIndices);

    int currentRowInNewArray = 0;
    int currentIndexInArray = 0;
    for(int i = 0;i < array.length;i++){
      if(currentIndexInArray < rowsIndices.size() && i == rowsIndices.get(currentIndexInArray)){
        currentIndexInArray++;
        continue;
      }

      newArray[currentRowInNewArray] = array[i].clone();
      currentRowInNewArray++;
    }

    return newArray;
  }

  /**
   *  @brief Checks if a row exists inside an array.
   *
   *  The objects of the row might not have the same sequence.
   *
   *  @param array The array.
   *  @param row The row.
   *  @param maintainSequence Flag to determine whether the order of the objects in the row should matter.
   *
   *  @return Returns true of the row exists inside the array.
   */
  public static boolean rowInArray(int[][] array, int[] row, boolean maintainSequence){
    int numberOfRows = array.length;

    for(int i = 0;i < numberOfRows;i++){
      if(array[i].length == row.length){

        int[] arrayRow = array[i].clone();
        int[] checkRow = row.clone();

        if(!maintainSequence){
          Arrays.sort(arrayRow);
          Arrays.sort(checkRow);
        }

        int counter = 0;
        for(int j = 0;j < checkRow.length;j++){
          if(arrayRow[j] == checkRow[j]){
            counter++;
          }
        }

        if(counter == row.length){
          return true;
        }
      }
    }

    return false;
  }

}
