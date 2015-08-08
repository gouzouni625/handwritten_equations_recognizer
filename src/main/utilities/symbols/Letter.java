package main.utilities.symbols;

import java.util.List;
import java.util.ArrayList;

import main.utilities.symbols.Symbol.ArgumentPosition;
import main.utilities.symbols.Symbol.ChildAcceptanceCriterion;
import main.utilities.symbols.Symbol.SymbolClass;
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
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{sizeChildAcceptanceCriterion, sizeChildAcceptanceCriterion, widthSizeExceptSQRTFractionLine, sizeChildAcceptanceCriterion},
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

  @Override
  public ArgumentPosition relativePosition(Symbol symbol){
    ArgumentPosition relativePosition = super.relativePosition(symbol);

    if(relativePosition == ArgumentPosition.ABOVE){
      relativePosition = ArgumentPosition.ABOVE_RIGHT;
    }
    else if(relativePosition == ArgumentPosition.BELOW){
      relativePosition = ArgumentPosition.BELOW_RIGHT;
    }

    return relativePosition;
  }

}
