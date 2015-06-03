package main.utilities;

import java.lang.IndexOutOfBoundsException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class MinimumSpanningTree{
  public MinimumSpanningTree(int numberOfVertices){
    connections_ = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections_[i][j] = (i == j);
      }
    }
  }

  public MinimumSpanningTree(boolean[][] connections){
    int numberOfVertices = connections.length;
    connections_ = new boolean[numberOfVertices][numberOfVertices];

    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections_[i][j] = connections[i][j];
      }
    }

  }

  public static MinimumSpanningTree kruskal(double[] edgeWeights, int numberOfVertices){
    if(numberOfVertices == 0){
      return (new MinimumSpanningTree(numberOfVertices));
    }

    boolean[][] connections = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = (i == j);
      }
    }

    int[] indeces = Utilities.sortArray(edgeWeights);
    int verticesLeft = numberOfVertices;

    // Add the first connection to the tree.
    // Notice that the first connection adds 2 vertices to the tree.
    MinimumSpanningTree.addConnection(connections, indeces[0]);
    verticesLeft -= 2;

    for(int i = 1;i < indeces.length;i++){
      if(!MinimumSpanningTree.createsCircle(connections, indeces[i])){
        MinimumSpanningTree.addConnection(connections, indeces[i]);
        verticesLeft--;
        if(verticesLeft == 0){
          break;
        }
      }
    }

    return (new MinimumSpanningTree(connections));
  }

  public int numberOfVertices(){
    return connections_.length;
  }

  public boolean areConnected(int vertex1, int vertex2)
                                               throws IndexOutOfBoundsException{
    if(vertex1 < 0 || vertex1 >= connections_.length ||
       vertex2 < 0 || vertex2 >= connections_.length){
      throw new IndexOutOfBoundsException();
    }
    else{
      return connections_[vertex1][vertex2];
    }
  }

  public void connect(int vertex1, int vertex2) throws IndexOutOfBoundsException{
    MinimumSpanningTree.connect(connections_, vertex1, vertex2);
  }

  public static void connect(boolean[][] connections, int vertex1, int vertex2)
                                              throws IndexOutOfBoundsException{
    if(vertex1 < 0 || vertex1 >= connections.length ||
        vertex2 < 0 || vertex2 >= connections.length){
       throw new IndexOutOfBoundsException();
     }
     else{
       connections[vertex1][vertex2] = true;

       if(vertex1 != vertex2){
         connections[vertex2][vertex1] = true;
       }
     }
  }

  public void disconnect(int vertex1, int vertex2) throws IndexOutOfBoundsException{
    MinimumSpanningTree.disconnect(connections_, vertex1, vertex2);
  }

  public static void disconnect(boolean[][] connections, int vertex1, int vertex2) throws IndexOutOfBoundsException{
    if(vertex1 < 0 || vertex1 >= connections.length ||
        vertex2 < 0 || vertex2 >= connections.length){
       throw new IndexOutOfBoundsException();
     }
     else{
       connections[vertex1][vertex2] = false;

       if(vertex1 != vertex2){
         connections[vertex2][vertex1] = false;
       }
     }
  }

  private static void addConnection(boolean[][] connections, int index){
    int[] matrixIndices = MinimumSpanningTree.
                          vectorIndexToUpperTriangularIndeces(
                                                     connections.length, index);

    MinimumSpanningTree.connect(connections, matrixIndices[0],
                                                              matrixIndices[1]);
  }

  private static int[] vectorIndexToUpperTriangularIndeces(int numberOfRows,
                                                                     int index){
    int rowIndex = 0;
    int columnIndex = 0;

    while(index >= numberOfRows - 1 - rowIndex){
      index -= numberOfRows - 1 - rowIndex;
      rowIndex++;
    }
    columnIndex = rowIndex + 1 + index;

    return (new int[] {rowIndex, columnIndex});
  }

  private static boolean createsCircle(boolean[][] connections, int index){
    int[] matrixIndices = MinimumSpanningTree.vectorIndexToUpperTriangularIndeces(connections.length, index);
    int rowIndex = matrixIndices[0];
    int columnIndex = matrixIndices[1];

    int depth = 0;
    if(rowIndex > columnIndex){
      return (MinimumSpanningTree.doIReach(rowIndex, columnIndex, connections, depth));
    }
    else{
      return (MinimumSpanningTree.doIReach(columnIndex, rowIndex, connections, depth));
    }
  }

  private static boolean doIReach(int destination, int beginning, boolean[][] connections, int depth){
    if(beginning == destination){
      return true;
    }

    int length = connections.length;
    depth++;
    if(depth == (length * (length -1)) / 2){
      return false;
    }

    for(int j = 0;j < length;j++){
      if(beginning == j){
        continue;
      }
      if(!connections[beginning][j]){
        continue;
      }

      if(doIReach(destination, j, connections, depth)){
        return true;
      }
    }

    return false;
  }

  public int[][] getUniquePaths(){
    return (Utilities.findUniquePaths(connections_));
  }

  public int[] getContext(int[] vertices){
    return (Utilities.getContext(vertices, connections_));
  }

  private boolean[][] connections_;

}
