package main.utilities;

import java.lang.IndexOutOfBoundsException;
import java.util.HashSet;
import java.util.Iterator;

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
      return (new MinimumSpanningTree(new boolean[numberOfVertices][numberOfVertices]));
    }

    boolean[][] connections = new boolean[numberOfVertices][numberOfVertices];
    boolean[][] canReach = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = (i == j);
        canReach[i][j] = (i == j);
      }
    }

    int[] sortedIndices = Utilities.sortArray(edgeWeights);

    // Use the first connection.
    int[] vertices = MinimumSpanningTree.addConnection(connections, sortedIndices[0]);
    canReach = updateCanReach(canReach, vertices[0], vertices[1]);

    for(int i = 1;i < sortedIndices.length;i++){
      vertices = Utilities.vectorIndexToUpperTriangularIndeces(connections.length, sortedIndices[i]);
      if(!canReach[vertices[0]][vertices[1]]){
        MinimumSpanningTree.addConnection(connections, sortedIndices[i]);
        canReach = updateCanReach(canReach, vertices[0], vertices[1]);

        if(Utilities.areAllTrue(canReach)){
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

  public static void connect(boolean[][] connections, int vertex1, int vertex2) throws IndexOutOfBoundsException{
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

  private static int[] addConnection(boolean[][] connections, int index){
    int[] matrixIndices = Utilities.vectorIndexToUpperTriangularIndeces(connections.length, index);

    MinimumSpanningTree.connect(connections, matrixIndices[0], matrixIndices[1]);

    return matrixIndices;
  }

  public int[][] getUniquePaths(int maxPathLength){
    return (Utilities.findUniquePaths(connections_, maxPathLength));
  }

  public int[] getContext(int[] vertices){
    return (Utilities.getContext(vertices, connections_));
  }

  public static boolean[][] updateCanReach(boolean[][] canReach, int row, int column){
    int numberOfVertices = canReach.length;

    canReach[row][column] = true;
    canReach[column][row] = true;

    HashSet<Integer> vertexGroup = new HashSet<Integer>();
    vertexGroup.add(row);
    vertexGroup.add(column);
    int oldSize = vertexGroup.size();
    int newSize = vertexGroup.size();

    do{
      oldSize = newSize;

      for(int i = 0;i < numberOfVertices;i++){
        if(vertexGroup.contains(i)){
          continue;
        }

        Iterator<Integer> iterator = vertexGroup.iterator();
        while(iterator.hasNext()){
          if(canReach[i][iterator.next().intValue()]){

            Iterator<Integer> innerIterator = vertexGroup.iterator();
            while(innerIterator.hasNext()){
              int target = innerIterator.next();
              canReach[i][target] = true;
              canReach[target][i] = true;
            }

            vertexGroup.add(i);

            break;
          }
        }
      }

      newSize = vertexGroup.size();
    }while(oldSize != newSize);

    return canReach;
  }

  private boolean[][] connections_;

}
