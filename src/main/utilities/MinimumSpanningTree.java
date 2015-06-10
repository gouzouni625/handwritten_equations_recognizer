package main.utilities;

import java.lang.IndexOutOfBoundsException;

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
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = (i == j);
      }
    }

    int[] sortedIndices = Utilities.sortArray(edgeWeights);
    boolean[] vertexConnected = new boolean[numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      vertexConnected[i] = false;
    }

    int edgesCreated = 0;

    int[] verticesConnected = MinimumSpanningTree.addConnection(connections, sortedIndices[0]);
    vertexConnected[verticesConnected[0]] = true;
    vertexConnected[verticesConnected[1]] = true;
    edgesCreated++;

    for(int i = 1;i < sortedIndices.length;i++){
      if(!MinimumSpanningTree.createsCircle(connections, sortedIndices[i])){
        verticesConnected = MinimumSpanningTree.addConnection(connections, sortedIndices[i]);
        vertexConnected[verticesConnected[0]] = true;
        vertexConnected[verticesConnected[1]] = true;
        edgesCreated++;

        if(Utilities.areAllTrue(vertexConnected) && edgesCreated == numberOfVertices - 1){
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

  private static boolean createsCircle(boolean[][] connections, int index){
    int[] matrixIndices = Utilities.vectorIndexToUpperTriangularIndeces(connections.length, index);
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

  public int[][] getUniquePaths(int maxPathLength){
    return (Utilities.findUniquePaths(connections_, maxPathLength));
  }

  public int[] getContext(int[] vertices){
    return (Utilities.getContext(vertices, connections_));
  }

  private boolean[][] connections_;

}
