package main.classifiers;

import main.utilities.traces.TraceGroup;

public abstract class Classifier{
  public Classifier(int maxTracesInSymbol){
    maxTracesInSymbol_ = maxTracesInSymbol;
  }

  public abstract double classify(TraceGroup symbol, TraceGroup context, boolean subSymbolCheck, boolean subContextCheck);

  public abstract int getClassificationLabel();

  public static double MINIMUM_RATE = 0;
  public static double MAXIMUM_RATE = 100;

  protected int maxTracesInSymbol_;

}
