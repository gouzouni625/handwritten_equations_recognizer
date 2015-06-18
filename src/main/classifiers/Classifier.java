package main.classifiers;

import main.utilities.traces.TraceGroup;

public abstract interface Classifier{

  public abstract double classify(TraceGroup symbol, TraceGroup context);

  public abstract int getClassificationLabel();

}
