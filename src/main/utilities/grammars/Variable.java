package main.utilities.grammars;

import java.util.List;
import java.util.ArrayList;

import main.utilities.grammars.Symbol.ArgumentPosition;
import main.utilities.traces.TraceGroup;

public class Variable extends Symbol{

  public Variable(Variable.Types type, TraceGroup traceGroup){
    super();

    type_ = type;

    arguments_ = new ArrayList<List<Symbol>>();
    arguments_.add(new ArrayList<Symbol>());
    arguments_.add(new ArrayList<Symbol>());
    arguments_.add(new ArrayList<Symbol>());
    arguments_.add(new ArrayList<Symbol>());

    positionOfArguments_ = new ArgumentPosition[] {ArgumentPosition.LEFT, ArgumentPosition.ABOVE_RIGHT, ArgumentPosition.BELOW_RIGHT, ArgumentPosition.RIGHT};

    traceGroup_ = traceGroup;
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

  public void reEvaluate(){}

}
