package main.utilities.grammars;

import main.utilities.traces.TraceGroup;

public class UnrecognizedSymbol extends Symbol{

  public UnrecognizedSymbol(UnrecognizedSymbol.Types type, TraceGroup traceGroup){
    super();

    type_ = type;

    switch(type){
      case HORIZONTAL_LINE:
        possibleSymbols_ = new Symbol[2];
        possibleSymbols_[0] = SymbolFactory.createByType(Operator.Types.MINUS, traceGroup);
        possibleSymbols_[1] = SymbolFactory.createByType(Operator.Types.FRACTION_LINE, traceGroup);
    }

    traceGroup_ = traceGroup;
  }

  public enum Types{
    HORIZONTAL_LINE;
  }

  /** Do not use this method. Instead use the one for possible Symbols. */
  public String toString(){
    return "";
  }

  @Override
  public int setArgument(Symbol.ArgumentPosition argumentPosition, Symbol symbol){
    for(int i = 0;i < possibleSymbols_.length;i++){
      for(int j = 0;j < possibleSymbols_[i].positionOfArguments_.length;j++){
        if(possibleSymbols_[i].positionOfArguments_[j] == argumentPosition){
          possibleSymbols_[i].arguments_[j] = symbol;
          possibleSymbols_[i].positionOfArguments_[j] = null;
        }
      }
    }

    if(argumentPosition == Symbol.ArgumentPosition.LEFT || argumentPosition == Symbol.ArgumentPosition.RIGHT){
      return level_;
    }
    else{
      return Symbol.newLevel();
    }
  }

  public Symbol reEvaluate(){
    // Find the symbol of the possible symbols that has the most arguments filled.
    int[] foundArguments = new int[possibleSymbols_.length];
    for(int i = 0;i < foundArguments.length;i++){
      foundArguments[i] = 0;
    }

    for(int i = 0;i < possibleSymbols_.length;i++){
      for(int j = 0;j < possibleSymbols_[i].arguments_.length;j++){
        if(possibleSymbols_[i].arguments_[j] != null){
          foundArguments[i]++;
        }
      }
    }

    int max = foundArguments[0];
    int maxIndex = 0;
    for(int i = 1;i < foundArguments.length;i++){
      if(foundArguments[i] > max){
        max = foundArguments[i];
        maxIndex = i;
      }
    }

    return (possibleSymbols_[maxIndex]);
  }

  public Types type_;

  Symbol[] possibleSymbols_;

}
