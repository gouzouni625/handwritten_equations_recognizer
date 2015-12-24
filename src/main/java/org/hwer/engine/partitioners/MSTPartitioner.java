package org.hwer.engine.partitioners;

import java.util.ArrayList;

import org.hwer.engine.classifiers.Classifier;
import org.hwer.engine.utilities.PathExtentionCheck;
import org.hwer.engine.utilities.Utilities;
import org.hwer.engine.utilities.math.MinimumSpanningTree;
import org.hwer.engine.utilities.symbols.Operator;
import org.hwer.engine.utilities.symbols.Symbol;
import org.hwer.engine.utilities.symbols.SymbolFactory;
import org.hwer.engine.utilities.traces.Point;
import org.hwer.engine.utilities.traces.Trace;
import org.hwer.engine.utilities.traces.TraceGroup;

/**
 * @class MSTPartitioner
 * @brief A Partitioner that uses a Minimum Spanning Tree to partition the given equation.
 */
public class MSTPartitioner extends Partitioner {
  public MSTPartitioner () {
    super();
  }

  public MSTPartitioner (Classifier classifier) {
    super(classifier);
  }

  /**
   * @param expression The main.java.utilities.traces.TraceGroup with the ink traces of the equation.
   * @return Returns an array of main.java.utilities.traces.TraceGroup each one of which contains the traces of a single
   * symbol.
   * @brief Partitions a group of ink traces of an equation.
   * <p>
   * The result is an array of groups of ink traces each one of which represents a symbol of the equation.
   */
  public TraceGroup[] partition (TraceGroup expression) throws IllegalArgumentException {
    if (expression == null) {
      throw new IllegalArgumentException();
    }

    expression_ = expression;

    int numberOfTraces = expression.size();

    if (numberOfTraces == 0) {
      return new TraceGroup[0];
    }
    else if (numberOfTraces == 1) {
      labels_ = new int[numberOfTraces];
      double rate = classifier_.classify(expression, null, false, false);
      labels_[0] = classifier_.getClassificationLabel();

      /* ===== Logs ===== */
      if (! silent_) {
        System.out.println("Log: path rate and label... ===== Start =====");

        System.out.println("path " + 0 + " subSymbolCheck: " + false);
        System.out.println("path " + 0 + " rate: " + rate);
        System.out.println("path " + 0 + " label: " + labels_[0]);

        System.out.println("Log: path rate and label... ===== End =======");
      }
      /* ===== Logs ===== */

      return (new TraceGroup[]{expression});
    }

    // Calculate the distances between all the traces.
    double[] distances = this.calculateDistancesBetweenTraces(expression);

    /* ===== Logs ===== */
    if (! silent_) {
      System.out.println("Log: distances between traces... ===== Start =====");

      for (int i = 0; i < distances.length; i++) {
        System.out.println("distance " + i + ": " + distances[i]);
      }

      System.out.println("Log: distances between traces... ===== End =======");
    }
    /* ===== Logs ===== */

    // Create a minimum spanning tree using the distances between the traces.
    MinimumSpanningTree minimumSpanningTree = MinimumSpanningTree.kruskal(distances, numberOfTraces);

    /* ===== Logs ===== */
    if (! silent_) {
      System.out.println("Log: minimum spanning tree... ===== Start =====");

      for (int i = 0; i < numberOfTraces; i++) {
        for (int j = 0; j < numberOfTraces; j++) {
          System.out.print(minimumSpanningTree.areConnected(i, j) + ", ");
        }

        System.out.println();
      }

      System.out.println("Log: minimum spanning tree... ===== End =======");
    }
    /* ===== Logs ===== */

    // Get all the unique paths upon the minimum spanning tree.
    int[][] paths = minimumSpanningTree.getUniquePaths(MAX_TRACES_IN_SYMBOL);
    int numberOfPaths = paths.length;

    /* ===== Logs ===== */
    if (! silent_) {
      System.out.println("Log: paths upon the minimum spanning tree... ===== Start =====");

      for (int i = 0; i < numberOfPaths; i++) {
        System.out.print("path " + i + " = ");
        for (int j = 0; j < paths[i].length; j++) {
          System.out.print(paths[i][j] + ", ");
        }

        System.out.println();
      }

      System.out.println("Log: paths upon the minimum spanning tree... ===== End =======");
    }
    /* ===== Logs ===== */

    // Find the overlapping traces.
    int[][] overlaps = Utilities.concatenateArrays(this.findOverlaps(expression), this.findEqualsSymbol(expression));
    int numberOfOverlaps = overlaps.length;

    /* ===== Logs ===== */
    if (! silent_) {
      System.out.println("Log: overlaps... ===== Start =====");

      for (int i = 0; i < numberOfOverlaps; i++) {
        System.out.println(overlaps[i][0] + ", " + overlaps[i][1]);
      }

      System.out.println("Log: overlaps... ===== End =======");
    }
    /* ===== Logs ===== */

    // Delete all the paths that do not comply with the overlaps. That is, a path that contains one symbol of an overlap
    // but not the other is removed.
    ArrayList<Integer> pathsToClear = new ArrayList<Integer>();
    boolean foundFirstOverlapIndex;
    boolean foundSecondOvelapIndex;
    boolean clearFlag = false;
    for (int path = 0; path < numberOfPaths; path++) {
      for (int overlap = 0; overlap < numberOfOverlaps; overlap++) {
        foundFirstOverlapIndex = false;
        foundSecondOvelapIndex = false;

        for (int trace = 0; trace < paths[path].length; trace++) {
          if (paths[path][trace] == overlaps[overlap][0]) {
            foundFirstOverlapIndex = true;
          }

          if (paths[path][trace] == overlaps[overlap][1]) {
            foundSecondOvelapIndex = true;
          }
        }

        if (foundFirstOverlapIndex != foundSecondOvelapIndex) {
          clearFlag = true;
          break;
        }
      }

      if (clearFlag) {
        pathsToClear.add(path);
      }

      clearFlag = false;
    }
    paths = Utilities.removeRows(paths, pathsToClear);
    numberOfPaths = paths.length;

    /* ===== Logs ===== */
    if (! silent_) {
      System.out.println("Log: paths after removing those that didn't satisfy the overlaps... ===== Start =====");

      for (int i = 0; i < numberOfPaths; i++) {
        System.out.print("path " + i + " = ");
        for (int j = 0; j < paths[i].length; j++) {
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
    //TraceGroup context;
    //int[] contextIndices;

    for (int i = 0; i < numberOfPaths; i++) {
      // Create the traceGroup of symbols.
      symbol = expression.subTraceGroup(paths[i]);

      // Find the context.
      //contextIndices = minimumSpanningTree.getContext(paths[i]);
      //context = expression.subTraceGroup(contextIndices);

      // Evaluate each path and discard garbage.
      //boolean subSymbolCheck = !Utilities.rowInArray(overlaps, paths[i], false);
      pathsRates[i] = classifier_.classify(symbol, null, false, false);
      pathsLabels[i] = classifier_.getClassificationLabel();

      /* ===== Logs ===== */
      if (! silent_) {
        System.out.println("Log: path rate and label... ===== Start =====");

        System.out.println("path " + i + " subSymbolCheck: " + false);
        System.out.println("path " + i + " rate: " + pathsRates[i]);
        System.out.println("path " + i + " label: " + pathsLabels[i]);

        System.out.println("Log: path rate and label... ===== End =======");
      }
      /* ===== Logs ===== */
    }

    // Get all the possible partitions.
    boolean[][] connections = new boolean[numberOfPaths][numberOfPaths];
    for (int i = 0; i < numberOfPaths; i++) {
      for (int j = i + 1; j < numberOfPaths; j++) {
        connections[i][j] = this.areCombinable(paths[i], paths[j]);
        connections[j][i] = connections[i][j];
      }

      connections[i][i] = false;
    }

    int[][] partitions = Utilities.findUniquePaths(connections, numberOfPaths, new PartitionCheck(paths, numberOfTraces));
    int numberOfPartitions = partitions.length;

    /* ===== Logs ===== */
    if (! silent_) {
      System.out.println("Log: all partitions... ===== Start =====");

      for (int i = 0; i < numberOfPartitions; i++) {
        for (int j = 0; j < partitions[i].length; j++) {
          System.out.print(partitions[i][j] + ", ");
        }

        System.out.println();
      }

      System.out.println("Log: all partitions... ===== End =======");
    }
    /* ===== Logs ===== */

    // remove partitions that are not eligible(e.g. they contain the same trace more than once).
    ArrayList<Integer> partitionsToRemove = new ArrayList<Integer>();
    for (int partition = 0; partition < numberOfPartitions; partition++) {
      if (! this.isPartitionEligible(partitions[partition], paths, numberOfTraces)) {
        partitionsToRemove.add(partition);
      }
    }
    partitions = Utilities.removeRows(partitions, partitionsToRemove);
    numberOfPartitions = partitions.length;

    /* ===== Logs ===== */
    if (! silent_) {
      System.out.println("Log: partitions after removing those that were not eligible... ===== Start =====");

      for (int i = 0; i < numberOfPartitions; i++) {
        System.out.print("partition " + i + " = ");
        for (int j = 0; j < partitions[i].length; j++) {
          System.out.print(partitions[i][j] + ", ");
        }

        System.out.println();
      }

      System.out.println("Log: partitions after removing those that were not eligible... ===== End =======");
    }
    /* ===== Logs ===== */

    // Find the best partition.
    double currentRate = 0;
    for (int path = 0; path < partitions[0].length; path++) {
      currentRate += pathsRates[partitions[0][path]];
    }

    double maxRate = currentRate;
    int bestPartition = 0;

    /* ===== Logs ===== */
    if (! silent_) {
      System.out.println("Log: partition rate... ===== Start =====");

      System.out.println("partition 0 rate: " + currentRate);

      System.out.println("Log: partition rate... ===== End =======");
    }
    /* ===== Logs ===== */

    for (int partition = 1; partition < numberOfPartitions; partition++) {
      // Calculate the rate of this partition.
      currentRate = 0;
      for (int path = 0; path < partitions[partition].length; path++) {
        currentRate += pathsRates[partitions[partition][path]];
      }

      /* ===== Logs ===== */
      if (! silent_) {
        System.out.println("Log: partition rate... ===== Start =====");

        System.out.println("partition " + partition + " rate: " + currentRate);

        System.out.println("Log: partition rate... ===== End =======");
      }
      /* ===== Logs ===== */

      if (currentRate > maxRate) {
        maxRate = currentRate;
        bestPartition = partition;
      }
    }

    // Collect the traces of the best partition into an array of trace groups.
    // Each trace group in the final array, constitutes a symbol.
    TraceGroup[] partition = new TraceGroup[partitions[bestPartition].length];
    labels_ = new int[partitions[bestPartition].length];
    for (int i = 0; i < partition.length; i++) {
      partition[i] = expression.subTraceGroup(paths[partitions[bestPartition][i]]);
      labels_[i] = pathsLabels[partitions[bestPartition][i]];
    }

    return partition;
  }

  public TraceGroup[] append(Symbol[] symbols, TraceGroup newTraces){
    if(symbols == null || symbols.length == 0){
      return partition(newTraces);
    }

    int numberOfSymbols = symbols.length;
    int numberOfNewTraces = newTraces.size();

    TraceGroup freeTraces = new TraceGroup();

    boolean continueFlag = false;
    for(int i = 0;i < numberOfNewTraces;i++){
      for(int j = 0;j < numberOfSymbols;j++){
        if(symbols[j].traceGroup_.isOverlappedBy(newTraces.get(i))){
          symbols[j].traceGroup_.add(newTraces.get(i));

          continueFlag = true;
          break;
        }
        else{
          TraceGroup combined = new TraceGroup(symbols[j].traceGroup_).add(newTraces.get(i));
          System.out.println("rate: " + classifier_.classify(combined, null, false, false));
          System.out.println("classification label: " + classifier_.getClassificationLabel());
          System.out.println(i + ", " + j);

          symbols[j].traceGroup_.calculateCorners();
          newTraces.get(i).calculateCorners();

          if((classifier_.getClassificationLabel() == SymbolFactory.getLabelByType(Operator.Types.EQUALS)) &&
              (symbols[j].type_ != Operator.Types.FRACTION_LINE && symbols[j].type_ != Operator.Types.EQUALS) &&
              (symbols[j].parent_ == null) &&
              (symbols[j].traceGroup_.getWidth() >= 0.5 * newTraces.get(i).getWidth())){
            symbols[j].traceGroup_.add(newTraces.get(i));

              continueFlag = true;
              break;
          }
        }
      }

      if(continueFlag){
        continueFlag = false;
        continue;
      }
      else{
        freeTraces.add(newTraces.get(i));
      }
    }

    for(int i = 0;i < numberOfSymbols;i++){
      double rate = classifier_.classify(symbols[i].traceGroup_, null, false, false);
      symbols[i] = SymbolFactory.createByLabel(symbols[i].traceGroup_, classifier_.getClassificationLabel());

      /* ===== Logs ===== */
      if (! silent_) {
        System.out.println("Log: path rate and label... ===== Start =====");

        System.out.println("path " + i + " subSymbolCheck: " + false);
        System.out.println("path " + i + " rate: " + rate);
        System.out.println("path " + i + " label: " + classifier_.getClassificationLabel());

        System.out.println("Log: path rate and label... ===== End =======");
      }
      /* ===== Logs ===== */
    }

    return partition(freeTraces);
  }



  /**
   * @return Returns the labels calculated by main.java.partitioners.MSTPartitioner.partition method.
   * @brief Getter method for the labels of each symbol.
   * <p>
   * The labels are the ones calculated by main.java.partitioners.MSTPartitioner.partition method.
   */
  public int[] getLabels () {
    return labels_;
  }

  /**
   * @param partition      The partition to be checked.
   * @param paths          All the paths upon the minimum spanning tree.
   * @param numberOfTraces The total number of ink traces in the equation.
   * @return Returns true if each ink trace is used exactly once by the given partition.
   * @brief Checks if a partition is eligible.
   * <p>
   * Concretely, it checks if an ink trace is used by, at least, two different paths in the given partition.
   */
  private boolean isPartitionEligible (int[] partition, int[][] paths, int numberOfTraces) {
    int[] tracesOccurenceCounter = new int[numberOfTraces];
    for (int i = 0; i < numberOfTraces; i++) {
      tracesOccurenceCounter[i] = 0;
    }

    for (int path = 0; path < partition.length; path++) {
      for (int trace = 0; trace < paths[partition[path]].length; trace++) {
        tracesOccurenceCounter[paths[partition[path]][trace]]++;
      }
    }

    for (int i = 0; i < numberOfTraces; i++) {
      if (tracesOccurenceCounter[i] != 1) {
        return false;
      }
    }

    return true;
  }

  /**
   * @param expression The equation.
   * @return Returns the distances between all the ink traces.
   * @brief Calculates the distances between all the ink traces of a given equation.
   */
  private double[] calculateDistancesBetweenTraces (TraceGroup expression) {
    int numberOfTraces = expression.size();

    double[] distances = new double[numberOfTraces * (numberOfTraces - 1) / 2];
    int index = 0;
    for (int i = 0; i < numberOfTraces; i++) {
      for (int j = i + 1; j < numberOfTraces; j++) {
        distances[index] = this.distanceOfTraces(expression.get(i), expression.get(j));
        index++;
      }
    }

    return distances;
  }

  /**
   * @param trace1 The first Trace.
   * @param trace2 The second Trace.
   * @return Returns the distance of the two traces.
   * @brief Calculates the distance between two main.java.utilities.traces.Trace.
   */
  private double distanceOfTraces (Trace trace1, Trace trace2) {
    if (trace1.size() == 0 || trace2.size() == 0) {
      return - 1;
    }

    Point centerOfMass1 = trace1.getCenterOfMass();
    Point centerOfMass2 = trace2.getCenterOfMass();

    return (Point.distance(centerOfMass1, centerOfMass2));
  }

  /**
   * @param expression The equation.
   * @return Returns the overlapping pairs of of ink traces.
   * @brief Finds which ink traces inside an equation are overlapped.
   */
  private int[][] findOverlaps (TraceGroup expression) {
    int numberOfTraces = expression.size();

    ArrayList<int[]> overlaps = new ArrayList<int[]>();

    for (int i = 0; i < numberOfTraces; i++) {
      for (int j = i + 1; j < numberOfTraces; j++) {
        if (Trace.areOverlapped(expression.get(i), expression.get(j))) {
          overlaps.add(new int[]{i, j});
        }
      }
    }

    // Convert array list to array.
    int[][] overlapsArray = new int[overlaps.size()][2];
    for (int i = 0; i < overlaps.size(); i++) {
      overlapsArray[i][0] = overlaps.get(i)[0];
      overlapsArray[i][1] = overlaps.get(i)[1];
    }

    return overlapsArray;
  }

  /**
   * @param expression The equation.
   * @return Returns the pairs of traces that create an equals symbol.
   * @brief Finds all equals symbols in an equation.
   */
  private int[][] findEqualsSymbol (TraceGroup expression) {
    int numberOfTraces = expression.size();

    ArrayList<int[]> equals = new ArrayList<int[]>();

    for (int i = 0; i < numberOfTraces; i++) {
      for (int j = i + 1; j < numberOfTraces; j++) {
        if (this.areEqualsSymbol(expression.get(i), expression.get(j))) {
          equals.add(new int[]{i, j});
        }
      }
    }

    // Convert array list to array.
    int[][] equalsArray = new int[equals.size()][2];
    for (int i = 0; i < equals.size(); i++) {
      equalsArray[i][0] = equals.get(i)[0];
      equalsArray[i][1] = equals.get(i)[1];
    }

    return equalsArray;
  }

  /**
   * @param trace1 The first trace.
   * @param trace2 The second trace.
   * @return Returns true if the two traces create an equals symbols.
   * @brief Checks if two main.java.utilities.traces.Trace create an equals symbol.
   */
  private boolean areEqualsSymbol (Trace trace1, Trace trace2) {
    TraceGroup traceGroup = new TraceGroup().add(trace1).add(trace2);

    double rate = classifier_.classify(traceGroup, null, false, false);
    int label = classifier_.getClassificationLabel();

    if(label == SymbolFactory.getLabelByType(Operator.Types.EQUALS) && rate > 0.50){
      return true;
    }
    else{
      return false;
    }
  }

  /**
   * @param path1 The first path.
   * @param path2 The second path.
   * @return Returns true if the two paths do not contain the same ink trace.
   * @brief Check if two paths upon the minimum spanning tree are combinable.
   * <p>
   * Two paths are combinable if they don't contain the same ink trace.
   */
  private boolean areCombinable (int[] path1, int[] path2) {
    int length1 = path1.length;
    int length2 = path2.length;

    for (int i = 0; i < length1; i++) {
      for (int j = 0; j < length2; j++) {
        if (path1[i] == path2[j]) {
          return false;
        }
      }
    }

    return true;
  }

  private int[] labels_; //!< The labels of the symbols that resulted from the partitioning.

  /**
   * @class PartitionCheck
   * @brief implements PathExtentionCheck to create a check whether a partition is eligible on not.
   * This check is used during the calculation of all possible partitions upon the remaining paths. It helps
   * reduce the number of possible partitions and thus make the partition faster.
   */
  private class PartitionCheck implements PathExtentionCheck {

    /**
     * @param paths          The paths upon the minimum spanning tree.
     * @param numberOfTraces The number of ink traces in the given equation.
     * @brief Constructor.
     */
    public PartitionCheck (int[][] paths, int numberOfTraces) {
      paths_ = paths;
      numberOfTraces_ = numberOfTraces;
    }

    /**
     * @param list The path that should be checked for validity.
     * @return Returns true if the path is valid.
     * @brief The check that will be performed to decide whether the given path is valid or not.
     */
    public boolean check (ArrayList<Integer> list) {
      int[] tracesOccurenceCounter = new int[numberOfTraces_];
      for (int i = 0; i < numberOfTraces_; i++) {
        tracesOccurenceCounter[i] = 0;
      }

      for (int path = 0; path < list.size(); path++) {
        for (int trace = 0; trace < paths_[list.get(path)].length; trace++) {
          tracesOccurenceCounter[paths_[list.get(path)][trace]]++;
        }
      }

      for (int i = 0; i < numberOfTraces_; i++) {
        if (tracesOccurenceCounter[i] > 1) {
          return false;
        }
      }

      return true;
    }

    private int[][] paths_; //!< The paths upon the minimum spanning tree.
    private int numberOfTraces_; //!< The number of ink traces in the given equation.
  }

  private TraceGroup expression_; //!< The given equation to be partitioned.

}
