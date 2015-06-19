package main.utilities.grammars;

import main.utilities.traces.TraceGroup;

public abstract class Symbol{

  public Symbol(){
    level_ = -1;
  }

  public abstract boolean isOperator();

  public abstract String toString();

  public static int getNewLevel(){
    currentLevel++;
    return currentLevel;
  }

  public static int getCurrentLevel(){
    return currentLevel;
  }

  public int level_;
  private static int currentLevel = -1;

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

  public TraceGroup traceGroup_;

}
