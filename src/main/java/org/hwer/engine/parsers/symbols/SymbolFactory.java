package org.hwer.engine.parsers.symbols;

import org.hwer.engine.utilities.traces.TraceGroup;

/** @class SymbolFactory
 *
 *  @brief A class used to create Symbol objects.
 *
 *  This class also contains all the needed variables and methods to make the transition of data between the equation
 *  partitioner and the parser.
 */
public class SymbolFactory{
  /**
   *  @brief Creates a Symbol object for a given label.
   *
   *  This label is the label used by the equation partitioner and the neural network, thus an integer.
   *
   *  @param traceGroup The TraceGroup of the Symbol to be created.
   *  @param label The label of the Symbol to be created.
   *
   *  @return Returns the created Symbol.
   */
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
      case SymbolFactory.LABEL_LOWER_X:
        return SymbolFactory.createByType(Letter.Types.LOWER_X, traceGroup);
      case SymbolFactory.LABEL_LOWER_Y:
        return SymbolFactory.createByType(Letter.Types.LOWER_Y, traceGroup);
      case SymbolFactory.LABEL_PLUS:
        return SymbolFactory.createByType(Operator.Types.PLUS, traceGroup);
      case SymbolFactory.LABEL_EQUALS:
        return SymbolFactory.createByType(Operator.Types.EQUALS, traceGroup);
      case SymbolFactory.LABEL_HORIZONTAL_LINE:
        return (new UnrecognizedSymbol(UnrecognizedSymbol.Types.HORIZONTAL_LINE, traceGroup));
      case SymbolFactory.LABEL_SQRT:
        return SymbolFactory.createByType(Operator.Types.SQRT, traceGroup);
      case SymbolFactory.LABEL_LEFT_PARENTHESIS:
        return SymbolFactory.createByType(Operator.Types.LEFT_PARENTHESIS, traceGroup);
      case SymbolFactory.LABEL_RIGHT_PARENTHESIS:
        return SymbolFactory.createByType(Operator.Types.RIGHT_PARENTHESIS, traceGroup);
      default:
        return null;
    }
  }

  /**
   *  @brief Creates a Symbol object for a given type.
   *
   *  @param type The type of the Symbol to be created.
   *  @param traceGroup The TraceGroup of the Symbol to be created.
   *
   *  @return Returns the created Symbol.
   */
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
    else if(type == Letter.Types.LOWER_X){
      return (new Letter(Letter.Types.LOWER_X, traceGroup));
    }
    else if(type == Letter.Types.LOWER_Y){
      return (new Letter(Letter.Types.LOWER_Y, traceGroup));
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
    else if(type == Operator.Types.SQRT){
      return (new Operator(Operator.Types.SQRT, traceGroup));
    }
    else if(type == Operator.Types.LEFT_PARENTHESIS){
      return (new Operator(Operator.Types.LEFT_PARENTHESIS, traceGroup));
    }
    else if(type == Operator.Types.RIGHT_PARENTHESIS){
      return (new Operator(Operator.Types.RIGHT_PARENTHESIS, traceGroup));
    }
    else{
      return null;
    }
  }

  public static <E extends Enum<?>> int getLabelByType(E type){
    if(type == Number.Types.ZERO){
      return LABEL_ZERO;
    }
    else if(type == Number.Types.ONE){
      return LABEL_ONE;
    }
    else if(type == Number.Types.TWO){
      return LABEL_TWO;
    }
    else if(type == Number.Types.THREE){
      return LABEL_THREE;
    }
    else if(type == Number.Types.FOUR){
      return LABEL_FOUR;
    }
    else if(type == Number.Types.FIVE){
      return LABEL_FIVE;
    }
    else if(type == Number.Types.SIX){
      return LABEL_SIX;
    }
    else if(type == Number.Types.SEVEN){
      return LABEL_SEVEN;
    }
    else if(type == Number.Types.EIGHT){
      return LABEL_EIGHT;
    }
    else if(type == Number.Types.NINE){
      return LABEL_NINE;
    }
    else if(type == Letter.Types.LOWER_X){
      return LABEL_LOWER_X;
    }
    else if(type == Letter.Types.LOWER_Y){
      return LABEL_LOWER_Y;
    }
    else if(type == Operator.Types.PLUS){
      return LABEL_PLUS;
    }
    else if(type == Operator.Types.EQUALS){
      return LABEL_EQUALS;
    }
    else if(type == Operator.Types.MINUS){
      return LABEL_HORIZONTAL_LINE;
    }
    else if(type == Operator.Types.FRACTION_LINE){
      return LABEL_HORIZONTAL_LINE;
    }
    else if(type == Operator.Types.SQRT){
      return LABEL_SQRT;
    }
    else if(type == Operator.Types.LEFT_PARENTHESIS){
      return LABEL_LEFT_PARENTHESIS;
    }
    else if(type == Operator.Types.RIGHT_PARENTHESIS){
      return LABEL_RIGHT_PARENTHESIS;
    }
    else{
      return UNKNOWN_LABEL;
    }
  }

  public static final int UNKNOWN_LABEL = -1; //!< Label to denote the lack of a label.
  public static final int LABEL_ZERO = 0; //!< The integer used by the equation partitioner for '0'.
  public static final int LABEL_ONE = 1; //!< The integer used by the equation partitioner for '1'.
  public static final int LABEL_TWO = 2; //!< The integer used by the equation partitioner for '2'.
  public static final int LABEL_THREE = 3; //!< The integer used by the equation partitioner for '3'.
  public static final int LABEL_FOUR = 4; //!< The integer used by the equation partitioner for '4'.
  public static final int LABEL_FIVE = 5; //!< The integer used by the equation partitioner for '5'.
  public static final int LABEL_SIX = 6; //!< The integer used by the equation partitioner for '6'.
  public static final int LABEL_SEVEN = 7; //!< The integer used by the equation partitioner for '7'.
  public static final int LABEL_EIGHT = 8; //!< The integer used by the equation partitioner for '8'.
  public static final int LABEL_NINE = 9; //!< The integer used by the equation partitioner for '9'.
  public static final int LABEL_PLUS = 10; //!< The integer used by the equation partitioner for '+'.
  public static final int LABEL_EQUALS = 11; //!< The integer used by the equation partitioner for '='.
  public static final int LABEL_LOWER_X = 12; //!< The integer used by the equation partitioner for 'x'.
  public static final int LABEL_LOWER_Y = 13; //!< The integer used by the equation partitioner for 'y'.
  public static final int LABEL_HORIZONTAL_LINE = 14; //!< The integer used by the equation partitioner for '-'.
  public static final int LABEL_SQRT = 15; //!< The integer used by the equation partitioner for sqrt symbol.
  public static final int LABEL_LEFT_PARENTHESIS = 16; //!< The integer used by the equation partitioner for '('.
  public static final int LABEL_RIGHT_PARENTHESIS = 17; //!< The integer used by the equation partitioner for ')'.

}
