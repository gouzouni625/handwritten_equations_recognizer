package main.partitioners;

import java.util.ArrayList;

import main.utilities.Utilities;
import main.utilities.math.MinimumSpanningTree;
import main.utilities.traces.Point;
import main.utilities.traces.Trace;
import main.utilities.traces.TraceGroup;
import main.utilities.Callable;

/* MST = Minimum Spanning Tree.*/
public abstract class MSTPartitioner extends Partitioner{

  public TraceGroup[] partition(TraceGroup expression){
    int numberOfTraces = expression.size();

    if(numberOfTraces == 1){

      labels_ = new int[numberOfTraces];
      classifier_.classify(expression, new TraceGroup(), true, true);
      labels_[0] = classifier_.getClassificationLabel();

      return (new TraceGroup[] {expression});
    }

    // Calculate the distances between all the traces.
    double[] distances = this.calculateDistancesBetweenTraces(expression);

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: distances between traces... ===== Start =====");

      for(int i = 0;i < distances.length;i++){
        System.out.println("distance " + i + ": " + distances[i]);
      }

      System.out.println("Log: distances between traces... ===== End =======");
    }
    /* ===== Logs ===== */

    // Create a minimum spanning tree using the distances between the traces.
    MinimumSpanningTree minimumSpanningTree = MinimumSpanningTree.kruskal(distances, numberOfTraces);

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: minimum spanning tree... ===== Start =====");

      for(int i = 0;i < numberOfTraces;i++){
        for(int j = 0;j < numberOfTraces;j++){
          System.out.print(minimumSpanningTree.areConnected(i, j) + ", ");
        }

        System.out.println();
      }

      System.out.println("Log: minimum spanning tree... ===== End =======");
    }
    /* ===== Logs ===== */

    int[][] paths = minimumSpanningTree.getUniquePaths(Utilities.MAX_TRACES_IN_SYMBOL);
    int numberOfPaths = paths.length;

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: paths upon the minimum spanning tree... ===== Start =====");

      for(int i = 0;i < numberOfPaths;i++){
        System.out.print("path " + i + " = ");
        for(int j = 0;j < paths[i].length;j++){
          System.out.print(paths[i][j] + ", ");
        }

       System.out.println();
      }

      System.out.println("Log: paths upon the minimum spanning tree... ===== End =======");
    }
    /* ===== Logs ===== */

    int[][] overlaps = Utilities.concatenateArrays(this.findOverlaps(expression), this.findEqualsSymbol(expression));
    int numberOfOverlaps = overlaps.length;

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: overlaps... ===== Start =====");

      for(int i = 0;i < numberOfOverlaps;i++){
        System.out.println(overlaps[i][0] + ", " + overlaps[i][1]);
      }

      System.out.println("Log: overlaps... ===== End =======");
    }
    /* ===== Logs ===== */

    // Delete all the paths that do not comply with the overlaps.
    ArrayList<Integer> pathsToClear = new ArrayList<Integer>();
    boolean foundFirstOverlapIndex;
    boolean foundSecondOvelapIndex;
    boolean clearFlag = false;
    for(int path = 0;path < numberOfPaths;path++){
      for(int overlap = 0;overlap < numberOfOverlaps;overlap++){
        foundFirstOverlapIndex = false;
        foundSecondOvelapIndex = false;

        for(int trace = 0;trace < paths[path].length;trace++){
          if(paths[path][trace] == overlaps[overlap][0]){
            foundFirstOverlapIndex = true;
          }

          if(paths[path][trace] == overlaps[overlap][1]){
            foundSecondOvelapIndex = true;
          }
        }

        if(foundFirstOverlapIndex != foundSecondOvelapIndex){
          clearFlag = true;
          break;
        }
      }

      if(clearFlag){
        pathsToClear.add(path);
      }

      clearFlag = false;
    }
    paths = Utilities.removeRows(paths, pathsToClear);
    numberOfPaths = paths.length;

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: paths after removing those that didn't satisfy the overlaps... ===== Start =====");

      for(int i = 0;i < numberOfPaths;i++){
        System.out.print("path " + i + " = ");
        for(int j = 0;j < paths[i].length;j++){
          System.out.print(paths[i][j] + ", ");
        }

       System.out.println();
      }

      System.out.println("Log: paths after removing those that didn't satisfy the overlaps... ===== End =======");
    }
    /* ===== Logs ===== */

    double[] pathsRates = new double[numberOfPaths];
    int[] pathsLabels = new int[numberOfPaths];

    TraceGroup symbol;
    TraceGroup context;
    int[] contextIndices;

    for(int i = 0;i < numberOfPaths;i++){
      // Create the traceGroup of symbols.
      symbol = expression.subTraceGroup(paths[i]);

      // Find the context.
      contextIndices = minimumSpanningTree.getContext(paths[i]);
      context = expression.subTraceGroup(contextIndices);

      // Evaluate each path and discard garbage.
      boolean subSymbolCheck = !Utilities.rowInArray(overlaps, paths[i], false);
      pathsRates[i] = classifier_.classify(symbol, context, subSymbolCheck, true);
      pathsLabels[i] = classifier_.getClassificationLabel();

      /* ===== Logs ===== */
      if(!silent_){
        System.out.println("Log: path rate and label... ===== Start =====");

        System.out.println("path " + i + " subSymbolCheck: " + subSymbolCheck);
        System.out.println("path " + i + " rate: " + pathsRates[i]);
        System.out.println("path " + i + " label: " + pathsLabels[i]);

        System.out.println("Log: path rate and label... ===== End =======");
      }
      /* ===== Logs ===== */
    }

    // Get all the possible partitions.
    boolean[][] connections = new boolean[numberOfPaths][numberOfPaths];
    for(int i = 0;i < numberOfPaths;i++){
      for(int j = i + 1;j < numberOfPaths;j++){
        connections[i][j] = this.areCombinable(paths[i], paths[j]);
        connections[j][i] = connections[i][j];
      }

      connections[i][i] = false;
    }

    int[][] partitions = Utilities.findUniquePaths(connections, numberOfPaths, new PartitionCheck(paths, numberOfTraces));
    int numberOfPartitions = partitions.length;

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: all partitions... ===== Start =====");

      for(int i = 0;i < numberOfPartitions;i++){
        for(int j = 0;j < partitions[i].length;j++){
          System.out.print(partitions[i][j] + ", ");
        }

        System.out.println();
      }

      System.out.println("Log: all partitions... ===== End =======");
    }
    /* ===== Logs ===== */

    // remove partitions that are not eligible(e.g. they contain the same trace more than once).
    ArrayList<Integer> partitionsToRemove = new ArrayList<Integer>();
    for(int partition = 0;partition < numberOfPartitions;partition++){
      if(!this.isEligible(partitions[partition], paths, numberOfTraces)){
        partitionsToRemove.add(partition);
      }
    }
    partitions = Utilities.removeRows(partitions, partitionsToRemove);
    numberOfPartitions = partitions.length;

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: partitions after removing those that were not eligible... ===== Start =====");

      for(int i = 0;i < numberOfPartitions;i++){
        System.out.print("partition " + i + " = ");
        for(int j = 0;j < partitions[i].length;j++){
          System.out.print(partitions[i][j] + ", ");
        }

       System.out.println();
      }

      System.out.println("Log: partitions after removing those that were not eligible... ===== End =======");
    }
    /* ===== Logs ===== */

    // Find the best partition.
    double currentRate = 0;
    for(int path = 0;path < partitions[0].length;path++){
      currentRate += pathsRates[partitions[0][path]];
    }

    double maxRate = currentRate;
    int bestPartition = 0;

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: partition rate... ===== Start =====");

      System.out.println("partition 0 rate: " + currentRate);

      System.out.println("Log: partition rate... ===== End =======");
    }
    /* ===== Logs ===== */

    for(int partition = 1;partition < numberOfPartitions;partition++){
      // Calculate the rate of this partition.
      currentRate = 0;
      for(int path = 0;path < partitions[partition].length;path++){
        currentRate += pathsRates[partitions[partition][path]];
      }

      /* ===== Logs ===== */
      if(!silent_){
        System.out.println("Log: partition rate... ===== Start =====");

        System.out.println("partition " +  partition + " rate: " + currentRate);

        System.out.println("Log: partition rate... ===== End =======");
      }
      /* ===== Logs ===== */

      if(currentRate > maxRate){
        maxRate = currentRate;
        bestPartition = partition;
      }
    }

    // Collect the traces of the best partition into an array of trace groups.
    // Each trace group in the final array, constitutes a symbol.
    TraceGroup[] partition = new TraceGroup[partitions[bestPartition].length];
    labels_ = new int[partitions[bestPartition].length];
    for(int i = 0;i < partition.length;i++){
      partition[i] = expression.subTraceGroup(paths[partitions[bestPartition][i]]);
      labels_[i] = pathsLabels[partitions[bestPartition][i]];
    }

    return partition;
  }

  public int[] getLabels(){
    return labels_;
  }

  private boolean isEligible(int[] partition, int[][] paths, int numberOfTraces){
    int[] tracesOccurenceCounter = new int[numberOfTraces];
    for(int i = 0;i < numberOfTraces;i++){
      tracesOccurenceCounter[i] = 0;
    }

    for(int path = 0;path < partition.length;path++){
      for(int trace = 0;trace < paths[partition[path]].length;trace++){
        tracesOccurenceCounter[paths[partition[path]][trace]]++;
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

  private int[][] findOverlaps(TraceGroup expression){
    int numberOfTraces = expression.size();

    ArrayList<int[]> overlaps = new ArrayList<int[]>();

    for(int i = 0;i < numberOfTraces;i++){
      for(int j = i + 1;j < numberOfTraces;j++){
        if(Trace.areOverlapped(expression.get(i), expression.get(j))){
          overlaps.add(new int[] {i, j});
        }
      }
    }

    // Convert array list to array.
    int[][] overlapsArray = new int[overlaps.size()][2];
    for(int i = 0;i < overlaps.size();i++){
      overlapsArray[i][0] = overlaps.get(i)[0];
      overlapsArray[i][1] = overlaps.get(i)[1];
    }

    return overlapsArray;
  }

  private int[][] findEqualsSymbol(TraceGroup expression){
    int numberOfTraces = expression.size();

    ArrayList<int[]> equals = new ArrayList<int[]>();

    for(int i = 0;i < numberOfTraces;i++){
      for(int j = i + 1;j < numberOfTraces;j++){
        if(this.areEqualsSymbol(expression.get(i), expression.get(j))){
          equals.add(new int[] {i, j});
        }
      }
    }

    // Convert array list to array.
    int[][] equalsArray = new int[equals.size()][2];
    for(int i = 0;i < equals.size();i++){
      equalsArray[i][0] = equals.get(i)[0];
      equalsArray[i][1] = equals.get(i)[1];
    }

    return equalsArray;
  }

  private boolean areEqualsSymbol(Trace trace1, Trace trace2){
    if((trace2.getBottomRightCorner().x_ >= trace1.getTopLeftCorner().x_ && trace2.getTopLeftCorner().x_ <= trace1.getBottomRightCorner().x_) &&
        (trace1.getHeight() <= 0.40 * trace1.getWidth()) &&
        (trace2.getHeight() <= 0.40 * trace2.getWidth())){
      return true;
    }

    return false;
  }

  private double distanceOfTraces(Trace trace1, Trace trace2){
    if(trace1.size() == 0 || trace2.size() == 0){
      return -1;
    }

    Point centroid1 = trace1.getCentroid();
    Point centroid2 = trace2.getCentroid();

    return (Point.distance(centroid1, centroid2));
  }

  public void setSilent(boolean silent){
    silent_ = silent;
  }

  public boolean getSilent(){
    return silent_;
  }

  private boolean areCombinable(int[] path1, int[] path2){
    int length1 = path1.length;
    int length2 = path2.length;

    for(int i = 0;i < length1;i++){
      for(int j = 0;j < length2;j++){
        if(path1[i] == path2[j]){
          return false;
        }
      }
    }

    return true;
  }

  private boolean silent_ = true;

  private int[] labels_;

  private class PartitionCheck implements Callable{

    public PartitionCheck(int[][] paths, int numberOfTraces){
      paths_ = paths;
      numberOfTraces_ = numberOfTraces;
    }

    public boolean call(ArrayList<Integer> list){
      int[] tracesOccurenceCounter = new int[numberOfTraces_];
      for(int i = 0;i < numberOfTraces_;i++){
        tracesOccurenceCounter[i] = 0;
      }

      for(int path = 0;path < list.size();path++){
        for(int trace = 0;trace < paths_[list.get(path)].length;trace++){
          tracesOccurenceCounter[paths_[list.get(path)][trace]]++;
        }
      }

      for(int i = 0;i < numberOfTraces_;i++){
        if(tracesOccurenceCounter[i] > 1){
          return false;
        }
      }

      return true;
    }

    private int[][] paths_;
    private int numberOfTraces_;
  }

}
