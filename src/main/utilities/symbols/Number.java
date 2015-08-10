package main.utilities.symbols;

import java.util.ArrayList;
import java.util.List;

import main.utilities.symbols.Symbol;
import main.utilities.traces.TraceGroup;

/** @class Number
 *
 *  @brief Implements a Number as a Symbol.
 */
public class Number extends Symbol{
  /**
   *  @brief Constructor.
   *
   *  @param type The type of this Number.
   *  @param traceGroup The TraceGroup of this Number.
   */
  public Number(Number.Types type, TraceGroup traceGroup){
    super(traceGroup, SymbolClass.NUMBER);

    type_ = type;

    // Initially the Symbol has no parent.
    parent_ = null;

    children_ = new ArrayList<List<Symbol>>();
    children_.add(new ArrayList<Symbol>());
    // A Number accepts children only on ABOVE_RIGHT position, as an exponent.
    childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.ABOVE_RIGHT};
    // A Number can accept as expoenent another Number, a Letter, an Operator or an UnrecognizedSymbol.
    childrenClass_ = new SymbolClass[][] {{SymbolClass.NUMBER, SymbolClass.LETTER, SymbolClass.OPERATOR,
                                           SymbolClass.UNRECOGNIZED}};
    /* use width along with are to avoid the situation frac{x}{3}=y where = gets to be the exponent of 3.  */
    // - Use sizeChildAcceptanceCriterion for accepting another Number in ABOVE_RIGHT position. That means that, when
    //     drawing an equation, a Number, as an exponent, should have, at max, half the size of the base Number.
    // - Use sizeChildAcceptanceCriterion for accepting a Letter in ABOVE_RIGHT position. That means that, when drawing
    //     an equation, a Letter, as an exponent, should have, at max, half the size of the base Number.
    // - Use widthSizeExceptSQRTFractionLine for accepting an Operator in ABOVE_RIGHT position. That means that, when
    //     drawing an equation, an Operator, as an exponent, should have, at max, half the width of the base Number. This
    //     doesn't apply for the Operator.Types.SQRT and Operator.TypesFraction_LINE Symbol. This is done to avoid the
    //     case of long fractions or square roots not being accepted as exponents to a Number(e.g.
    //     5^{sqrt{5x^{2} + 2x + 6}}).
    // - Use sizeWidthChildAcceptanceCriterion for accepting an UnrecognizedSymbol in ABOVE_RIGHT position. That means
    //     that, when drawing an equation, an UnrecognizedSymbol, as an exponent, should have, at max, half the width
    //     and half the size of the base Number.
    childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{sizeChildAcceptanceCriterion,
                                                                     sizeChildAcceptanceCriterion,
                                                                     widthSizeExceptSQRTFractionLine,
                                                                     sizeWidthChildAcceptanceCriterion}};

    // Initially the Symbol has no next symbol.
    nextSymbol_ = null;
    // A Number accepts next symbols only at RIGHT position.
    nextSymbolPositions_ = new ArgumentPosition[] {ArgumentPosition.RIGHT};

  }

  /** @class Types
   *
   *  @brief Contains all the different types of Number objects.
   *
   *  The TeX format for every Number is saved as the String representation of the type.
   */
  public enum Types{
    ZERO("0^{" + ArgumentPosition.ABOVE_RIGHT + "}"), //!< Number ZERO 0.
    ONE("1^{" + ArgumentPosition.ABOVE_RIGHT + "}"), //!< Number ONE 1.
    TWO("2^{" + ArgumentPosition.ABOVE_RIGHT + "}"), //!< Number TWO 2.
    THREE("3^{" + ArgumentPosition.ABOVE_RIGHT + "}"), //!< Number THREE 3.
    FOUR("4^{" + ArgumentPosition.ABOVE_RIGHT + "}"), //!< Number FOUR 4.
    FIVE("5^{" + ArgumentPosition.ABOVE_RIGHT + "}"), //!< Number FIVE 5.
    SIX("6^{" + ArgumentPosition.ABOVE_RIGHT + "}"), //!< Number SIX 6.
    SEVEN("7^{" + ArgumentPosition.ABOVE_RIGHT + "}"), //!< Number SEVEN 7.
    EIGHT("8^{" + ArgumentPosition.ABOVE_RIGHT + "}"), //!< Number EIGHT 8.
    NINE("9^{" + ArgumentPosition.ABOVE_RIGHT + "}"); //!< Number NINE 9.

    /**
     *  @brief Constructor.
     *
     *  @param stringValue The String value for this Type.
     */
    private Types(String stringValue){
      stringValue_ = stringValue;
    }

    /**
     *  @brief Returns the string value of this Type.
     */
    @Override
    public String toString(){
      return stringValue_;
    }

    private String stringValue_; //!< The string value of this Type.
  }

  /**
   *  @brief Finds the relative position between this Symbol and a given Symbol.
   *
   *  Override the default implementation to treat some special cases.
   *
   *  @param symbol The given Symbol.
   *
   *  @return Returns the relative position between this Symbol and the given one.
   */
  @Override
  public ArgumentPosition relativePosition(Symbol symbol){
    // Get the relative position from the default implementation.
    ArgumentPosition relativePosition = super.relativePosition(symbol);

    // To make things easier, if the relative position is ABOVE, continue as if it was ABOVE_RIGHT. This is done to avoid
    // missing an exponent that is drawn a little to the left, thus ABOVE this Number.
    if(relativePosition == ArgumentPosition.ABOVE){
      relativePosition = ArgumentPosition.ABOVE_RIGHT;
    }
    // To make things easier, if the relative position is BELOW_RIGHT, continue as if it was RIGHT. This is done to avoid
    // missing a next Symbol that is drawn a little lower thus BELOW_RIGHT this Number.
    else if(relativePosition == ArgumentPosition.BELOW_RIGHT){
      relativePosition = ArgumentPosition.RIGHT;
    }

    return relativePosition;
  }

}
