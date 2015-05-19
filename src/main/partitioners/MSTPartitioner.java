package partitioners;

import java.util.Hashtable;

import utilities.TraceGroup;
import utilities.Trace;

/* MST = Minimum Spanning Tree.*/
public abstract class MSTPartitioner extends Partitioner{
  
  public TraceGroup[] partition(TraceGroup expression){
    int numberOfTraces = expression.size();
    
    // Calculate the distance between all the traces. For n traces, all the
    // possible distances are ((n * (n - 1)) / 2). All distances are stored
    // in a vector of size ((n * (n - 1)) / 2). The first n-1 distances
    // involve trace1, the next n-2 distances involve trace2, etc...
    //
    // distancesBetweenTraces = [9, 9.2, 4, 5.5, ...] of size
    // ((n * (n - 1)) / 2).
    double[] distances = new double[numberOfTraces * (numberOfTraces - 1) / 2];
    for(int i = 0;i < numberOfTraces;i++){
      for(int j = i + 1;j < numberOfTraces;j++){
        distances[i * (numberOfTraces - 1) + j] =
                         distanceOfTraces(expression.get(i), expression.get(j));
      }
    }
    
    // The distances calculated, form a connected, undirected graph. In order to
    // create a minimum spanning tree, only some of these distances must be
    // kept. Using a proper algorithm, find a minimum spanning tree(there are
    // minimum spanning trees in each connected, undirected graph). The minimum
    // spanning tree is a vector of ((n * (n - 1)) / 2) booleans, where n is the
    // number of traces. If minimumSpanningTree[index] = true, this means that
    // the corresponding connection is a part of the minimum spanning tree.
    //
    // minimumSpanningTree = [false, true, false, false, ...] of size
    // ((n * (n - 1)) / 2). True, means that the corresponding edge(see
    // distancesBetweenTraces) is included in the minimum spanning tree.
    boolean[] minimumSpanningTree = kruskalAlgorithm(distances,
                                                                numberOfTraces);
    
    // Make distancesBetweenTraces eligible for garbage collection.
    distances = null;
    
    // Transform the vector describing the minimum spanning tree to a nxn matrix
    // where n is the number of traces. If connections[i][j] is true, then
    // trace i and trace j are connected inside the minimum spanning tree.
    boolean[][] connections = new boolean[numberOfTraces][numberOfTraces];
    for(int i = 0;i < numberOfTraces;i++){
      for(int j = 0;j < numberOfTraces;j++){
        connections[i][j] = (i == j);
      }
    }
    
    for(int trace = 0;trace < numberOfTraces - 1;trace++){
      // Minimum Spanning Tree index.
      int mSTIndex = 0;
      for(int i = 0;i < trace;i++){
        mSTIndex += numberOfTraces - i - 1;
      }
      
      for(int j = 0;j < numberOfTraces - trace - 1;j++){
        connections[trace][trace + 1 + j] = minimumSpanningTree[j + mSTIndex];
        connections[trace + 1 + j][trace] = minimumSpanningTree[j + mSTIndex];
      }
    }
    
    // Make minimumSpanningTree eligible for garbage collection.
    minimumSpanningTree = null;
    
    // Feed all the possible combinations of arbitrary number of connected traces
    // to the classifier and see if they make a symbol.
    Hashtable<Integer, Double> traceCombinationRates =
                                               new Hashtable<Integer, Double>();
    
    // For each possible size of group, in terms of traces.
    for(int size = 1;size < numberOfTraces;size++){
      
      for(int primaryTrace = 0;primaryTrace < numberOfTraces;primaryTrace++){
        boolean[][] connectionsClone = connections.clone();
        
        int slotsLeft = size;
        do{
          
        }
        
        TraceGroup possibleSymbol = new TraceGroup();
        TraceGroup context = new TraceGroup();
        
        possibleSymbol.addTrace(expression.get(primaryTrace));
        int slotsLeft = size - 1;

        
      }
    }
    
    
  }
  
  private double distanceOfTraces(Trace trace1, Trace trace2){
    
  }
  
  private int traceHashKey(TraceGroup expression, Trace trace){
    int traceId = expression.indexOf(trace);
    return (int)(Math.pow(2, traceId));
  }
  
  private int traceGroupHashKey(TraceGroup expression, TraceGroup traceGroup){
    int traceGroupHashKey = 0;
    for(int i = 0;i < traceGroup.size();i++){
      traceGroupHashKey ^= traceHashKey(expression, traceGroup.get(i));
    }
    
    return traceGroupHashKey;
  }
}