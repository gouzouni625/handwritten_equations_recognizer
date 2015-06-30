package main.utilities.grammars;

import main.utilities.grammars.Symbol;
import main.utilities.traces.TraceGroup;

public class Operator extends Symbol{

  public Operator(Operator.Types type, TraceGroup traceGroup){
    super();

    type_ = type;

    arguments_ = new Symbol[] {null, null};

    switch(type){
      case EQUALS:
      case PLUS:
      case MINUS:
        positionOfArguments_ = new Symbol.ArgumentPosition[] {Symbol.ArgumentPosition.LEFT, Symbol.ArgumentPosition.RIGHT};
      case FRACTION_LINE:
        positionOfArguments_ = new Symbol.ArgumentPosition[] {Symbol.ArgumentPosition.ABOVE, Symbol.ArgumentPosition.BELOW};
    }

    traceGroup_ = traceGroup;
  }

  public enum Types{
    PLUS("+"),
    EQUALS("="),
    MINUS("-"),
    FRACTION_LINE("\\frac{?}{?}");

    private Types(String stringValue){
      stringValue_ = stringValue;
    }

    public String stringValue_;
  }

  public String toString(){
    return this.type_.stringValue_;
  }

  public Types type_;

}
