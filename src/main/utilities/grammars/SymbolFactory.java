package main.utilities.grammars;

import main.utilities.Utilities;
import main.utilities.traces.TraceGroup;

public class SymbolFactory{

  public static Symbol create(TraceGroup traceGroup, int label){

    switch(label){
      case Utilities.LABEL_ZERO:
        return (new Number(Number.Types.ZERO, traceGroup));
      case Utilities.LABEL_ONE:
        return (new Number(Number.Types.ONE, traceGroup));
      case Utilities.LABEL_TWO:
        return (new Number(Number.Types.TWO, traceGroup));
      case Utilities.LABEL_THREE:
        return (new Number(Number.Types.THREE, traceGroup));
      case Utilities.LABEL_FOUR:
        return (new Number(Number.Types.FOUR, traceGroup));
      case Utilities.LABEL_FIVE:
        return (new Number(Number.Types.FIVE, traceGroup));
      case Utilities.LABEL_SIX:
        return (new Number(Number.Types.SIX, traceGroup));
      case Utilities.LABEL_SEVEN:
        return (new Number(Number.Types.SEVEN, traceGroup));
      case Utilities.LABEL_EIGHT:
        return (new Number(Number.Types.EIGHT, traceGroup));
      case Utilities.LABEL_NINE:
        return (new Number(Number.Types.NINE, traceGroup));
      case Utilities.LABEL_PLUS:
        return (new Operator(Operator.Types.PLUS, traceGroup, false));
      case Utilities.LABEL_EQUALS:
        return (new Operator(Operator.Types.EQUALS, traceGroup, false));
      case Utilities.LABEL_VARIABLE_X:
        return (new Variable(Variable.Types.X, traceGroup));
      case Utilities.LABEL_VARIABLE_Y:
        return (new Variable(Variable.Types.Y, traceGroup));
      case Utilities.LABEL_MINUS:
        return (new Operator(Operator.Types.MINUS, traceGroup, false));
      default:
        return null;
    }
  }

}
