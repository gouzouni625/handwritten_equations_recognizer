package main.utilities.grammars;

import main.utilities.traces.TraceGroup;

public class SymbolFactory{

  public static Symbol create(TraceGroup traceGroup, int label){

    switch(label){
      case SymbolFactory.LABEL_ZERO:
        return (new Number(Number.Types.ZERO, traceGroup));
      case SymbolFactory.LABEL_ONE:
        return (new Number(Number.Types.ONE, traceGroup));
      case SymbolFactory.LABEL_TWO:
        return (new Number(Number.Types.TWO, traceGroup));
      case SymbolFactory.LABEL_THREE:
        return (new Number(Number.Types.THREE, traceGroup));
      case SymbolFactory.LABEL_FOUR:
        return (new Number(Number.Types.FOUR, traceGroup));
      case SymbolFactory.LABEL_FIVE:
        return (new Number(Number.Types.FIVE, traceGroup));
      case SymbolFactory.LABEL_SIX:
        return (new Number(Number.Types.SIX, traceGroup));
      case SymbolFactory.LABEL_SEVEN:
        return (new Number(Number.Types.SEVEN, traceGroup));
      case SymbolFactory.LABEL_EIGHT:
        return (new Number(Number.Types.EIGHT, traceGroup));
      case SymbolFactory.LABEL_NINE:
        return (new Number(Number.Types.NINE, traceGroup));
      case SymbolFactory.LABEL_PLUS:
        return (new Operator(Operator.Types.PLUS, traceGroup));
      case SymbolFactory.LABEL_EQUALS:
        return (new Operator(Operator.Types.EQUALS, traceGroup));
      case SymbolFactory.LABEL_VARIABLE_X:
        return (new Variable(Variable.Types.X, traceGroup));
      case SymbolFactory.LABEL_VARIABLE_Y:
        return (new Variable(Variable.Types.Y, traceGroup));
      case SymbolFactory.LABEL_HORIZONTAL_LINE:
        return (new Operator(Operator.Types.MINUS, traceGroup));
      default:
        return null;
    }
  }

  public static final int UNKNOWN_LABEL = -1;
  public static final int LABEL_ZERO = 0;
  public static final int LABEL_ONE = 1;
  public static final int LABEL_TWO = 2;
  public static final int LABEL_THREE = 3;
  public static final int LABEL_FOUR = 4;
  public static final int LABEL_FIVE = 5;
  public static final int LABEL_SIX = 6;
  public static final int LABEL_SEVEN = 7;
  public static final int LABEL_EIGHT = 8;
  public static final int LABEL_NINE = 9;
  public static final int LABEL_PLUS = 10;
  public static final int LABEL_EQUALS = 11;
  public static final int LABEL_VARIABLE_X = 12;
  public static final int LABEL_VARIABLE_Y = 13;
  public static final int LABEL_HORIZONTAL_LINE = 14;

}
