package main.utilities.grammars;

import java.util.List;
import java.util.ArrayList;

import main.utilities.grammars.Symbol;
import main.utilities.traces.TraceGroup;

public class Number extends Symbol{

  public Number(Number.Types type, TraceGroup traceGroup){
    super();

    type_ = type;

    arguments_ = new ArrayList<List<Symbol>>();
    arguments_.add(new ArrayList<Symbol>());
    arguments_.add(new ArrayList<Symbol>());
    arguments_.add(new ArrayList<Symbol>());

    positionOfArguments_ = new ArgumentPosition[] {ArgumentPosition.LEFT, ArgumentPosition.ABOVE_RIGHT, ArgumentPosition.RIGHT};

    traceGroup_ = traceGroup;
  }

  public enum Types{
    ZERO(ArgumentPosition.LEFT + "0^{" + ArgumentPosition.ABOVE_RIGHT + "}" + ArgumentPosition.RIGHT),
    ONE(ArgumentPosition.LEFT + "1^{" + ArgumentPosition.ABOVE_RIGHT + "}" + ArgumentPosition.RIGHT),
    TWO(ArgumentPosition.LEFT + "2^{" + ArgumentPosition.ABOVE_RIGHT + "}" + ArgumentPosition.RIGHT),
    THREE(ArgumentPosition.LEFT + "3^{" + ArgumentPosition.ABOVE_RIGHT + "}" + ArgumentPosition.RIGHT),
    FOUR(ArgumentPosition.LEFT + "4^{" + ArgumentPosition.ABOVE_RIGHT + "}" + ArgumentPosition.RIGHT),
    FIVE(ArgumentPosition.LEFT + "5^{" + ArgumentPosition.ABOVE_RIGHT + "}" + ArgumentPosition.RIGHT),
    SIX(ArgumentPosition.LEFT + "6^{" + ArgumentPosition.ABOVE_RIGHT + "}" + ArgumentPosition.RIGHT),
    SEVEN(ArgumentPosition.LEFT + "7^{" + ArgumentPosition.ABOVE_RIGHT + "}" + ArgumentPosition.RIGHT),
    EIGHT(ArgumentPosition.LEFT + "8^{" + ArgumentPosition.ABOVE_RIGHT + "}" + ArgumentPosition.RIGHT),
    NINE(ArgumentPosition.LEFT + "9^{" + ArgumentPosition.ABOVE_RIGHT + "}" + ArgumentPosition.RIGHT);

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
