package main.utilities.grammars;

import main.utilities.traces.TraceGroup;


public class Variable extends Symbol{

  public Variable(Variable.Types type, TraceGroup traceGroup){
    super();

    type_ = type;

    arguments_ = new Symbol[] {null, null};
    positionOfArguments_ = new ArgumentPosition[] {ArgumentPosition.ABOVE_RIGHT, ArgumentPosition.BELOW_RIGHT};

    traceGroup_ = traceGroup;
  }

  public enum Types{
    X("x^{" + ArgumentPosition.ABOVE_RIGHT + "}_{" + ArgumentPosition.BELOW_RIGHT + "}"),
    Y("y^{" + ArgumentPosition.ABOVE_RIGHT + "}_{" + ArgumentPosition.BELOW_RIGHT + "}");

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
