package main.utilities.grammars;

import java.util.List;
import java.util.ArrayList;

import main.utilities.grammars.Symbol.ArgumentPosition;
import main.utilities.grammars.Symbol.ChildAcceptanceCriterion;
import main.utilities.grammars.Symbol.SymbolClass;
import main.utilities.traces.TraceGroup;

public class Letter extends Symbol{

  public Letter(Letter.Types type, TraceGroup traceGroup){
    super(traceGroup, SymbolClass.LETTER);

    type_ = type;

    parent_ = null;

    switch(type){
      case LOWER_X:
      case LOWER_Y:
        children_ = new ArrayList<List<Symbol>>();
        children_.add(new ArrayList<Symbol>());
        children_.add(new ArrayList<Symbol>());
        childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.ABOVE_RIGHT, ArgumentPosition.BELOW_RIGHT};
        childrenClass_ = new SymbolClass[][] {{SymbolClass.NUMBER, SymbolClass.LETTER, SymbolClass.OPERATOR, SymbolClass.UNRECOGNIZED}, {SymbolClass.NUMBER, SymbolClass.LETTER}};
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{sizeChildAcceptanceCriterion, sizeChildAcceptanceCriterion, sizeChildAcceptanceCriterion, sizeChildAcceptanceCriterion},
                                                                        {sizeChildAcceptanceCriterion, sizeChildAcceptanceCriterion}};
        break;
    }

    nextSymbol_ = null;
    nextSymbolPositions_ = new ArgumentPosition[] {ArgumentPosition.RIGHT};
 }

  public enum Types{
    LOWER_X("x^{" + ArgumentPosition.ABOVE_RIGHT + "}_{" + ArgumentPosition.BELOW_RIGHT + "}"),
    LOWER_Y("y^{" + ArgumentPosition.ABOVE_RIGHT + "}_{" + ArgumentPosition.BELOW_RIGHT + "}");

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
