package tests.utilities.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import main.utilities.math.MinimumSpanningTree;

public class MinimumSpanningTreeTest{

  /** \brief also tests areConnected and numberOfVertices.
   *
   */
  @Test
  public void testMinimumSpanningTree(){
    int numberOfVertices = 5;
    MinimumSpanningTree minimumSpanningTree =
                                      new MinimumSpanningTree(numberOfVertices);
    assertEquals(numberOfVertices, minimumSpanningTree.numberOfVertices(), 0);

    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        if(i == j){
          assertTrue(minimumSpanningTree.areConnected(i, j));
        }
        else{
          assertFalse(minimumSpanningTree.areConnected(i, j));
        }
      }
    }

    boolean[][] connections = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        connections[i][j] = !(i == j);
      }
    }

    minimumSpanningTree = new MinimumSpanningTree(connections);

    assertEquals(numberOfVertices, minimumSpanningTree.numberOfVertices(), 0);

    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        if(i == j){
          assertFalse(minimumSpanningTree.areConnected(i, j));
        }
        else{
          assertTrue(minimumSpanningTree.areConnected(i, j));
        }
      }
    }

  }

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

    assertEquals(0, MinimumSpanningTree.kruskal(null, 0).numberOfVertices(), 0);

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

  @Test
  public void testConnectDisconnect(){
    int numberOfVertices = 10;
    MinimumSpanningTree minimumSpanningTree = new MinimumSpanningTree(numberOfVertices);

    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        if(j == i){
          minimumSpanningTree.connect(i, j);
        }
        else{
          minimumSpanningTree.disconnect(i, j);
        }
      }
    }

    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        if(j == i){
          assertTrue(minimumSpanningTree.areConnected(i, j));
        }
        else{
          assertFalse(minimumSpanningTree.areConnected(i, j));
        }
      }
    }

  }

  @Test
  public void testAddConnection() throws IllegalArgumentException,
                                         InvocationTargetException,
                                         IllegalAccessException,
                                         NoSuchMethodException,
                                         SecurityException{
    Class[] arguments = new Class[2];
    arguments[0] = boolean[][].class;
    arguments[1] = int.class;
    Method addConnection = MinimumSpanningTree.class.getDeclaredMethod(
                                                    "addConnection", arguments);
    addConnection.setAccessible(true);

    boolean[][] connections = new boolean[5][5];
    for(int i = 0;i < 5;i++){
      for(int j = 0;j < 5;j++){
        connections[i][j] = false;
      }
    }

    addConnection.invoke(null, connections, 5);
    assertTrue(connections[1][3]);

    addConnection.invoke(null, connections, 9);
    assertTrue(connections[3][4]);
  }

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

  @Test
  public void testUpdateCanReach(){
    int numberOfVertices = 5;
    boolean[][] canReach = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        canReach[i][j] = (i == j);
      }
    }

    canReach[0][2] = true; canReach[2][0] = true;
    canReach[1][4] = true; canReach[4][1] = true;

    canReach = MinimumSpanningTree.updateCanReach(canReach, 1, 2);

    assertTrue(canReach[0][1]); assertTrue(canReach[1][0]);
    assertTrue(canReach[0][4]); assertTrue(canReach[4][0]);
    assertTrue(canReach[1][2]); assertTrue(canReach[2][1]);

    numberOfVertices = 10;
    canReach = new boolean[numberOfVertices][numberOfVertices];
    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        canReach[i][j] = (i == j);
      }
    }

    canReach[0][6] = true; canReach[6][0] = true;
    canReach[1][5] = true; canReach[5][1] = true;
    canReach[2][9] = true; canReach[9][2] = true;
    canReach[6][8] = true; canReach[8][6] = true;

    canReach = MinimumSpanningTree.updateCanReach(canReach, 0, 7);

    assertTrue(canReach[0][7]); assertTrue(canReach[7][0]);
    assertTrue(canReach[6][7]); assertTrue(canReach[7][6]);
    assertTrue(canReach[7][8]); assertTrue(canReach[8][7]);

    canReach = MinimumSpanningTree.updateCanReach(canReach, 2, 6);

    assertTrue(canReach[0][2]); assertTrue(canReach[2][0]);
    assertTrue(canReach[0][9]); assertTrue(canReach[9][0]);

    assertTrue(canReach[2][6]); assertTrue(canReach[6][2]);
    assertTrue(canReach[6][9]); assertTrue(canReach[9][6]);

    assertTrue(canReach[2][7]); assertTrue(canReach[7][2]);
    assertTrue(canReach[7][9]); assertTrue(canReach[9][7]);

    assertTrue(canReach[2][8]); assertTrue(canReach[8][2]);
    assertTrue(canReach[8][9]); assertTrue(canReach[9][8]);

    canReach = MinimumSpanningTree.updateCanReach(canReach, 4, 5);

    assertTrue(canReach[1][4]); assertTrue(canReach[4][1]);
    assertTrue(canReach[4][5]); assertTrue(canReach[5][4]);

    canReach = MinimumSpanningTree.updateCanReach(canReach, 1, 3);

    assertTrue(canReach[1][3]); assertTrue(canReach[3][1]);
    assertTrue(canReach[3][4]); assertTrue(canReach[4][3]);
    assertTrue(canReach[3][5]); assertTrue(canReach[5][3]);

    canReach = MinimumSpanningTree.updateCanReach(canReach, 4, 9);

    for(int i = 0;i < numberOfVertices;i++){
      for(int j = 0;j < numberOfVertices;j++){
        assertTrue(canReach[i][j]);
      }
    }
  }

}
