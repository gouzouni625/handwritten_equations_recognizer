package main.partitioners;

import main.classifiers.Classifier;
import main.utilities.TraceGroup;

public abstract class Partitioner{

  public abstract TraceGroup[] partition(TraceGroup expression);

  protected Classifier classifier_;

}
