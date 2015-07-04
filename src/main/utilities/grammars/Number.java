package main.utilities.grammars;

import main.utilities.grammars.Symbol;
import main.utilities.traces.TraceGroup;

public class Number extends Symbol{

  public Number(Number.Types type, TraceGroup traceGroup){
    super();

    type_ = type;

    arguments_ = new Symbol[] {null};
    positionOfArguments_ = new ArgumentPosition[] {ArgumentPosition.ABOVE_RIGHT};

    traceGroup_ = traceGroup;
  }

  public enum Types{
    ZERO("0^{" + ArgumentPosition.ABOVE_RIGHT + "}"),
    ONE("1^{" + ArgumentPosition.ABOVE_RIGHT + "}"),
    TWO("2^{" + ArgumentPosition.ABOVE_RIGHT + "}"),
    THREE("3^{" + ArgumentPosition.ABOVE_RIGHT + "}"),
    FOUR("4^{" + ArgumentPosition.ABOVE_RIGHT + "}"),
    FIVE("5^{" + ArgumentPosition.ABOVE_RIGHT + "}"),
    SIX("6^{" + ArgumentPosition.ABOVE_RIGHT + "}"),
    SEVEN("7^{" + ArgumentPosition.ABOVE_RIGHT + "}"),
    EIGHT("8^{" + ArgumentPosition.ABOVE_RIGHT + "}"),
    NINE("9^{" + ArgumentPosition.ABOVE_RIGHT + "}");

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
