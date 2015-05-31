package main.utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

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
  /** \brief Sorts the clone array and returns the indices changed in the same
   *         way. The original array is not changed.
   *
   * Maybe change to something more sophisticated than bubble sort.
   */
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
  public static int[][] findUniquePaths(boolean[][] connections){
    Hashtable<Integer, ArrayList<Integer> > hashTable = new Hashtable<Integer, ArrayList<Integer> >();
    int hashTableOldSize = hashTable.size();

    for(int vertex = 0;vertex < connections.length;vertex++){
      ArrayList<Integer> path = new ArrayList<Integer>();
      path.add(vertex);

      hashTable.put(pathHashKey(path), path);
    }

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

    }while(hashTableOldSize != hashTable.size());

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

}
