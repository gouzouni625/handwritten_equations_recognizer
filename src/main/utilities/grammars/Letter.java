package main.utilities.grammars;

import java.util.List;
import java.util.ArrayList;

import main.utilities.traces.TraceGroup;

public class Variable extends Symbol{

  public Variable(Variable.Types type, TraceGroup traceGroup){
    super(traceGroup);

    type_ = type;

    passiveArguments_ = new ArrayList<List<Symbol>>();
    passiveArguments_.add(new ArrayList<Symbol>());
    passiveArguments_.add(new ArrayList<Symbol>());
    passiveArguments_.add(new ArrayList<Symbol>());
    passiveArguments_.add(new ArrayList<Symbol>());

    positionOfPassiveArguments_ = new ArgumentPosition[] {ArgumentPosition.LEFT, ArgumentPosition.ABOVE_RIGHT, ArgumentPosition.BELOW_RIGHT, ArgumentPosition.RIGHT};

    positionOfActiveArguments_ = new ArgumentPosition[] {ArgumentPosition.ABOVE, ArgumentPosition.BELOW};
  }

  public enum Types{
    X(ArgumentPosition.LEFT + "x^{" + ArgumentPosition.ABOVE_RIGHT + "}_{" + ArgumentPosition.BELOW_RIGHT + "}" + ArgumentPosition.RIGHT),
    Y(ArgumentPosition.LEFT + "y^{" + ArgumentPosition.ABOVE_RIGHT + "}_{" + ArgumentPosition.BELOW_RIGHT + "}" + ArgumentPosition.RIGHT);

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
