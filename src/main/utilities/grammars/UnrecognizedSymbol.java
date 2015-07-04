package main.utilities.grammars;

import main.utilities.traces.TraceGroup;

public class UnrecognizedSymbol extends Symbol{

  public UnrecognizedSymbol(UnrecognizedSymbol.Types type, TraceGroup traceGroup){
    super();

    type_ = type;

    chosenSymbol_ = null;

    switch(type){
      case HORIZONTAL_LINE:
        possibleSymbols_ = new Symbol[2];
        possibleSymbols_[0] = SymbolFactory.createByType(Operator.Types.MINUS, traceGroup);
        possibleSymbols_[1] = SymbolFactory.createByType(Operator.Types.FRACTION_LINE, traceGroup);
        break;
    }

    traceGroup_ = traceGroup;
  }

  public enum Types{
    HORIZONTAL_LINE;
  }

  public String toString(){
    if(chosenSymbol_ != null){
      return (chosenSymbol_.toString());
    }
    else{
      return "";
    }
  }

  @Override
  public void setArgument(Symbol.ArgumentPosition argumentPosition, Symbol symbol){
    for(int i = 0;i < possibleSymbols_.length;i++){
      for(int j = 0;j < possibleSymbols_[i].positionOfArguments_.length;j++){
        if(possibleSymbols_[i].positionOfArguments_[j] == argumentPosition){

          possibleSymbols_[i].arguments_.get(j).add(symbol);
        }
      }
    }
  }

  public void reEvaluate(){
    // Find the symbol of the possible symbols that has the most arguments filled.
    int[] foundArguments = new int[possibleSymbols_.length];
    for(int i = 0;i < foundArguments.length;i++){
      foundArguments[i] = 0;
    }

    for(int i = 0;i < possibleSymbols_.length;i++){
      for(int j = 0;j < possibleSymbols_[i].arguments_.size();j++){
        if(possibleSymbols_[i].arguments_.get(j).size() != 0){
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

    chosenSymbol_ = possibleSymbols_[maxIndex];
  }

  public Types type_;

  Symbol[] possibleSymbols_;
  Symbol chosenSymbol_;

}
