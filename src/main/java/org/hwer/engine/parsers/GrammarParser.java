package org.hwer.engine.parsers;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;

import org.hwer.engine.utilities.Utilities;
import org.hwer.engine.parsers.grammars.Grammar;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory;
import org.hwer.engine.utilities.traces.Point;
import org.hwer.engine.utilities.traces.Trace;
import org.hwer.engine.utilities.traces.TraceGroup;

/** @class GrammarParser
 *
 *  @brief Implements a Parser using a grammar to parse the given ink traces.
 */
public abstract class GrammarParser extends Parser{
  /**
   *  @brief Parses a given set of groups of ink traces along with its labels.
   *
   *  @param traceGroups An array with the main.java.utilities.traces.TraceGroup of ink traces.
   *  @param labels The labels of the traces.
   */
  public void parse(TraceGroup[] traceGroups, int[] labels){
    int numberOfTraceGroups = traceGroups.length;

    // Transform traceGroups to symbols.
    symbols_ = new Symbol[numberOfTraceGroups];
    int numberOfSymbols = numberOfTraceGroups;
    for(int i = 0;i < numberOfSymbols;i++){
      try{
        symbols_[i] = SymbolFactory.createByLabel(traceGroups[i], labels[i]);
      }
      catch (Exception exception){
        exception.printStackTrace();
      }
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

  /**
   *  @brief Parses again and again a group of main.java.utilities.symbols.Symbol until nothing changes.
   *
   *  @param symbols An array with the main.java.utilities.symbols.Symbol objects to parse.
   */
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

    // Preprocess symbols to find better symbol combinations.
    // The symbols are parsed in pairs. After being sorted by abscissa, each symbol will be parsed with its next. For
    // example, in \frac{2}{3}, the parsing pairs will be (\frac, 2) and (2, 3). A better parsing could be done if the
    // pairs where (\frac, 2) and (\frac, 3). In the later case, the relationships between the symbols are obvious. To
    // wrap up, the preprocessing done here will help solving the above problem. Concretely, if between two symbols that
    // form a pair, lies another symbol, then, two new pairs will be created one for each of the two symbols with the
    // one between, and the initial pair will be removed. For example, if the given pair is (2, 3), then this pair will
    // be removed and two new will takes its place. Those two will be (\frac, 2) and (\frac, 3) because the fraction line
    // is between 2 and 3.
    Hashtable<Integer, int[]> pathsTable = new Hashtable<Integer, int[]>();
    for(int i = 0;i < numberOfSymbols - 1;i++){
      int[][] paths = this.processPath(symbols, symbols[i], i, symbols[i + 1], i + 1);

      for(int path = 0;path < paths.length;path++){
        pathsTable.put(Utilities.pathHashKey(paths[path]), paths[path]);
      }
    }

    // Transform hash table to array.
    int numberOfPaths = pathsTable.size();
    int[][] paths = new int[numberOfPaths][2];
    Iterator<int[]> iterator = pathsTable.values().iterator();
    int index = 0;
    while(iterator.hasNext()){
      paths[index] = iterator.next();
      index++;
    }

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: paths... ===== Start =====");

      for(int i = 0;i < numberOfPaths;i++){
        System.out.println("Symbol path " + i + ": " + paths[i][0] + ", " + paths[i][1]);
      }

      System.out.println("Log: paths... ===== End =======");
    }
    /* ===== Logs ===== */

    // Sort paths by first symbol and then by second. Note that, since symbols are sorted by abscissa then paths will
    // also be sorted by abscissa if they are sorted by symbols.
    Arrays.sort(paths, new Comparator<int[]>(){
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

    // Check symbols one by one to find their position in the equation.
    // Find the relationship between the symbols in each pair.
    for(int i = 0;i < numberOfPaths;i++){
      grammar_.parse(symbols[paths[i][0]], symbols[paths[i][1]]);
    }

    // Reevaluate each unrecognized symbol to determine its type.
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

    // At this point, "first level" parsing is done. Now, parse the children of each symbols as if they where a new
    // equation. This makes it possible to have arbitrary depth in an equation. Concretely, equations like
    // x^{2^{3}^{4}^...^{N}} can be parsed.
    for(Symbol symbol : symbols){
      for(List<Symbol> samePositionChildrenList : symbol.children_){

        // Create an array of all the children on the same position.
        Symbol[] samePositionChildrenArray = new Symbol[samePositionChildrenList.size()];
        for(int i = 0;i < samePositionChildrenArray.length;i++){
          samePositionChildrenArray[i] = samePositionChildrenList.get(i);
        }

        // Parse the children in the same position.
        parse(samePositionChildrenArray);

        // The parsing may result in some symbols changing parents, so, remove all the children from the symbol and
        // enter the parsed ones.
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

  public void append(TraceGroup[] traceGroups, int[] labels){
    if(symbols_ == null || symbols_.length == 0){
      parse(traceGroups, labels);

      return;
    }

    int numberOfTraceGroups = traceGroups.length;
    int oldNumberOfSymbols = symbols_.length;
    int numberOfSymbols = numberOfTraceGroups + oldNumberOfSymbols;

    symbols_ = Arrays.copyOf(symbols_, numberOfSymbols);

    // Transform traceGroups to symbols.
    for(int i = 0;i < numberOfTraceGroups;i++){
        try{
            symbols_[i + oldNumberOfSymbols] = SymbolFactory.createByLabel(traceGroups[i], labels[i]);
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
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

  /**
   *  @brief Processes a pair of symbols to check if there is another symbol between them.
   *
   *  @param symbols All the symbols that are currently being processed.
   *  @param symbol1 The first symbol to be processed.
   *  @param index1 The position of the first symbol into symbols.
   *  @param symbol2 The second symbol to be processed.
   *  @param index2 The position of the second symbol into symbols.
   *
   *  @return Returns the paths that result from the preprocessing.
   */
  public int[][] processPath(Symbol[] symbols, Symbol symbol1, int index1, Symbol symbol2, int index2){
    Trace connectionLine = new Trace();
    Point[] closestPoints = TraceGroup.closestPoints(symbol1.traceGroup_, symbol2.traceGroup_);
    connectionLine.add(new Point(closestPoints[0]));
    connectionLine.add(new Point(closestPoints[1]));

    for(int i = 0;i < symbols.length;i++){
      if(i == index1 || i == index2){
        continue;
      }

      for(int j = 0;j < symbols[i].traceGroup_.size();j++){
        if(Trace.areOverlapped(connectionLine, symbols[i].traceGroup_.get(j))){
          int[] path1 = symbols[i].traceGroup_.getTopLeftCorner().x_ < symbol1.traceGroup_.getTopLeftCorner().x_ ?
                        new int[] {i, index1} : new int[] {index1, i};
          int[] path2 = symbols[i].traceGroup_.getTopLeftCorner().x_ < symbol2.traceGroup_.getTopLeftCorner().x_ ?
                        new int[] {i, index2} : new int[] {index2, i};
          return (new int[][] {path1, path2});
        }
      }
    }

    return (new int[][] {{index1, index2}});
  }

  /**
   *
   */
  public String toString(){
    if(symbols_ != null && symbols_.length > 0){
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

  /**
   *  @brief Getter method for the silent mode of the grammar used by this GrammarParser.
   *
   *  @return Returns true if the grammar is in silent mode.
   */

  public boolean isGrammarSilent(){
    return grammar_.isQuiet();
  }


  /**
   *  @brief Setter method for the silent mode of the grammar used by this GrammarParser.
   *
   *  @param silent The value for the silent mode of the grammar used by this GrammarParser.
   */
  public void setGrammarSilent(boolean silent){
    grammar_.setQuiet(silent);
  }

  public void reset(){
    symbols_ = null;
  }

  protected Grammar grammar_; //!< The main.java.utilities.grammars.Grammar to be used by this GrammarParser.

}
