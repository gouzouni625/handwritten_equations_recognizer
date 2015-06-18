package main.utilities.grammars;

import main.utilities.Utilities;
import main.utilities.traces.TraceGroup;

public class SymbolFactory{

  public static Symbol create(TraceGroup traceGroup, int label){
    Symbol symbol;

    switch(label){
      case Utilities.LABEL_ZERO:
        symbol = new Number();
        ((Number)(symbol)).type_ = Number.Types.ZERO;
      case Utilities.LABEL_ONE:
        symbol = new Number();
        ((Number)(symbol)).type_ = Number.Types.ONE;
      case Utilities.LABEL_TWO:
        symbol = new Number();
        ((Number)(symbol)).type_ = Number.Types.TWO;
      case Utilities.LABEL_THREE:
        symbol = new Number();
        ((Number)(symbol)).type_ = Number.Types.THREE;
      case Utilities.LABEL_FOUR:
        symbol = new Number();
        ((Number)(symbol)).type_ = Number.Types.FOUR;
      case Utilities.LABEL_FIVE:
        symbol = new Number();
        ((Number)(symbol)).type_ = Number.Types.FIVE;
      case Utilities.LABEL_SIX:
        symbol = new Number();
        ((Number)(symbol)).type_ = Number.Types.SIX;
      case Utilities.LABEL_SEVEN:
        symbol = new Number();
        ((Number)(symbol)).type_ = Number.Types.SEVEN;
      case Utilities.LABEL_EIGHT:
        symbol = new Number();
        ((Number)(symbol)).type_ = Number.Types.EIGHT;
      case Utilities.LABEL_NINE:
        symbol = new Number();
        ((Number)(symbol)).type_ = Number.Types.NINE;
        symbol.arguments_ = new Symbol[2];
        symbol.positionOfArguments_ = new Symbol.ArgumentPosition[2];
        symbol.positionOfArguments_[0] = Symbol.ArgumentPosition.ARGUMENT_ABOVE_RIGHT;
        symbol.positionOfArguments_[1] = Symbol.ArgumentPosition.ARGUMENT_DOWN_RIGHT;
        break;
      case Utilities.LABEL_PLUS:
      case Utilities.LABEL_EQUALS:
        symbol = new Operator(false);
        symbol.positionOfArguments_ = new Symbol.ArgumentPosition[2];
        symbol.positionOfArguments_[0] = Symbol.ArgumentPosition.ARGUMENT_LEFT;
        symbol.positionOfArguments_[1] = Symbol.ArgumentPosition.ARGUMENT_RIGHT;
        break;
      case Utilities.LABEL_VARIABLE_X:
        symbol = new Variable();
        ((Variable)(symbol)).type_ = Variable.Types.X;
      case Utilities.LABEL_VARIABLE_Y:
        symbol = new Variable();
        ((Variable)(symbol)).type_ = Variable.Types.Y;
        symbol.arguments_ = new Symbol[2];
        symbol.positionOfArguments_ = new Symbol.ArgumentPosition[2];
        symbol.positionOfArguments_[0] = Symbol.ArgumentPosition.ARGUMENT_ABOVE_RIGHT;
        symbol.positionOfArguments_[1] = Symbol.ArgumentPosition.ARGUMENT_DOWN_RIGHT;
        break;
      case Utilities.LABEL_MINUS:
        symbol = new Operator(true);
        symbol.positionOfArguments_ = new Symbol.ArgumentPosition[2];
        symbol.positionOfArguments_[0] = Symbol.ArgumentPosition.ARGUMENT_LEFT;
        symbol.positionOfArguments_[1] = Symbol.ArgumentPosition.ARGUMENT_RIGHT;
      default:
        symbol = null;
        break;
    }
    symbol.traceGroup_ = new TraceGroup(traceGroup);

    return symbol;
  }

}
