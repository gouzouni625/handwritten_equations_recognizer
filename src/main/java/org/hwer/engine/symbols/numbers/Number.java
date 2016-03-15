package org.hwer.engine.symbols.numbers;

import java.util.ArrayList;
import java.util.List;

import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.symbols.SymbolFactory.SymbolClass;
import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;

/** @class Number
 *
 *  @brief Implements a Number as a Symbol.
 */
public abstract class Number extends Symbol implements SymbolClass{
  /**
   *  @brief Constructor.
   *
   *  @param traceGroup The TraceGroup of this Number.
   */
  public Number(TraceGroup traceGroup){
    super(traceGroup);

    children_ = new ArrayList<List<Symbol>>();
    children_.add(new ArrayList<Symbol>());
    // A Number accepts children only on ABOVE_RIGHT position, as an exponent.
    childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.ABOVE_RIGHT};
    // A Number can accept as exponent another Number, a Letter, an Operator or an UnrecognizedSymbol.
    childrenClasses_ = new Classes[][] {{Classes.NUMBER, Classes.LETTER, Classes.OPERATOR,
                                           Classes.AMBIGUOUS, Classes.VARIABLE}};
    // - Use sizeChildAcceptanceCriterion for accepting another Number in ABOVE_RIGHT position. That means that, when
    //     drawing an equation, a Number, as an exponent, should have, at max, half the size of the base Number.
    // - Use sizeChildAcceptanceCriterion for accepting a Letter in ABOVE_RIGHT position. That means that, when drawing
    //     an equation, a Letter, as an exponent, should have, at max, half the size of the base Number.
    // - Use widthSizeExceptSQRTFractionLine for accepting an Operator in ABOVE_RIGHT position. That means that, when
    //     drawing an equation, an Operator, as an exponent, should have, at max, half the width of the base Number. This
    //     doesn't apply for the square root and fraction line Symbol. The exclusion of these two is done to avoid the
    //     case of long fractions or square roots not being accepted as exponents to a Number(e.g.
    //     5^{sqrt{5x^{2} + 2x + 6}}). The size-width criterion is done avoid the case where x_{2}=5 gets recognized as
    //     x_{2=}. In this situation, '=' will be bigger, or, at least, not twice as small as '2', so '2' will not accept
    //     '=' as a child.
    // - Use sizeWidthChildAcceptanceCriterion for accepting an UnrecognizedSymbol in ABOVE_RIGHT position. That means
    //     that, when drawing an equation, an UnrecognizedSymbol, as an exponent, should have, at max, half the width
    //     and half the size of the base Number.
    childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{sizeChildAcceptanceCriterion,
                                                                     sizeChildAcceptanceCriterion,
                                                                     widthSizeExceptSQRTFractionLine,
                                                                     sizeWidthChildAcceptanceCriterion,
            sizeChildAcceptanceCriterion,}};
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

  public Classes getClazz(){
    return Classes.NUMBER;
  }

  public String toString(String symbolString){
    return (symbolString + "^{" + ArgumentPosition.ABOVE_RIGHT + "}");
  }

  @Override
  public void reEvaluate(boolean force){}

}
