package org.hwer.engine.symbols.operators;

import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.symbols.SymbolFactory.SymbolClass;
import org.hwer.engine.utilities.traces.TraceGroup;


/** @class Operator
 *
 *  @brief Implements mathematical operators as Symbol objects.
 */
public abstract class Operator extends Symbol implements SymbolClass{
  /**
   *  @brief Constructor.
   *
   *  @param traceGroup The TraceGroup of this Operator.
   */
  public Operator(TraceGroup traceGroup){
    super(traceGroup);
  }

  public Classes getClazz(){
    return Classes.OPERATOR;
  }

  public String toString(String symbolString){
    return symbolString;
  }

  @Override
  public void reEvaluate(boolean force){}

  @Override
  public void reset(){
    setParent(null);
    setPreviousSymbol(null);
    setNextSymbol(null);
  }

}
