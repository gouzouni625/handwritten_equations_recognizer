package main.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import org.opencv.core.Mat;

/**
 * Class that contains some methods used in many different places of the
 * project.
 *
 * @author Georgios Ouzounis
 *
 * TODO
 * Many of these methods can be templates(very low priority).
 *
 */
public class Utilities{

  public static int[] sortArray(double[] array){
    int length = array.length;
    double[] arrayClone = array.clone();

    int[] indices = new int[length];
    for(int i = 0;i < length;i++){
      indices[i] = i;
    }

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

  public static boolean arrayContains(int[] array, int value){
    for(int i = 0;i < array.length;i++){
      if(array[i] == value){
        return true;
      }
    }

    return false;
  }

  public static int[] getContext(int[] vertices, boolean[][] connections){
    // Use a hash set to avoid including the same vertex twice.
    // For example, if an existing path contains 5,3 and they both
    // connect to 4, then adding 4 twice must be avoided.
    HashSet<Integer> uniqueContext = new HashSet<Integer>();
    for(int i = 0;i < vertices.length;i++){
      for(int j = 0;j < connections.length;j++){
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

  public static int[] getContext(ArrayList<Integer> vertices, boolean[][] connections){
    int length = vertices.size();
    int[] verticesInt = new int[length];

    for(int i = 0;i < length;i++){
      verticesInt[i] = vertices.get(i).intValue();
    }

    return (Utilities.getContext(verticesInt, connections));
  }

  @SuppressWarnings("unchecked")
  public static int[][] findUniquePaths(boolean[][] connections, int maxPathLength){
    Hashtable<Integer, ArrayList<Integer> > hashTable = new Hashtable<Integer, ArrayList<Integer> >();
    int hashTableOldSize = hashTable.size();

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
            newPaths.add(currentPathClone);
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

  public static int pathHashKey(ArrayList<Integer> path){
    int numberOfVertices = path.size();

    int[] array = new int[numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      array[i] = path.get(i).intValue();
    }

    return pathHashKey(array);
  }

  public static int pathHashKey(int[] path){
    int key = 0;

    for(int i = 0;i < path.length;i++){
      key ^= (int)(Math.pow(2, path[i]));
    }

    return key;
  }

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

  public static double maxValue(double[] array){
    return (array[Utilities.indexOfMax(array)]);
  }

  public static byte[] imageToByteArray(Mat image){
    int numberOfRows = image.rows();
    int numberOfColumns = image.cols();

    byte[] array = new byte[numberOfRows * numberOfColumns];
    if(array.length == 0){
      return array;
    }

    for(int row = 0;row < numberOfRows;row++){
      for(int column = 0;column < numberOfColumns;column++){
        array[row * numberOfColumns + column] = (byte)(image.get(row, column)[0]);
      }
    }

    return array;
  }

  public static double[] relativeValues(double[] array){
    int length = array.length;

    double sum = 0;
    for(int i = 0;i < length;i++){
      sum += array[i];
    }

    double[] relativeValuesArray = new double[length];
    for(int i = 0;i < length;i++){
      relativeValuesArray[i] = array[i] / sum * 100;
    }

    return relativeValuesArray;
  }

  public static int[] vectorIndexToUpperTriangularIndeces(int numberOfRows, int index){
    int rowIndex = 0;
    int columnIndex = 0;

    while(index >= numberOfRows - 1 - rowIndex){
      index -= numberOfRows - 1 - rowIndex;
      rowIndex++;
    }
    columnIndex = rowIndex + 1 + index;

    return (new int[] {rowIndex, columnIndex});
  }

  public static int upperTriangularIndecesToVectorIndex(int numberOfRows, int row, int column){
    int index = 0;
    for(int i = 0;i < row;i++){
      index += numberOfRows - 1 - i;
    }
    index += column;

    return index;
  }

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

  public static final byte UNKNOWN_LABEL = -0x01;

  public static final int LABEL_ZERO = 0;
  public static final int LABEL_ONE = 1;
  public static final int LABEL_TWO = 2;
  public static final int LABEL_THREE = 3;
  public static final int LABEL_FOUR = 4;
  public static final int LABEL_FIVE = 5;
  public static final int LABEL_SIX = 6;
  public static final int LABEL_SEVEN = 7;
  public static final int LABEL_EIGHT = 8;
  public static final int LABEL_NINE = 9;
  public static final int LABEL_PLUS = 10;
  public static final int LABEL_EQUALS = 11;
  public static final int LABEL_VARIABLE_X = 12;
  public static final int LABEL_VARIABLE_Y = 13;
  public static final int LABEL_MINUS = 14;

  public static final int MAX_TRACES_IN_SYMBOL = 3;

  public static final int DATA_MAGIC_NUMBER = 0x00000803;
  public static final int LABELS_MAGIC_NUMBER = 0x00000801;

  public static final double MINIMUM_RATE = 0;
  public static final double MAXIMUM_RATE = 100;

}
