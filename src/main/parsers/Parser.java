package main.parsers;

import main.utilities.traces.TraceGroup;

public abstract class Parser{

  public abstract void parse(TraceGroup[] traceGroups, int[] labels);

  public abstract String toString();

  public void setSilent(boolean silent){
    silent_ = silent;
  }

  public boolean isSilent(){
    return silent_;
  }

  protected boolean silent_ = true;

}
