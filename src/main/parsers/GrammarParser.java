package main.parsers;

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

    // Check symbols one by one to find their position in the equation.
    for(int i = 0;i < symbols_.length - 1;i++){
      grammar_.parse(symbols_[i], symbols_[i + 1]);
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
