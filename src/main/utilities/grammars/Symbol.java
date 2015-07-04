package main.utilities.grammars;

import java.util.List;

import main.utilities.traces.TraceGroup;

public abstract class Symbol{

  public Symbol(){}

  public abstract void reEvaluate();

  public Enum<?> getType(){
    return type_;
  }

  public void setArgument(ArgumentPosition argumentPosition, Symbol symbol){
    for(int i = 0;i < positionOfArguments_.length;i++){
      if(positionOfArguments_[i] == argumentPosition){
        arguments_.get(i).add(symbol);
      }
    }
  }

  public String toString(){
    String stringValue = type_.toString();

    for(int argument = 0;argument < arguments_.length;argument++){
      if(arguments_[argument] != null && stringValue.contains(positionOfArguments_[argument].toString())){

        stringValue.replaceAll(positionOfArguments_[argument].toString(), arguments_[argument].toString());
      }
    }

    return stringValue;
  }

  protected Enum<?> type_;

  public List<List<Symbol>> arguments_;
  public ArgumentPosition[] positionOfArguments_;
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
