package classifiers;

import utilities.TraceGroup;

/* TODO
 * If there is no need for variables inside Classifier class, then it must be
 * transformed to and abstract interface.
 */

public abstract class Classifier{
  /* Returns the possibility that the symbol tracegroup is indeed a symbol.*/
  public abstract double classify(TraceGroup symbol, TraceGroup context);
}
