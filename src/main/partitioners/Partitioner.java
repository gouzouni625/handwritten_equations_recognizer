package partitioners;

import classifiers.Classifier;
import utilities.TraceGroup;

public abstract class Partitioner{
  public abstract TraceGroup[] partition(TraceGroup expression);
  
  public void setClassifier(Classifier classifier){
    classifier_ = classifier;
  }
  
  public Classifier getClassifier(){
    return classifier_;
  }
  
  protected Classifier classifier_;
}