package main.partitioners;

import java.util.ArrayList;

import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

import main.utilities.MinimumSpanningTree;
import main.utilities.Point;
import main.utilities.Trace;
import main.utilities.TraceGroup;
import main.utilities.Utilities;

/* MST = Minimum Spanning Tree.*/
public abstract class MSTPartitioner extends Partitioner{

  public TraceGroup[] partition(TraceGroup expression){
    if(expression.size() == 1){
      return (new TraceGroup[] {expression});
    }

    // Calculate the distances between all the traces.
    double[] distances = this.calculateDistancesBetweenTraces(expression);

    // Create a minimum spanning tree using the distances between the traces.
    MinimumSpanningTree minimumSpanningTree = MinimumSpanningTree.kruskal(distances, expression.size());

    // Feed all the possible combinations of arbitrary number of connected
    // traces to the classifier and see if they make a symbol.
    int[][] uniquePaths = minimumSpanningTree.getAllPathsUnique();
    double[] uniquePathsRates = new double[uniquePaths.length];
    boolean[] isGarbage = new boolean[uniquePaths.length];
    int garbageCounter = 0;

    TraceGroup symbol;
    TraceGroup context;
    int[] contextIndices;

    for(int i = 0;i < uniquePaths.length;i++){
      // Create the traceGroup of symbols.
      symbol = expression.subTraceGroup(uniquePaths[i]);

      // Find the context.
      contextIndices = minimumSpanningTree.getContext(uniquePaths[i]);
      context = expression.subTraceGroup(contextIndices);

      // Evaluate each path and discard garbage.
      double rate = classifier_.classify(symbol, context);

      /* ===== */
      System.out.println(rate);
      System.out.println(classifier_.getClassificationLabel());
      /* ===== */

      if(rate >= 0.5){
        uniquePathsRates[i] = rate;
        isGarbage[i] = false;
      }
      else{
        isGarbage[i] = true;
        garbageCounter++;
      }
    }

    /* ===== */
    for(int i = 0;i < uniquePaths.length;i++){
      for(int j = 0;j < uniquePaths[i].length;j++){
        System.out.print(uniquePaths[i][j] + ", ");
      }

      System.out.println();
    }

    for(int i = 0;i < isGarbage.length;i++){
      System.out.println(isGarbage[i]);
    }
    /* ===== */

    // Remove paths that are garbage.
    int index = 0;
    int[][] finalPaths = new int[uniquePaths.length - garbageCounter][];
    double[] finalPathsRates = new double[uniquePaths.length - garbageCounter];
    for(int i = 0;i < uniquePaths.length;i++){
      if(!isGarbage[i]){
        finalPaths[index] = uniquePaths[i];
        finalPathsRates[index] = uniquePathsRates[i];
        index++;
      }
    }

    /* ===== */
    System.out.println("===== Final Paths =====");
    for(int i = 0;i < finalPaths.length;i++){
      for(int j = 0;j < finalPaths[i].length;j++){
        System.out.print(finalPaths[i][j] + ", ");
      }

      System.out.println();
    }
    System.out.println("===== End of Final paths =====");
    /* ===== */

    // Get all the possible partitions.
    ArrayList<ArrayList<Integer> > partitions = new ArrayList<ArrayList<Integer> >();
    int numberOfPaths = finalPaths.length;

    for(int i = 0;i < numberOfPaths;i++){
      ArrayList<Integer> partition = new ArrayList<Integer>();
      partition.add(new Integer(i));
      partitions.add(partition);
    }

    boolean[][] connections = new boolean[numberOfPaths][numberOfPaths];
    for(int i = 0;i < numberOfPaths;i++){
      for(int j = 0;j < numberOfPaths;j++){
        connections[i][j] = true;
      }
    }

    /* ===== */
    System.out.println("Entering find Paths!");
    /* ===== */
    partitions = Utilities.findPaths(partitions, connections);
    System.out.println(partitions.size());

    partitions = Utilities.eliminateDuplicates(partitions);
    System.out.println(partitions.size());

    /* ===== */
    System.out.println("===== Partitions =====");
    for(int i = 0;i < partitions.size();i++){
      for(int j = 0;j < partitions.get(i).size();j++){
        System.out.print(partitions.get(i).get(j) + ", ");
      }

      System.out.println();
    }
    /* ===== */

    // Find the best partition.
    double maxRate = -1;
    int bestPartition = -1;

    for(int partition = 0;partition < partitions.size();partition++){

      /* ===== */
      System.out.println("Partition " + partition + "is eligible: " + this.isEligible(partitions.get(partition), finalPaths, expression.size()));
      /* ===== */


      if(this.isEligible(partitions.get(partition), finalPaths, expression.size())){

        // Calculate the rate of this partition.
        double currentRate = 0;
        for(int path = 0;path < partitions.get(partition).size();path++){
          currentRate += finalPathsRates[partitions.get(partition).get(path).intValue()];
        }

        if(currentRate > maxRate){
          maxRate = currentRate;
          bestPartition = partition;
        }
      }
    }

    /* ===== */
    System.out.print("The best partition is: " + bestPartition);
    /* ===== */

    // Collect the traces of the best partition into an array of trace groups.
    // Each trace group in the final array, constitutes a symbol.
    TraceGroup[] partition = new TraceGroup[partitions.get(bestPartition).size()];
    for(int i = 0;i < partition.length;i++){
      partition[i] = expression.subTraceGroup(finalPaths[partitions.get(bestPartition).get(i)]);
    }

    return partition;
  }

  private boolean isEligible(ArrayList<Integer> partition, int[][] paths, int numberOfTraces){
    int[] tracesOccurenceCounter = new int[numberOfTraces];
    for(int i = 0;i < numberOfTraces;i++){
      tracesOccurenceCounter[i] = 0;
    }

    for(int path = 0;path < partition.size();path++){
      for(int trace = 0;trace < paths[partition.get(path)].length;trace++){
        System.out.println(paths[partition.get(path)][trace]);
        tracesOccurenceCounter[paths[partition.get(path)][trace]]++;
      }
    }

    for(int i = 0;i < numberOfTraces;i++){
      if(tracesOccurenceCounter[i] != 1){
        return false;
      }
    }

    return true;
  }

  private double[] calculateDistancesBetweenTraces(TraceGroup expression){
    int numberOfTraces = expression.size();

    double[] distances = new double[numberOfTraces * (numberOfTraces - 1) / 2];
    int index = 0;
    for(int i = 0;i < numberOfTraces;i++){
      for(int j = i + 1;j < numberOfTraces;j++){
        distances[index] = this.distanceOfTraces(expression.get(i), expression.get(j));
        index++;
      }
    }

    return distances;
  }

  private double distanceOfTraces(Trace trace1, Trace trace2){
    if(trace1.size() == 0 || trace2.size() == 0){
      return -1;
    }

    // Return the minimum distance between the points of the traces.
    double[][] distances = new double[trace1.size()][trace2.size()];
    for(int i = 0;i < trace1.size();i++){
      for(int j = 0;j < trace2.size();j++){
        distances[i][j] = Point.distance(trace1.get(i), trace2.get(j));
      }
    }

    double minimumDistance = distances[0][0];
    for(int i = 0;i < trace1.size();i++){
      for(int j = 0;j < trace2.size();j++){
        if(minimumDistance > distances[i][j]){
          minimumDistance = distances[i][j];
        }
      }
    }

    return minimumDistance;
  }

}
