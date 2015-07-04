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
        positionOfArguments_ = new ArgumentPosition[] {ArgumentPosition.LEFT, ArgumentPosition.RIGHT};
        break;
      case FRACTION_LINE:
        positionOfArguments_ = new ArgumentPosition[] {ArgumentPosition.ABOVE, ArgumentPosition.BELOW};
        break;
    }

    traceGroup_ = traceGroup;
  }

  public enum Types{
    PLUS(ArgumentPosition.LEFT + "+" + ArgumentPosition.RIGHT),
    EQUALS(ArgumentPosition.LEFT + "=" + ArgumentPosition.RIGHT),
    MINUS(ArgumentPosition.LEFT + "-" + ArgumentPosition.RIGHT),
    FRACTION_LINE("\\frac{" + ArgumentPosition.ABOVE + "}{" + ArgumentPosition.BELOW + "}");

    private Types(String stringValue){
      stringValue_ = stringValue;
    }

    @Override
    public String toString(){
      return stringValue_;
    }

    private String stringValue_;
  }

  public Symbol reEvaluate(){
    return this;
  }

}
