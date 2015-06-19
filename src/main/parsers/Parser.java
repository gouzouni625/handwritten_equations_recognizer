package main.parsers;

import main.utilities.grammars.GeometricalGrammar;
import main.utilities.grammars.Symbol;
import main.utilities.grammars.SymbolFactory;
import main.utilities.math.MinimumSpanningTree;
import main.utilities.traces.Point;
import main.utilities.traces.TraceGroup;

public class Parser{

  public void parse(TraceGroup[] traceGroups, int[] labels){
    int numberOfTraceGroups = traceGroups.length;

    if(numberOfTraceGroups == 1){
      // TODO
      // Return the symbol to string.
    }

    // Calculate the distances between all the symbols.
    double[] distances = this.calculateDistancesBetweenSymbols(traceGroups);

    boolean[][] connections = new boolean[numberOfTraceGroups][numberOfTraceGroups];
    for(int i = 0;i < numberOfTraceGroups;i++){
      for(int j = 0;j < numberOfTraceGroups;j++){
        connections[i][j] = (i == j);
      }
    }

    // Create a minimum spanning tree using the distances between the symbols.
    MinimumSpanningTree minimumSpanningTree = MinimumSpanningTree.kruskal(distances, numberOfTraceGroups);

    // Find all the pairs of symbols connected on the minimum spanning tree.
    int[][] paths = minimumSpanningTree.getUniquePaths(2);
    int numberOfPaths = paths.length;

    // Transform traceGroups to symbols.
    symbols_ = new Symbol[numberOfTraceGroups];
    int numberOfSymbols = numberOfTraceGroups;
    for(int i = 0;i < numberOfSymbols;i++){
      symbols_[i] = SymbolFactory.create(traceGroups[i], labels[i]);
    }

    // Find the relationship between the symbols in each pair.
    for(int i = 0;i < numberOfPaths;i++){
      GeometricalGrammar.parse(symbols_[paths[i][0]], symbols_[paths[i][1]]);
    }
  }

  private double[] calculateDistancesBetweenSymbols(TraceGroup[] symbols){
    int numberOfSymbols = symbols.length;

    double[] distances = new double[numberOfSymbols * (numberOfSymbols - 1) / 2];
    int index = 0;
    for(int i = 0;i < numberOfSymbols;i++){
      for(int j = i + 1;j < numberOfSymbols;j++){
        distances[index] = this.distanceOfSymbols(symbols[i], symbols[j]);
        index++;
      }
    }

    return distances;
  }

  private double distanceOfSymbols(TraceGroup symbol1, TraceGroup symbol2){
    if(symbol1.size() == 0 && symbol2.size() == 0){
      return -1;
    }

    Point centroid1 = symbol1.getCentroid();
    Point centroid2 = symbol2.getCentroid();

    return (Point.distance(centroid1, centroid2));
  }

  public String toString(){
    int[] sortedIndices = this.sort();

    String expression = "";

    int i = 0;
    int offset;
    while(i < symbols_.length){
      offset = 0;

      expression+= symbols_[sortedIndices[i]].toString();

      if(symbols_[i].arguments_[0] != null){
        expression += "^{" + symbols_[sortedIndices[i]].arguments_[0].toString() + "}";
        offset++;
      }
      if(symbols_[i].arguments_[1] != null){
        expression += "_{" + symbols_[sortedIndices[i]].arguments_[1].toString() + "}";
        offset++;
      }

      i += offset + 1;
    }

    return expression;
  }

  public int[] sort(){
    int length = symbols_.length;

    int[] indices = new int[length];
    for(int i = 0;i < length;i++){
      indices[i] = i;
    }

    for(int i = 0;i < length;i++){
      for(int j = 1;j < length - i;j++){
        if(symbols_[indices[j - 1]].traceGroup_.getTopLeftCorner().x_ > symbols_[indices[j]].traceGroup_.getTopLeftCorner().x_){
          int temp = indices[j - 1];
          indices[j - 1] = indices[j];
          indices[j] = temp;
        }
      }
    }

    return indices;
  }

  Symbol[] symbols_;
}
