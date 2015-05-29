package main.classifiers;

import main.utilities.TraceGroup;

public abstract interface Classifier{
  public abstract double classify(TraceGroup symbol, TraceGroup context);

  public abstract int getClassificationLabel();
}
