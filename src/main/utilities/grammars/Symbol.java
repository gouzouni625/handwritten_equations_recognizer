package main.utilities.grammars;

import main.utilities.traces.TraceGroup;

public abstract class Symbol{

  public Symbol(){
    level_ = -1;
  }

  public abstract String toString();

  public static int getNewLevel(){
    CURRENT_LEVEL++;
    return CURRENT_LEVEL;
  }

  public static int getCurrentLevel(){
    return CURRENT_LEVEL;
  }

  public int level_;
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

  public abstract Symbol reEvaluate();

  public TraceGroup traceGroup_;

}
