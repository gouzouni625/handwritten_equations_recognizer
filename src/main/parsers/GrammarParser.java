package main.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import main.utilities.Utilities;
import main.utilities.grammars.Grammar;
import main.utilities.grammars.Symbol;
import main.utilities.grammars.SymbolFactory;
import main.utilities.grammars.UnrecognizedSymbol;
import main.utilities.math.MinimumSpanningTree;
import main.utilities.traces.Point;
import main.utilities.traces.TraceGroup;

public abstract class GrammarParser extends Parser{

  public void parse(TraceGroup[] traceGroups, int[] labels){
    int numberOfTraceGroups = traceGroups.length;

    // Transform traceGroups to symbols.
    symbols_ = new Symbol[numberOfTraceGroups];
    int numberOfSymbols = numberOfTraceGroups;
    for(int i = 0;i < numberOfSymbols;i++){
      symbols_[i] = SymbolFactory.createByLabel(traceGroups[i], labels[i]);
    }

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: symbols... ===== Start =====");

      for(int i = 0;i < numberOfSymbols;i++){
        System.out.println("Symbol " + i + ": " + symbols_[i]);
      }

      System.out.println("Log: symbols... ===== End =======");
    }
    /* ===== Logs ===== */

    if(numberOfTraceGroups == 1){
      return;
    }

    // Sort symbols by abscissa.
    for(int i = 0;i < numberOfTraceGroups;i++){
      symbols_[i].traceGroup_.calculateCorners();
    }

    Arrays.sort(symbols_, new Comparator<Symbol>(){
      @Override
      public int compare(Symbol symbol1, Symbol symbol2){
        double x1 = symbol1.traceGroup_.getTopLeftCorner().x_;
        double x2 = symbol2.traceGroup_.getTopLeftCorner().x_;

        if(x1 > x2){
          return 1;
        }
        else if(x1 == x2){
          return 0;
        }
        else{
          return -1;
        }
      }
    });

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: symbols sorted by abscissa... ===== Start =====");

      for(int i = 0;i < numberOfSymbols;i++){
        System.out.println("Symbol " + i + ": " + symbols_[i]);
      }

      System.out.println("Log: symbols sorted by abscissa... ===== End =======");
    }
    /* ===== Logs ===== */

    // Calculate the distances between all the symbols.
    double[] distances = this.calculateDistancesBetweenSymbols(traceGroups);

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: distances between symbols... ===== Start =====");

      for(int i = 0;i < distances.length;i++){
        System.out.println("distance " + i + ": " + distances[i]);
      }

      System.out.println("Log: distances between symbols... ===== End =======");
    }
    /* ===== Logs ===== */

    // Create a minimum spanning tree using the distances between the symbols.
    MinimumSpanningTree minimumSpanningTree = MinimumSpanningTree.kruskal(distances, numberOfTraceGroups);

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: minimum spanning tree... ===== Start =====");

      for(int i = 0;i < numberOfTraceGroups;i++){
        for(int j = 0;j < numberOfTraceGroups;j++){
          System.out.print(minimumSpanningTree.areConnected(i, j) + ", ");
        }

        System.out.println();
      }

      System.out.println("Log: minimum spanning tree... ===== End =======");
    }
    /* ===== Logs ===== */

    // Find all the pairs of symbols connected on the minimum spanning tree.
    int[][] paths = minimumSpanningTree.getUniquePaths(2);
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

    // Remove paths with only one symbol.
    ArrayList<Integer> singleSymbolPaths = new ArrayList<Integer>();
    for(int i = 0;i < numberOfPaths;i++){
      if(paths[i].length == 1){
        singleSymbolPaths.add(i);
      }
    }

    paths = Utilities.removeRows(paths, singleSymbolPaths);
    numberOfPaths = paths.length;

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: paths after removing single symbols... ===== Start =====");

      for(int i = 0;i < numberOfPaths;i++){
        System.out.print("path " + i + " = ");
        for(int j = 0;j < paths[i].length;j++){
          System.out.print(paths[i][j] + ", ");
        }

       System.out.println();
      }

      System.out.println("Log: paths after removing single symbols... ===== End =======");
    }
    /* ===== Logs ===== */

    // Sort paths by first symbol and then by second. Note that, since symbols are sorted by abscissa then paths will
    // also be sorted by abscissa if they are sorted by symbols.
    Arrays.sort(paths, new Comparator<int[]>(){
      @Override
      public int compare(int[] path1, int[] path2){
        if(path1[0] > path2[0]){
          return 1;
        }
        else if(path1[0] == path2[0]){
          if(path1[1] > path2[1]){
            return 1;
          }
          else if(path1[1] == path2[1]){
            return 0;
          }
          else{
            return -1;
          }
        }
        else{
          return -1;
        }
      }
    });

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: paths after sorting them... ===== Start =====");

      for(int i = 0;i < numberOfPaths;i++){
        System.out.print("path " + i + " = ");
        for(int j = 0;j < paths[i].length;j++){
          System.out.print(paths[i][j] + ", ");
        }

       System.out.println();
      }

      System.out.println("Log: paths after sorting them... ===== End =======");
    }
    /* ===== Logs ===== */

    // Initialize the first level.
    symbols_[0].level_ = Symbol.newLevel();

    // Find the relationship between the symbols in each pair.
    for(int i = 0;i < numberOfPaths;i++){
      grammar_.parse(symbols_[paths[i][0]], symbols_[paths[i][1]]);
    }

    // Check if the symbols were correctly recognized.
    for(int i = 0;i < numberOfSymbols;i++){
      symbols_[i] = symbols_[i].reEvaluate();
    }

  }

  public String toString(){
    String expression = "";

    int i = 0;
    int offset;
    while(i < symbols_.length){
      offset = 0;

      expression+= symbols_[i].toString();

      if(symbols_[i].arguments_[0] != null){
        expression += "^{" + symbols_[i].arguments_[0].toString() + "}";
        offset++;
      }
      if(symbols_[i].arguments_[1] != null){
        expression += "_{" + symbols_[i].arguments_[1].toString() + "}";
        offset++;
      }

      i += offset + 1;
    }

    return expression;
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

  public boolean isGrammarSilent(){
    return grammar_.isSilent();
  }

  public void setGrammarSilent(boolean silent){
    grammar_.setSilent(silent);
  }

  Symbol[] symbols_;

  protected Grammar grammar_;

}
