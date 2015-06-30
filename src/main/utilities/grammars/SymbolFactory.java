package main.utilities.grammars;

import main.utilities.traces.TraceGroup;

public class SymbolFactory{

  public static Symbol createByLabel(TraceGroup traceGroup, int label){

    switch(label){
      case SymbolFactory.LABEL_ZERO:
        return SymbolFactory.createByType(Number.Types.ZERO, traceGroup);
      case SymbolFactory.LABEL_ONE:
        return SymbolFactory.createByType(Number.Types.ONE, traceGroup);
      case SymbolFactory.LABEL_TWO:
        return SymbolFactory.createByType(Number.Types.TWO, traceGroup);
      case SymbolFactory.LABEL_THREE:
        return SymbolFactory.createByType(Number.Types.THREE, traceGroup);
      case SymbolFactory.LABEL_FOUR:
        return SymbolFactory.createByType(Number.Types.FOUR, traceGroup);
      case SymbolFactory.LABEL_FIVE:
        return SymbolFactory.createByType(Number.Types.FIVE, traceGroup);
      case SymbolFactory.LABEL_SIX:
        return SymbolFactory.createByType(Number.Types.SIX, traceGroup);
      case SymbolFactory.LABEL_SEVEN:
        return SymbolFactory.createByType(Number.Types.SEVEN, traceGroup);
      case SymbolFactory.LABEL_EIGHT:
        return SymbolFactory.createByType(Number.Types.EIGHT, traceGroup);
      case SymbolFactory.LABEL_NINE:
        return SymbolFactory.createByType(Number.Types.NINE, traceGroup);
      case SymbolFactory.LABEL_VARIABLE_X:
        return SymbolFactory.createByType(Variable.Types.X, traceGroup);
      case SymbolFactory.LABEL_VARIABLE_Y:
        return SymbolFactory.createByType(Variable.Types.Y, traceGroup);
      case SymbolFactory.LABEL_PLUS:
        return SymbolFactory.createByType(Operator.Types.PLUS, traceGroup);
      case SymbolFactory.LABEL_EQUALS:
        return SymbolFactory.createByType(Operator.Types.EQUALS, traceGroup);
      case SymbolFactory.LABEL_HORIZONTAL_LINE:
        return (new UnrecognizedSymbol(UnrecognizedSymbol.Types.HORIZONTAL_LINE, traceGroup));
      default:
        return null;
    }
  }

  public static <E extends Enum<E>> Symbol createByType(E type, TraceGroup traceGroup){
    if(type == Number.Types.ZERO){
      return (new Number(Number.Types.ZERO, traceGroup));
    }
    else if(type == Number.Types.ONE){
      return (new Number(Number.Types.ONE, traceGroup));
    }
    else if(type == Number.Types.TWO){
      return (new Number(Number.Types.TWO, traceGroup));
    }
    else if(type == Number.Types.THREE){
      return (new Number(Number.Types.THREE, traceGroup));
    }
    else if(type == Number.Types.FOUR){
      return (new Number(Number.Types.FOUR, traceGroup));
    }
    else if(type == Number.Types.FIVE){
      return (new Number(Number.Types.FIVE, traceGroup));
    }
    else if(type == Number.Types.SIX){
      return (new Number(Number.Types.SIX, traceGroup));
    }
    else if(type == Number.Types.SEVEN){
      return (new Number(Number.Types.SEVEN, traceGroup));
    }
    else if(type == Number.Types.EIGHT){
      return (new Number(Number.Types.EIGHT, traceGroup));
    }
    else if(type == Number.Types.NINE){
      return (new Number(Number.Types.NINE, traceGroup));
    }
    else if(type == Variable.Types.X){
      return (new Variable(Variable.Types.X, traceGroup));
    }
    else if(type == Variable.Types.Y){
      return (new Variable(Variable.Types.Y, traceGroup));
    }
    else if(type == Operator.Types.PLUS){
      return (new Operator(Operator.Types.PLUS, traceGroup));
    }
    else if(type == Operator.Types.EQUALS){
      return (new Operator(Operator.Types.EQUALS, traceGroup));
    }
    else if(type == Operator.Types.MINUS){
      return (new Operator(Operator.Types.MINUS, traceGroup));
    }
    else if(type == Operator.Types.FRACTION_LINE){
      return (new Operator(Operator.Types.FRACTION_LINE, traceGroup));
    }
    else{
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
