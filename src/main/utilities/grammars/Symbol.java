package main.utilities.grammars;

import main.utilities.traces.TraceGroup;

public abstract class Symbol{

  public Symbol(){
    level_ = -1;
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

  public static int newLevel(){
    CURRENT_LEVEL++;
    return CURRENT_LEVEL;
  }

  public static int currentLevel(){
    return CURRENT_LEVEL;
  }

  public int getLevel(){
    return level_;
  }

  public void setLevel(int level){
    level_ = level;
  }

  protected int level_;
  private static int CURRENT_LEVEL = -1;

  public Symbol[] arguments_;

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
  }

  // returns the level for the other symbol. Only left and right doesn't change level.
  public boolean setArgument(Symbol.ArgumentPosition argumentPosition, Symbol symbol){
    for(int i = 0;i < positionOfArguments_.length;i++){
      if(positionOfArguments_[i] == argumentPosition){
        arguments_[i] = symbol;
      }
    }

    // Check if a new level is required.
    if(argumentPosition == Symbol.ArgumentPosition.LEFT || argumentPosition == Symbol.ArgumentPosition.RIGHT){
      return false;
    }
    else{
      return true;
    }
  }

  public abstract Symbol reEvaluate();

  public Enum<?> getType(){
    return type_;
  }

  public TraceGroup traceGroup_;

  protected Enum<?> type_;

}
