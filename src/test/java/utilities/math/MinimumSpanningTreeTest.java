package test.java.utilities.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import main.java.utilities.math.MinimumSpanningTree;

/** @class MinimumSpanningTreeTest
 *
 *  @brief Class that contains tests for main.utilities.math.MinimumSpanningTree class.
 */
public class MinimumSpanningTreeTest{

/**
 *  @brief Tests the constructor of main.utilities.math.MinimumSpanningTree class.
 */
  @Test
  public void testMinimumSpanningTree(){
    int numberOfVertices = 10;
    boolean[][] connections = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = (i == j);
      }
    }

    MinimumSpanningTree minimumSpanningTree = new MinimumSpanningTree(connections);

    assertEquals(numberOfVertices, minimumSpanningTree.connections_.length, 0);

    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        assertEquals(i == j, minimumSpanningTree.connections_[i][j]);
      }
    }

    // Assert that the connections of the minimumSpanningTree are different objects than the ones provided as input.
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = (i != j);
      }
    }

    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        assertEquals(i == j, minimumSpanningTree.connections_[i][j]);
      }
    }
  }

  /**
   *  @brief Tests kruskal method of main.utilities.math.MinimumSpanningTree class.
   */
  @Test
  public void testKruskal(){
    double[] edgeWeights = new double[] {12, 100, 16, 100, 100, 100, 13,
                                         16, 100, 100, 100, 14, 100,
                                         12, 100, 14, 100, 100,
                                         13, 100, 100, 100,
                                         14, 100, 15,
                                         15, 100,
                                         14};
    int numberOfVertices = 8;

    MinimumSpanningTree minimumSpanningTree = MinimumSpanningTree.kruskal(edgeWeights, numberOfVertices);

    boolean[][] correctConnections = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        correctConnections[i][j] = (i == j);
      }
    }
    correctConnections[0][1] = true;
    correctConnections[0][7] = true;
    correctConnections[1][6] = true;
    correctConnections[2][3] = true;
    correctConnections[2][5] = true;
    correctConnections[3][4] = true;
    correctConnections[4][7] = true;

    for(int i = 0;i < numberOfVertices;i++){
      for(int j = i + 1;j < numberOfVertices;j++){
        if(correctConnections[i][j]){
          assertTrue(minimumSpanningTree.areConnected(i, j));
        }
        else{
          assertFalse(minimumSpanningTree.areConnected(i, j));
        }
      }
    }

    assertEquals(0, MinimumSpanningTree.kruskal(null, 0).connections_.length, 0);

    // Second example.
    edgeWeights = new double[] {3, 100, 2, 2, 100, 100, 100, 100, 100, 100, 100,
                                2, 100, 100, 3, 100, 100, 100, 100, 100, 100,
                                3, 2, 3, 1, 100, 100, 3, 100, 100,
                                100, 100, 100, 100, 100, 2, 100, 100,
                                100, 100, 100, 100, 100, 100, 100,
                                100, 2, 2, 100, 100, 100,
                                2, 100, 100, 2, 100,
                                100, 100, 100, 2,
                                100, 100, 3,
                                5, 100,
                                3};

    numberOfVertices = 12;

    minimumSpanningTree = MinimumSpanningTree.kruskal(edgeWeights, numberOfVertices);

    correctConnections = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        correctConnections[i][j] = (i == j);
      }
    }

    correctConnections[0][3] = true;
    correctConnections[0][4] = true;
    correctConnections[1][2] = true;
    correctConnections[2][4] = true;
    correctConnections[2][6] = true;
    correctConnections[3][9] = true;
    correctConnections[5][7] = true;
    correctConnections[5][8] = true;
    correctConnections[6][7] = true;
    correctConnections[6][10] = true;
    correctConnections[7][11] = true;

    for(int i = 0;i < numberOfVertices;i++){
      for(int j = i + 1;j < numberOfVertices;j++){
        if(correctConnections[i][j]){
          assertTrue(minimumSpanningTree.areConnected(i, j));
        }
        else{
          assertFalse(minimumSpanningTree.areConnected(i, j));
        }
      }
    }

  }

  /**
   *  @brief Tests areConnected method of main.utilities.math.MinimumSpanningTree class.
   */
  @Test
  public void testAreConnected(){
    int numberOfVertices = 10;
    boolean[][] connections = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = (i == j);
      }
    }

    MinimumSpanningTree minimumSpanningTree = new MinimumSpanningTree(connections);

    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        assertEquals(i == j, minimumSpanningTree.areConnected(i, j));
      }
    }
  }

  /**
   *  @brief Tests connect methods of main.utilities.math.MinimumSpanningTree class.
   */
  @Test
  public void testConnect(){
    int numberOfVertices = 10;
    boolean[][] connections = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = false;
      }
    }

    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        if(j == i){
          MinimumSpanningTree.connect(connections, i, j);
        }
      }
    }

    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        assertEquals(i == j, connections[i][j]);
      }
    }
  }

  /**
   *  @brief Tests getUniquePaths method of main.utilities.math.MinimumSpanningTree class.
   */
  @Test
  public void testGetUniquePaths(){
    double[] edgeWeights = new double[] {12, 100, 16, 100, 100, 100, 13,
                                         16, 100, 100, 100, 14, 100,
                                         12, 100, 14, 100, 100,
                                         13, 100, 100, 100,
                                         14, 100, 15,
                                         15, 100,
                                         14};
    int numberOfVertices = 8;

    MinimumSpanningTree minimumSpanningTree = MinimumSpanningTree.kruskal(edgeWeights, numberOfVertices);

    int[][] paths = minimumSpanningTree.getUniquePaths(numberOfVertices);

    assertEquals(36, paths.length, 0);
  }

  /**
   *  @brief Tests updateScope method of main.utilities.math.MinimumSpanningTree class.
   */
  @Test
  public void testUpdateScope(){
    int numberOfVertices = 5;
    boolean[][] scope = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        scope[i][j] = (i == j);
      }
    }

    scope[0][2] = true; scope[2][0] = true;
    scope[1][4] = true; scope[4][1] = true;

    scope = MinimumSpanningTree.updateScope(scope, 1, 2);

    assertTrue(scope[0][1]); assertTrue(scope[1][0]);
    assertTrue(scope[0][4]); assertTrue(scope[4][0]);
    assertTrue(scope[1][2]); assertTrue(scope[2][1]);

    numberOfVertices = 10;
    scope = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        scope[i][j] = (i == j);
      }
    }

    scope[0][6] = true; scope[6][0] = true;
    scope[1][5] = true; scope[5][1] = true;
    scope[2][9] = true; scope[9][2] = true;
    scope[6][8] = true; scope[8][6] = true;

    scope = MinimumSpanningTree.updateScope(scope, 0, 7);

    assertTrue(scope[0][7]); assertTrue(scope[7][0]);
    assertTrue(scope[6][7]); assertTrue(scope[7][6]);
    assertTrue(scope[7][8]); assertTrue(scope[8][7]);

    scope = MinimumSpanningTree.updateScope(scope, 2, 6);

    assertTrue(scope[0][2]); assertTrue(scope[2][0]);
    assertTrue(scope[0][9]); assertTrue(scope[9][0]);

    assertTrue(scope[2][6]); assertTrue(scope[6][2]);
    assertTrue(scope[6][9]); assertTrue(scope[9][6]);

    assertTrue(scope[2][7]); assertTrue(scope[7][2]);
    assertTrue(scope[7][9]); assertTrue(scope[9][7]);

    assertTrue(scope[2][8]); assertTrue(scope[8][2]);
    assertTrue(scope[8][9]); assertTrue(scope[9][8]);

    scope = MinimumSpanningTree.updateScope(scope, 4, 5);

    assertTrue(scope[1][4]); assertTrue(scope[4][1]);
    assertTrue(scope[4][5]); assertTrue(scope[5][4]);

    scope = MinimumSpanningTree.updateScope(scope, 1, 3);

    assertTrue(scope[1][3]); assertTrue(scope[3][1]);
    assertTrue(scope[3][4]); assertTrue(scope[4][3]);
    assertTrue(scope[3][5]); assertTrue(scope[5][3]);

    scope = MinimumSpanningTree.updateScope(scope, 4, 9);

    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        assertTrue(scope[i][j]);
      }
    }
  }

}
