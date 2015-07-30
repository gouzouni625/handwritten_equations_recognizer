package main.parsers;

import java.util.List;
import java.util.Arrays;
import java.util.Comparator;

import main.utilities.grammars.Grammar;
import main.utilities.grammars.Symbol;
import main.utilities.grammars.SymbolFactory;
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

    // Check symbols one by one to find their position in the equation.
    // Find the relationship between the symbols in each pair.
    for(int i = 0;i < numberOfSymbols - 1;i++){
      grammar_.parse(symbols[i], symbols[i + 1]);
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
      for(int i = 0;i < numberOfSymbols - 1;i++){
        grammar_.parse(symbols[i], symbols[i + 1]);
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

  public boolean isGrammarSilent(){
    return grammar_.isSilent();
  }

  public void setGrammarSilent(boolean silent){
    grammar_.setSilent(silent);
  }

  Symbol[] symbols_;

  protected Grammar grammar_;

}
