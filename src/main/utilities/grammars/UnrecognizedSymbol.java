package main.utilities.grammars;

import main.utilities.traces.TraceGroup;

public class UnrecognizedSymbol extends Symbol{

  public UnrecognizedSymbol(UnrecognizedSymbol.Types type, TraceGroup traceGroup){
    super();

    type_ = type;

    switch(type){
      case HORIZONTAL_LINE:
        possibleSymbols_ = new Symbol[2];
        possibleSymbols_[0] = SymbolFactory.createByType(traceGroup, Operator.Types.MINUS);
        possibleSymbols_[1] = SymbolFactory.createByType(traceGroup, Operator.Types.FRACTION_LINE);
    }
  }

  public Symbol recognize(){
    return null;
  }

  public enum Types{
    HORIZONTAL_LINE;
  }

  /** Do not use this method. Instead use the one for possible Symbols. */
  public String toString(){
    return null;
  }

  public Types type_;

  Symbol[] possibleSymbols_;

}
