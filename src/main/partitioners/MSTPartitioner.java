package main.partitioners;

import java.util.ArrayList;

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

    int numberOfTraces = expression.size();

    // Calculate the distances between all the traces.
    double[] distances = this.calculateDistancesBetweenTraces(expression);

    /* ===== Print Distances ===== */
    System.out.println("===== Print distances =====");
    for(int i = 0;i < distances.length;i++){
      System.out.println("distances[" + i + "] = " + distances[i]);
    }
    /* ===== */

    boolean[][] connections = new boolean[numberOfTraces][numberOfTraces];
    for(int i = 0;i < numberOfTraces;i++){
      for(int j = 0;j < numberOfTraces;j++){
        connections[i][j] = (i == j);
      }
    }

    // Create a minimum spanning tree using the distances between the traces.
    MinimumSpanningTree minimumSpanningTree = MinimumSpanningTree.kruskal(distances, numberOfTraces);

    /* ===== Print minimum spanning tree ===== */
    System.out.println("===== Print Minimum Spanning Tree =====");
    for(int i = 0;i < expression.size();i++){
      for(int j = 0;j < expression.size();j++){
        System.out.print(minimumSpanningTree.areConnected(i, j) + ", ");
      }

      System.out.println();
    }
    /* ===== */

    int[][] uniquePaths = minimumSpanningTree.getUniquePaths(Utilities.MAX_TRACES_IN_SYMBOL);

    /* ===== Print uniquePaths on the minimumSpanning tree ===== */
    System.out.println("===== Minimum Spanning Tree unique paths =====");
    for(int i = 0;i < uniquePaths.length;i++){
      System.out.print("path " + i + " = ");
      for(int j = 0;j < uniquePaths[i].length;j++){
        System.out.print(uniquePaths[i][j] + ", ");
      }

     System.out.println();
    }
    /* ===== Print uniquePaths on the minimumSpanning tree ===== */

    int[][] overlaps = this.findOverlaps(expression);

    /* Also add possible equals sign to overlaps. */
    int[][] equals = this.findEquals(expression);

    int[][] finalOverlaps = new int[overlaps.length + equals.length][];

    for(int i = 0;i < overlaps.length;i++){
      finalOverlaps[i] = overlaps[i];
    }

    for(int i = 0;i < equals.length;i++){
      finalOverlaps[overlaps.length + i] = equals[i];
    }
    overlaps = finalOverlaps;

    /* ===== Print overlaps ===== */
    System.out.println("===== Overlaps =====");
    for(int i = 0;i < overlaps.length;i++){
      System.out.println(overlaps[i][0] + ", " + overlaps[i][1]);
    }
    /* ===== Print overlaps ===== */

    // Delete all the unique Paths that do not comply with the overlaps.
    ArrayList<int[]> clearedPaths = new ArrayList<int[]>();
    boolean foundFirst;
    boolean foundSecond;
    boolean okFlag = true;
    for(int path = 0;path < uniquePaths.length;path++){

      for(int overlap = 0;overlap < overlaps.length;overlap++){
        foundFirst = false;
        foundSecond = false;

        for(int trace = 0;trace < uniquePaths[path].length;trace++){
          if(uniquePaths[path][trace] == overlaps[overlap][0]){
            foundFirst = true;
          }

          if(uniquePaths[path][trace] == overlaps[overlap][1]){
            foundSecond = true;
          }
        }

        if(foundFirst != foundSecond){
          okFlag = false;
          break;
        }
      }

      if(okFlag){
        clearedPaths.add(uniquePaths[path]);
      }
      else{
        okFlag = true;
      }
    }

    uniquePaths = new int[clearedPaths.size()][];
    for(int i = 0;i < clearedPaths.size();i++){
      uniquePaths[i] = clearedPaths.get(i);
    }
    clearedPaths = null;

    /* ===== Print uniquePaths after clearing ===== */
    System.out.println("===== unique paths after clearing =====");
    for(int i = 0;i < uniquePaths.length;i++){
      System.out.print("path " + i + " = ");
      for(int j = 0;j < uniquePaths[i].length;j++){
        System.out.print(uniquePaths[i][j] + ", ");
      }

     System.out.println();
    }
    /* ===== Print uniquePaths after clearing ===== */

    double[] uniquePathsRates = new double[uniquePaths.length];

    TraceGroup symbol;
    TraceGroup context;
    int[] contextIndices;

    for(int i = 0;i < uniquePaths.length;i++){
      // Create the traceGroup of symbols.
      symbol = expression.subTraceGroup(uniquePaths[i]);

      System.out.println("Outer symbol size:" + symbol.size());
      System.out.println("unique path size:" + uniquePaths[i].length);

      // Find the context.
      contextIndices = minimumSpanningTree.getContext(uniquePaths[i]);
      context = expression.subTraceGroup(contextIndices);

      // Evaluate each path and discard garbage.
      uniquePathsRates[i] = classifier_.classify(symbol, context);

      /* ===== Print unique paths rates ===== */
      System.out.println("path " + i + " = " + uniquePathsRates[i]);
      System.out.println(classifier_.getClassificationLabel());
      /* ===== */
    }

    // Get all the possible partitions.
    int numberOfPaths = uniquePaths.length;

    connections = new boolean[numberOfPaths][numberOfPaths];
    for(int i = 0;i < numberOfPaths;i++){
      for(int j = 0;j < numberOfPaths;j++){
        connections[i][j] = true;
      }
    }

    /**
     *  Leave the maxPathLength equal to MAX_TRACES_IN_SYMBOL for now. This is not totally valid
     *  because, even if a partition is of that length, one or more paths might have more than one
     *  vertices and thus the whole partition will have a length greater that MAX_TRACES_IN_SYMBOL.
     */
    int[][] partitions = Utilities.findUniquePaths(connections, numberOfPaths);

    /* ===== Print partitions ===== */
    //System.out.println("===== Print Partitions =====");
    //for(int i = 0;i < partitions.length;i++){
    //  for(int j = 0;j < partitions[i].length;j++){
    //    System.out.print(partitions[i][j] + ", ");
    //  }

    //  System.out.println();
    //}
    /* ===== Print partitions ===== */

    // Find the best partition.
    double maxRate = -1;
    int bestPartition = -1;

    for(int partition = 0;partition < partitions.length;partition++){
      if(this.isEligible(partitions[partition], uniquePaths, expression.size())){

        /* ===== */
        //System.out.println("partition " + partition + " is eligible.");
        /* ===== */

        // Calculate the rate of this partition.
        double currentRate = 0;
        for(int path = 0;path < partitions[partition].length;path++){
          currentRate += uniquePathsRates[partitions[partition][path]];
        }
        currentRate /= partitions[partition].length;

        if(currentRate > maxRate){
          maxRate = currentRate;
          bestPartition = partition;
        }
      }
    }

    // Collect the traces of the best partition into an array of trace groups.
    // Each trace group in the final array, constitutes a symbol.
    TraceGroup[] partition = new TraceGroup[partitions[bestPartition].length];
    for(int i = 0;i < partition.length;i++){
      partition[i] = expression.subTraceGroup(uniquePaths[partitions[bestPartition][i]]);
    }

    return partition;
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

  private int[][] findEquals(TraceGroup expression){
    int numberOfTraces = expression.size();

    ArrayList<int[]> equals = new ArrayList<int[]>();

    for(int i = 0;i < numberOfTraces;i++){
      for(int j = i + 1;j < numberOfTraces;j++){
        Trace trace1 = expression.get(i);
        Trace trace2 = expression.get(j);

        if((trace2.getBottomRightCorner().x_ >= trace1.getTopLeftCorner().x_ && trace2.getTopLeftCorner().x_ <= trace1.getBottomRightCorner().x_) &&
           (trace1.getHeight() <= 0.10 * trace1.getWidth()) &&
           (trace2.getHeight() <= 0.10 * trace2.getWidth())){
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
  /*private double distanceOfTraces(Trace trace1, Trace trace2){
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
  }*/

  private double distanceOfTraces(Trace trace1, Trace trace2){
    if(trace1.size() == 0 || trace2.size() == 0){
      return -1;
    }

    // Return the Euclidean distance between the centroids of the bounding boxes.
    Point centroid1 = trace1.getCentroid();
    Point centroid2 = trace2.getCentroid();

    return (Point.distance(centroid1, centroid2));
  }

}
