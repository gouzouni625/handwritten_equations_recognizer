package main.utilities.grammars;

import main.utilities.grammars.Symbol;
import main.utilities.traces.TraceGroup;

public class Number extends Symbol{

  public Number(Number.Types type, TraceGroup traceGroup){
    super();

    type_ = type;

    arguments_ = new Symbol[] {null, null};
    positionOfArguments_ = new Symbol.ArgumentPosition[] {Symbol.ArgumentPosition.ABOVE_RIGHT, Symbol.ArgumentPosition.BELOW_RIGHT};

    traceGroup_ = traceGroup;
  }

  public enum Types{
    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9");

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
