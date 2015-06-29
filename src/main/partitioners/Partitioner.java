package main.partitioners;

import main.classifiers.Classifier;
import main.utilities.traces.TraceGroup;

public abstract class Partitioner{

  public abstract TraceGroup[] partition(TraceGroup expression);

  protected Classifier classifier_;

  public static int MAX_TRACES_IN_SYMBOL = 3;

}
