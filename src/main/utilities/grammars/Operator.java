package main.utilities.grammars;

import java.util.List;
import java.util.ArrayList;

import main.utilities.grammars.Symbol;
import main.utilities.traces.TraceGroup;

public class Operator extends Symbol{

  public Operator(Operator.Types type, TraceGroup traceGroup){
    super(traceGroup);

    type_ = type;

    passiveArguments_ = new ArrayList<List<Symbol>>();

    switch(type){
      case EQUALS:
      case PLUS:
      case MINUS:
        positionOfPassiveArguments_ = new ArgumentPosition[] {ArgumentPosition.LEFT, ArgumentPosition.RIGHT};
        passiveArguments_.add(new ArrayList<Symbol>());
        passiveArguments_.add(new ArrayList<Symbol>());
        positionOfActiveArguments_ = new ArgumentPosition[] {ArgumentPosition.ABOVE, ArgumentPosition.BELOW};
        break;
      case FRACTION_LINE:
        positionOfPassiveArguments_ = new ArgumentPosition[] {ArgumentPosition.LEFT, ArgumentPosition.ABOVE, ArgumentPosition.BELOW, ArgumentPosition.RIGHT};
        passiveArguments_.add(new ArrayList<Symbol>());
        passiveArguments_.add(new ArrayList<Symbol>());
        passiveArguments_.add(new ArrayList<Symbol>());
        passiveArguments_.add(new ArrayList<Symbol>());
        positionOfActiveArguments_ = new ArgumentPosition[] {};
        break;
    }
  }

  public enum Types{
    PLUS(ArgumentPosition.LEFT + "+" + ArgumentPosition.RIGHT),
    EQUALS(ArgumentPosition.LEFT + "=" + ArgumentPosition.RIGHT),
    MINUS(ArgumentPosition.LEFT + "-" + ArgumentPosition.RIGHT),
    FRACTION_LINE(ArgumentPosition.LEFT + "\\frac{" + ArgumentPosition.ABOVE + "}{" + ArgumentPosition.BELOW + "}" + ArgumentPosition.RIGHT);

    private Types(String stringValue){
      stringValue_ = stringValue;
    }

    @Override
    public String toString(){
      return stringValue_;
    }

    private String stringValue_;
  }

}
