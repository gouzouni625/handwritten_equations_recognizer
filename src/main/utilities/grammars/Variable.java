package main.utilities.grammars;

import main.utilities.traces.TraceGroup;


public class Variable extends Symbol{

  public Variable(Variable.Types type, TraceGroup traceGroup){
    super();

    type_ = type;

    arguments_ = new Symbol[] {null, null};
    positionOfArguments_ = new Symbol.ArgumentPosition[] {Symbol.ArgumentPosition.ABOVE_RIGHT, Symbol.ArgumentPosition.BELOW_RIGHT};

    traceGroup_ = traceGroup;
  }

  public enum Types{
    X("x^{?}_{?}"),
    Y("y^{?}_{?}");

    private Types(String stringValue){
      stringValue_ = stringValue;
    }

    public String stringValue_;
  }

  public String toString(){
    return this.type_.stringValue_;
  }

  public Symbol reEvaluate(){
    return this;
  }

  public Types type_;

}
