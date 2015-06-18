package main.utilities.grammars;

import main.utilities.traces.TraceGroup;

public abstract class Symbol{

  public abstract boolean isOperator();

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
    ARGUMENT_ABOVE,
    ARGUMENT_ABOVE_RIGHT,
    ARGUMENT_RIGHT,
    ARGUMENT_DOWN_RIGHT,
    ARGUMENT_DOWN,
    ARGUMENT_DOWN_LEFT,
    ARGUMENT_LEFT,
    ARGUMENT_ABOVE_LEFT
  }

  public TraceGroup traceGroup_;

}
