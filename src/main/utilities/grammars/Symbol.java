package main.utilities.grammars;

import java.util.Arrays;
import java.util.List;

import main.utilities.traces.TraceGroup;

public abstract class Symbol{

  public Symbol(TraceGroup traceGroup){
    traceGroup_ = traceGroup;
  }

  public void reEvaluate(){}

  public Enum<?> getType(){
    return type_;
  }

  public void setArgument(ArgumentPosition argumentPosition, Symbol symbol){
    int index = Arrays.asList(positionOfPassiveArguments_).indexOf(argumentPosition);

    if(index == -1){
      index = Arrays.asList(positionOfActiveArguments_).indexOf(argumentPosition);

      if(index == -1){
        switch(argumentPosition){
          case ABOVE:
            this.setArgument(Symbol.ArgumentPosition.ABOVE_RIGHT, symbol);
            break;
          case ABOVE_RIGHT:
          case BELOW_RIGHT:
            this.setArgument(Symbol.ArgumentPosition.RIGHT, symbol);
            break;
          case BELOW:
            this.setArgument(Symbol.ArgumentPosition.BELOW_RIGHT, symbol);
          case BELOW_LEFT:
          case ABOVE_LEFT:
            this.setArgument(Symbol.ArgumentPosition.RIGHT, symbol);
          case LEFT:
            this.setArgument(Symbol.ArgumentPosition.ABOVE_LEFT, symbol);
          case RIGHT:
            this.setArgument(Symbol.ArgumentPosition.ABOVE_RIGHT, symbol);
        }
      }
      else{
        activeArgument_ = symbol;
      }
    }
    else{
      passiveArguments_.get(index).add(symbol);
    }
  }

  /*public String toString(){
    String stringValue = type_.toString();

    for(int i = 0;i < positionOfArguments_.length;i++){
      String argumentValue = "";

      for(int j = 0;j < arguments_.get(i).size();j++){
        argumentValue += arguments_.get(i).get(j).toString();
      }

      stringValue = stringValue.replaceAll(positionOfArguments_[i].toString(), argumentValue);
    }

    return stringValue;
  }*/

  protected Enum<?> type_;

  public List<List<Symbol>> passiveArguments_;
  Symbol activeArgument_;
  public ArgumentPosition[] positionOfActiveArguments_;  // Symbols to which, this symbol, is an argument.
  public ArgumentPosition[] positionOfPassiveArguments_; // Arguments of this symbol.
  public enum ArgumentPosition{
    ABOVE,
    ABOVE_RIGHT,
    RIGHT,
    BELOW_RIGHT,
    BELOW,
    BELOW_LEFT,
    LEFT,
    ABOVE_LEFT;
    //INSIDE
  }

  public TraceGroup traceGroup_;

}
