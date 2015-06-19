package main.utilities.grammars;

import main.utilities.grammars.Symbol;
import main.utilities.traces.TraceGroup;

public class Operator extends Symbol{

  public Operator(Operator.Types type, TraceGroup traceGroup, boolean levelChanging){
    super();

    type_ = type;

    arguments_ = new Symbol[] {null, null};
    positionOfArguments_ = new Symbol.ArgumentPosition[] {Symbol.ArgumentPosition.LEFT, Symbol.ArgumentPosition.RIGHT};

    traceGroup_ = traceGroup;

    levelChanging_ = levelChanging;
  }

  public boolean isOperator(){
    return true;
  }

  public boolean isLevelChanging(){
    return levelChanging_;
  }

  private boolean levelChanging_;

  public enum Types{
    EQUALS("="),
    PLUS("+"),
    MINUS("-");

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
