package main.utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

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

  @SuppressWarnings("unchecked")
  public static ArrayList<ArrayList<Integer> > findPaths(ArrayList<ArrayList<Integer> > existingPaths, boolean[][] connections){
    ArrayList<ArrayList<Integer> > paths = new ArrayList<ArrayList<Integer> >();
    ArrayList<Integer> newPath;

    // For every existing path.
    for(int path = 0;path < existingPaths.size();path++){
      int[] context = Utilities.getContext(existingPaths.get(path), connections);

      for(int neighbour = 0;neighbour < context.length;neighbour++){
        newPath = (ArrayList<Integer>)(existingPaths.get(path).clone());
        newPath.add(context[neighbour]);
        paths.add(newPath);
      }
    }

    if(paths.get(0).size() == connections.length){
      return ((ArrayList<ArrayList<Integer> >)(Utilities.concatenateLists(existingPaths, paths)));
    }
    else{
      return ((ArrayList<ArrayList<Integer> >)(Utilities.concatenateLists(existingPaths, findPaths(paths, connections))));
    }
  }

  public static int[] getContext(ArrayList<Integer> vertices, boolean[][] connections){
    int length = vertices.size();
    int[] verticesInt = new int[length];

    for(int i = 0;i < length;i++){
      verticesInt[i] = vertices.get(i).intValue();
    }

    return (Utilities.getContext(verticesInt, connections));
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

  public static boolean arrayContains(int[] array, int value){
    for(int i = 0;i < array.length;i++){
      if(array[i] == value){
        return true;
      }
    }

    return false;
  }

  public static ArrayList<ArrayList<Integer> > concatenateLists(ArrayList<ArrayList<Integer> > list1, ArrayList<ArrayList<Integer> > list2){
    ArrayList<ArrayList<Integer> > list = new ArrayList<ArrayList<Integer> >();

    for(int i = 0;i < list1.size();i++){
      list.add(list1.get(i));
    }

    for(int i = 0;i < list2.size();i++){
      list.add(list2.get(i));
    }

    return list;
  }
}
