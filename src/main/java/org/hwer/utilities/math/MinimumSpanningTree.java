package org.hwer.utilities.math;

import java.lang.IndexOutOfBoundsException;
import java.util.HashSet;
import java.util.Iterator;

import org.hwer.utilities.Utilities;

/** @class MinimumSpanningTree
 *
 *  @brief Implements a MinimumSpanningTree.
 *
 *  Copying from https://en.wikipedia.org/wiki/Minimum_spanning_tree : \n\n
 *  Given a connected, undirected graph, a spanning tree of that graph is a subgraph that is a tree and connects all
 *  the vertices together. A single graph can have many different spanning trees. We can also assign a weight to each
 *  edge, which is a number representing how unfavorable it is, and use this to assign a weight to a spanning tree by
 *  computing the sum of the weights of the edges in that spanning tree. A minimum spanning tree(MST) or minimum weight
 *  spanning tree is then a spanning tree with weight less than or equal to the weight of every other spanning tree.
 */
public class MinimumSpanningTree{
  /**
   *  @brief Constructor.
   *
   *  The connections of the MinimumSpanningTree are not the ones given as input but copies of them. That is, if the
   *  given connections matrix changes, the connections of the MinimumSpanningTree will not change.
   *
   *  @param connections The connections between the vertices of the MinimumSpanningTree.
   */
  public MinimumSpanningTree(boolean[][] connections){
    int numberOfVertices = connections.length;
    connections_ = new boolean[numberOfVertices][numberOfVertices];

    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections_[i][j] = connections[i][j];
      }
    }

  }

  /**
   *  @brief Creates a MinimumSpanningTree using Kruskal's algorithm.
   *
   *  Given the weights of each edge and the number of vertices, this method will create a MinimumSpanningTree using
   *  Kruskal's algorithm. More about Kruskal's algorithm can be found here: \n
   *  https://en.wikipedia.org/wiki/Kruskal%27s_algorithm \n\n
   *
   *  The edge weights should have the following format: \n
   *  Let A = {{0, d12, d13, d14},
   *           {d21, 0, d23, d24},
   *           {d31, d32, 0, d34},
   *           {d41, d42, d43, 0}} \n\n
   *
   *  be the matrix with the distances between the vertices. Obviously there are four vertices and also the matrix is
   *  symmetric. That is, dij = dji with i = 0, 1, 2, 3 and j = 0, 1, 2, 3. The corresponding edgeWeights array should be
   *  edgeWeights = {d12, d13, d14, d23, d24, d34}.
   *
   *  @param edgeWeights The weights of the edges.
   *  @param numberOfVertices The number of vertices on the graph.
   *
   *  @return Returns the MinimumSpanningTree created.
   *
   *  @sa Utilities.vectorIndexToUpperTriangularIndices, Utilities.upperTriangularIndicesToVectorIndex
   */
  public static MinimumSpanningTree kruskal(double[] edgeWeights, int numberOfVertices){
    if(numberOfVertices == 0){
      return (new MinimumSpanningTree(new boolean[numberOfVertices][numberOfVertices]));
    }

    boolean[][] connections = new boolean[numberOfVertices][numberOfVertices];
    boolean[][] scope = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = (i == j);
        scope[i][j] = (i == j);
      }
    }

    int[] sortedIndices = Utilities.sortArray(edgeWeights);

    // Use the first connection.
    int[] vertices = MinimumSpanningTree.connect(connections, sortedIndices[0]);
    scope = updateScope(scope, vertices[0], vertices[1]);

    for(int i = 1;i < sortedIndices.length;i++){
      vertices = Utilities.vectorIndexToUpperTriangularIndices(connections.length, sortedIndices[i]);
      if(!scope[vertices[0]][vertices[1]]){
        MinimumSpanningTree.connect(connections, sortedIndices[i]);
        scope = updateScope(scope, vertices[0], vertices[1]);

        if(Utilities.areAllTrue(scope)){
          break;
        }
      }
    }

    return (new MinimumSpanningTree(connections));
  }

  /**
   *  @brief Returns true if two specified vertices are connected on the tree.
   *
   *  @param vertex1 The first vertex.
   *  @param vertex2 The second vertex.
   *
   *  @return Returns true if the two specified vertices are connected on the tree.
   *
   *  @throws IndexOutOfBoundsException If at least one of the vertices given is less that zero or greater or equal to
   *  the number of vertices.
   */
  public boolean areConnected(int vertex1, int vertex2) throws IndexOutOfBoundsException{
    if(vertex1 < 0 || vertex1 >= connections_.length ||
       vertex2 < 0 || vertex2 >= connections_.length){
      throw new IndexOutOfBoundsException();
    }
    else{
      return connections_[vertex1][vertex2];
    }
  }

  /**
   *  @brief Connects two vertices on a given graph.
   *
   *  The graph is represented by a matrix of boolean values, dinoting the connections.
   *
   *  @param connections The graph.
   *  @param vertex1 The first vertex.
   *  @param vertex2 The second vertex.
   *
   *  @throws IndexOutOfBoundsException If at least one of the vertices given is less than zero of greater of equal to
   *  the number of vertices.
   */
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

  /**
   *  @brief Connects two vertices on a given graph.
   *
   *  The position of the vertices to be connected is provided by row-major, excluding the main diagonal representation.
   *
   *  @param connections The graph.
   *  @param index The position of the vertices to be connected.
   *
   *  @return Returns the corresponding matrix indices for the connected vertices.
   *
   *  @sa connect, Utilities.vectorIndexToUpperTriangularIndices
   */
  private static int[] connect(boolean[][] connections, int index){
    int[] matrixIndices = Utilities.vectorIndexToUpperTriangularIndices(connections.length, index);

    MinimumSpanningTree.connect(connections, matrixIndices[0], matrixIndices[1]);

    return matrixIndices;
  }

  /**
   *  @brief Returns all the possible unique paths upon the tree.
   *
   *  Unique means that, if path 0, 1, 2 is one of the returned paths, then, 2, 1, 0 will be excluded from the returned
   *  paths.
   *
   *  @param maxPathLength The maximum length of the paths.
   *
   *  @return Returns alll the possible unique paths upon the tree.
   *
   *  @sa Utilities.findUniquePaths
   */
  public int[][] getUniquePaths(int maxPathLength){
    return (Utilities.findUniquePaths(connections_, maxPathLength));
  }

  /**
   *  @brief Returns the neighboring vertices of a group of vertices on the tree.
   *
   *  @param vertices The group of vertices.
   *
   *  @return Returns the neighboring vertices of a group of vertices on the tree.
   *
   *  @sa Utilities.getContext
   */
  public int[] getContext(int[] vertices){
    return (Utilities.getContext(vertices, connections_));
  }

  /**
   *  @brief Updates the scope of each vertex when a new connection is added to the tree.
   *
   *  Kruskal's algorithm stops when there is a path from every vertex to every other vertex. That it, when each vertex
   *  has in its scope, all other vertices. This method will update the scopes that Kruskal's algorithm keep, for every
   *  vertex.
   *
   *  @param scope The current scope of each vertex.
   *  @param row The first vertex of the new connection.
   *  @param column The second vertex of the new connection.
   *
   *  @return Returns the updated scope.
   */
  public static boolean[][] updateScope(boolean[][] scope, int row, int column){
    int numberOfVertices = scope.length;

    scope[row][column] = true;
    scope[column][row] = true;

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
          if(scope[i][iterator.next().intValue()]){

            Iterator<Integer> innerIterator = vertexGroup.iterator();
            while(innerIterator.hasNext()){
              int target = innerIterator.next();
              scope[i][target] = true;
              scope[target][i] = true;
            }

            vertexGroup.add(i);

            break;
          }
        }
      }

      newSize = vertexGroup.size();
    }while(oldSize != newSize);

    return scope;
  }

  public boolean[][] connections_; //!< The connections of this MinimumSpanningTree.

}
