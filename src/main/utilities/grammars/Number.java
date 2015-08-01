package main.utilities.grammars;

import java.util.ArrayList;
import java.util.List;

import main.utilities.grammars.Symbol;
import main.utilities.grammars.Symbol.ArgumentPosition;
import main.utilities.grammars.Symbol.ChildAcceptanceCriterion;
import main.utilities.traces.TraceGroup;

public class Number extends Symbol{

  public Number(Number.Types type, TraceGroup traceGroup){
    super(traceGroup, SymbolClass.NUMBER);

    type_ = type;

    parent_ = null;

    children_ = new ArrayList<List<Symbol>>();
    children_.add(new ArrayList<Symbol>());
    childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.ABOVE_RIGHT};
    childrenClass_ = new SymbolClass[][] {{SymbolClass.NUMBER, SymbolClass.LETTER, SymbolClass.OPERATOR, SymbolClass.UNRECOGNIZED}};
    /* use width along with are to avoid the situation \frac{x}{3}=y where = gets to be the exponent of 3.  */
    childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{sizeChildAcceptanceCriterion, sizeChildAcceptanceCriterion,
                                                                     aboveRightOperatorAcceptanceCriterion, sizeWidthChildAcceptanceCriterion}};

    nextSymbol_ = null;
    nextSymbolPositions_ = new ArgumentPosition[] {ArgumentPosition.RIGHT};

  }

  public ChildAcceptanceCriterion aboveRightOperatorAcceptanceCriterion = new ChildAcceptanceCriterion() {

    @Override
    public boolean accept(Symbol symbol, Symbol child, ArgumentPosition relativePosition){
      // In case there is a big fraction as an exponent, accept it. The case of the whole acceptance criteria
      // was to prevent x_{3}=2 giving x_{3^{=}}. Since we have a fraction, this is not a problem.
      if(child.type_ == Operator.Types.FRACTION_LINE){
        return (symbol.traceGroup_.getArea() > 2 * child.traceGroup_.getArea());
      }
      else{
        return ((symbol.traceGroup_.getArea() > 2 * child.traceGroup_.getArea()) &&
                (symbol.traceGroup_.getWidth() > 2 * child.traceGroup_.getWidth()));
      }
    }
  };

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

}
