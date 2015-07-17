package main.utilities.grammars;

import java.util.List;
import java.util.ArrayList;

import main.utilities.grammars.Symbol;
import main.utilities.grammars.Symbol.SymbolClass;
import main.utilities.traces.TraceGroup;

public class Operator extends Symbol{

  public Operator(Operator.Types type, TraceGroup traceGroup){
    super(traceGroup, SymbolClass.OPERATOR);

    type_ = type;

    parent_ = null;

    switch(type){
      case PLUS:
      case EQUALS:
      case MINUS:
        children_ = new ArrayList<List<Symbol>>();
        childrenPositions_ = new ArgumentPosition[] {};
        childrenClass_ = new SymbolClass[][] {};
        break;
      case FRACTION_LINE:
        children_ = new ArrayList<List<Symbol>>();
        children_.add(new ArrayList<Symbol>());
        children_.add(new ArrayList<Symbol>());
        childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.ABOVE, ArgumentPosition.BELOW};
        childrenClass_ = new SymbolClass[][] {{SymbolClass.NUMBER, SymbolClass.OPERATOR, SymbolClass.LETTER}, {SymbolClass.NUMBER, SymbolClass.OPERATOR, SymbolClass.LETTER}};
        break;
    }

    nextSymbol_ = null;
    nextSymbolPositions_ = new ArgumentPosition[] {ArgumentPosition.RIGHT};
  }

  public enum Types{
    PLUS("+"),
    EQUALS("="),
    MINUS("-"),
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

}
