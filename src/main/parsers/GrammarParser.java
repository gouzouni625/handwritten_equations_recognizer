package main.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;

import main.utilities.Utilities;
import main.utilities.grammars.Grammar;
import main.utilities.grammars.Symbol;
import main.utilities.grammars.SymbolFactory;
import main.utilities.grammars.UnrecognizedSymbol;
import main.utilities.math.MinimumSpanningTree;
import main.utilities.traces.Point;
import main.utilities.traces.Trace;
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

    if(numberOfSymbols <= 1){
      return;
    }

    parse(symbols_);
  }

  public void parse(Symbol[] symbols){
    int numberOfSymbols = symbols.length;

    if(symbols.length <= 1){
      return;
    }

    // Sort symbols by abscissa.
    for(int i = 0;i < numberOfSymbols;i++){
      symbols[i].traceGroup_.calculateCorners();
    }

    Arrays.sort(symbols, new Comparator<Symbol>(){
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
        System.out.println("Symbol " + i + ": " + symbols[i]);
      }

      System.out.println("Log: symbols sorted by abscissa... ===== End =======");
    }
    /* ===== Logs ===== */

    // Calculate the distances between all the symbols.
    double[] distances = this.calculateDistancesBetweenSymbols(symbols);

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
    MinimumSpanningTree minimumSpanningTree = MinimumSpanningTree.kruskal(distances, numberOfSymbols);

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: minimum spanning tree... ===== Start =====");

      for(int i = 0;i < numberOfSymbols;i++){
        for(int j = 0;j < numberOfSymbols;j++){
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

    // Sort symbols in paths(e.g. path 4, 3 should be 3, 4).
    for(int i = 0;i < numberOfPaths;i++){
      if(paths[i][0] > paths[i][1]){
        int temp = paths[i][0];
        paths[i][0] = paths[i][1];
        paths[i][1] = temp;
      }
    }

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: paths after sorting symbols inside them... ===== Start =====");

      for(int i = 0;i < numberOfPaths;i++){
        System.out.print("path " + i + " = ");
        for(int j = 0;j < paths[i].length;j++){
          System.out.print(paths[i][j] + ", ");
        }

       System.out.println();
      }

      System.out.println("Log: paths after sorting symbols inside them... ===== End =======");
    }
    /* ===== Logs ===== */

    // Check symbols one by one to find their position in the equation.
    // Find the relationship between the symbols in each pair.
    for(int i = 0;i < numberOfPaths;i++){
      grammar_.parse(symbols[paths[i][0]], symbols[paths[i][1]]);
    }

    for(Symbol symbol : symbols){
      if(symbol.symbolClass_ == Symbol.SymbolClass.UNRECOGNIZED){
        symbol.reEvaluate();
      }
    }

    // Parse the equation again and again until there is no change. This is done to propagate
    // the properties of each symbol to every other.
    String previousState;
    do{
      previousState = this.toString();
      for(int i = 0;i < numberOfPaths;i++){
        grammar_.parse(symbols[paths[i][0]], symbols[paths[i][1]]);
      }
    }while(!previousState.equals(this.toString()));

    for(Symbol symbol : symbols){
      for(List<Symbol> samePositionChildrenList : symbol.children_){
        Symbol[] samePositionChildrenArray = new Symbol[samePositionChildrenList.size()];
        for(int i = 0;i < samePositionChildrenArray.length;i++){
          samePositionChildrenArray[i] = samePositionChildrenList.get(i);
        }

        parse(samePositionChildrenArray);

        samePositionChildrenList.clear();
        for(int i = 0;i < samePositionChildrenArray.length;i++){
          // If symbol is still the parent of samePositionChildrenArray[i], then add it.
          // If not, it will be some other symbol in samePositionChildrenArray so it should
          // not be added to symbol's children.
          if(samePositionChildrenArray[i].parent_ == symbol){
            samePositionChildrenList.add(samePositionChildrenArray[i]);
          }
        }
      }
    }
  }

  public String toString(){
    if(symbols_.length > 0){
      String equation = symbols_[0].toString();

      for(int i = 0;i < symbols_.length - 1;i++){
        if(symbols_[i].nextSymbol_ != null){
          // Print the next symbol only if it has no parent. If there is
          // a parent for this symbol, then, it should be print through
          // the parent.
          if(symbols_[i].nextSymbol_.parent_ == null){
            equation += symbols_[i].nextSymbol_.toString();
          }
        }
      }

      return equation;
    }
    else{
      return "";
    }
  }

  private double[] calculateDistancesBetweenSymbols(Symbol[] symbols){
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

  private double distanceOfSymbols(Symbol symbol1, Symbol symbol2){
    TraceGroup traceGroup1 = symbol1.traceGroup_;
    TraceGroup traceGroup2 = symbol2.traceGroup_;

    if(traceGroup1.size() == 0 || traceGroup2.size() == 0){
      return -1;
    }

    int size1 = traceGroup1.size();
    int size2 = traceGroup2.size();

    double minDistance = Trace.minimumDistance(traceGroup1.get(0), traceGroup2.get(0));

    for(int i = 0;i < size1;i++){
      for(int j = 0;j < size2;j++){
        double distance = Trace.minimumDistance(traceGroup1.get(i), traceGroup2.get(j));

        if(distance < minDistance){
          minDistance = distance;
        }
      }
    }

    Point centerOfMass1 = traceGroup1.getCenterOfMass();
    Point centerOfMass2 = traceGroup2.getCenterOfMass();
    double distanceOfCenterOfMass = Point.distance(centerOfMass1, centerOfMass2);

    return (0.5 * minDistance + 0.5 * distanceOfCenterOfMass);
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
