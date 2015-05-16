package utilities;

import java.util.Arrays;

public class MinimumSpanningTree{
  public MinimumSpanningTree(int numberOfVertices){
    numberOfVertices_ = numberOfVertices;
    
    connections_ = new boolean[numberOfVertices_][numberOfVertices_];
    for(int i = 0;i < numberOfVertices_;i++){
      for(int j = 0;j < numberOfVertices_;j++){
        connections_[i][j] = (i == j);
      }
    }
  }
  
  public MinimumSpanningTree(boolean[][] connections){
    connections_ = connections;

    numberOfVertices_ = connections.length;
  }
  
  public static MinimumSpanningTree kruskal(double[] edgeWeights,
                                                          int numberOfVertices){
    
    boolean[][] connections = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = (i == j);
      }
    }
    
    Arrays.sort(edgeWeights);

    for(int i = 0;i < edgeWeights.length;i++){
      
    }
  }
  
  public int getNumberOfVertices(){
    return numberOfVertices_;
  }
  
  // TODO
  // Throw an exception if either of the two vertices is out of bounds.
  public boolean areConnected(int vertex1, int vertex2){
    return connections_[vertex1][vertex2];
  }
  
  // TODO
  // Throw an exception if either of the two vertices is out of bounds.
  public void connect(int vertex1, int vertex2){
    connections_[vertex1][vertex2] = true;

    if(vertex1 != vertex2){
      connections_[vertex2][vertex1] = true;
    }
  }

  // TODO
  // Throw an exception if either of the two vertices is out of bounds.
  public void disconnect(int vertex1, int vertex2){
    connections_[vertex1][vertex2] = false;
    
    if(vertex1 != vertex2){
      connections_[vertex2][vertex1] = false;
    }
  }
  
  // TODO
  // Change so that the class saves only the need matrix, not symetric or
  // main diagonal.
  private boolean[][] connections_;
  
  private int numberOfVertices_;
}