package org.hwer.utilities.symbols;

import java.util.List;
import java.util.ArrayList;

import org.hwer.utilities.traces.TraceGroup;

/** @class Operator
 *
 *  @brief Implements mathematical operators as Symbol objects.
 */
public class Operator extends Symbol{
  /**
   *  @brief Constructor.
   *
   *  @param type The type of this Operator.
   *  @param traceGroup The TraceGroup of this Operator.
   */
  public Operator(Operator.Types type, TraceGroup traceGroup){
    super(traceGroup, SymbolClass.OPERATOR);

    type_ = type;

    // Initially the operator has no parent.
    parent_ = null;

    // Initialize the members of this operator, based on its type.
    switch(type){
      case PLUS:
      case EQUALS:
      case MINUS:
        // PLUS, EQUALS and MINUS operators do not accept any children.
        children_ = new ArrayList<List<Symbol>>();
        childrenPositions_ = new ArgumentPosition[] {};
        childrenClass_ = new SymbolClass[][] {};
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {};
        break;
      case FRACTION_LINE:
        // FRACTION_LINE operator accepts children in positions ABOVE and BELOW.
        children_ = new ArrayList<List<Symbol>>();
        children_.add(new ArrayList<Symbol>());
        children_.add(new ArrayList<Symbol>());
        childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.ABOVE, ArgumentPosition.BELOW};
        // Symbols accepted as children in ABOVE position: Number, Operator, Letter, UnrecognizedSymbol.
        // Symbols accepted as children in BELOW position: Number, Operator, Letter, UnrecognizedSymbol.
        childrenClass_ = new SymbolClass[][] {{SymbolClass.NUMBER, SymbolClass.OPERATOR, SymbolClass.LETTER,
                                               SymbolClass.UNRECOGNIZED},
                                              {SymbolClass.NUMBER, SymbolClass.OPERATOR, SymbolClass.LETTER,
                                               SymbolClass.UNRECOGNIZED}};
        // Use no criteria for accepting any child.
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{allChildAcceptanceCriterion,
                                                                         allChildAcceptanceCriterion,
                                                                         allChildAcceptanceCriterion,
                                                                         allChildAcceptanceCriterion},
                                                                        {allChildAcceptanceCriterion,
                                                                         allChildAcceptanceCriterion,
                                                                         allChildAcceptanceCriterion,
                                                                         allChildAcceptanceCriterion}};
        break;
      case SQRT:
        // SQRT operator accept children in positions INSIDE and ABOVE_RIGHT.
        children_ = new ArrayList<List<Symbol>>();
        children_.add(new ArrayList<Symbol>());
        children_.add(new ArrayList<Symbol>());
        childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.INSIDE, ArgumentPosition.ABOVE_RIGHT};
        // Symbols accepted as children in ABOVE position: Number, Operator, Letter, UnrecognizedSymbol.
        // Symbols accepted as children in ABOVE_RIGHT position: Number, Operator, Letter, UnrecognizedSymbol.
        childrenClass_ = new SymbolClass[][] {{SymbolClass.NUMBER, SymbolClass.OPERATOR, SymbolClass.LETTER,
                                               SymbolClass.UNRECOGNIZED},
                                              {SymbolClass.NUMBER, SymbolClass.OPERATOR, SymbolClass.LETTER,
                                               SymbolClass.UNRECOGNIZED}};
        // Use sizeChildAcceptanceCriterion for accepting a child. That means that, when drawing an equation, a child
        // of an SQRT Symbol should have, at most, half the size of the SQRT Symbol.
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{sizeChildAcceptanceCriterion,
                                                                         sizeChildAcceptanceCriterion,
                                                                         sizeChildAcceptanceCriterion,
                                                                         sizeChildAcceptanceCriterion},
                                                                        {sizeChildAcceptanceCriterion,
                                                                         sizeChildAcceptanceCriterion,
                                                                         sizeChildAcceptanceCriterion,
                                                                         sizeChildAcceptanceCriterion}};
        break;
      case LEFT_PARENTHESIS:
        // LEFT_PARENTHESIS Operator objects do not accept children.
        children_ = new ArrayList<List<Symbol>>();
        childrenPositions_ = new ArgumentPosition[] {};
        childrenClass_ = new SymbolClass[][] {};
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {};
        break;
      case RIGHT_PARENTHESIS:
        // RIGHT_PARENTHESIS Operator objects accept children ABOVE_RIGHT.
        children_ = new ArrayList<List<Symbol>>();
        children_.add(new ArrayList<Symbol>());
        childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.ABOVE_RIGHT};
        // SymbolClass Accepted as ABOVE_RIGHT child: Number, Operator, Letter, UnrecognizedSymbol.
        childrenClass_ = new SymbolClass[][] {{SymbolClass.NUMBER, SymbolClass.LETTER, SymbolClass.OPERATOR,
                                               SymbolClass.UNRECOGNIZED}};
        // Use no criteria for accepting any child.
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{allChildAcceptanceCriterion,
                                                                         allChildAcceptanceCriterion,
                                                                         allChildAcceptanceCriterion,
                                                                         allChildAcceptanceCriterion}};
        break;
    }

    // Initially the Operator has no next symbol.
    nextSymbol_ = null;
    // An Operator accepts next symbols only at RIGHT position.
    nextSymbolPositions_ = new ArgumentPosition[] {ArgumentPosition.RIGHT};
  }

  /** @class Types
   *
   *  @brief Contains all the different types of Operator objects.
   *
   *  The TeX format for every Operator is saved as the String representation of the type.
   */
  public enum Types{
    PLUS("+"), //!< Operator PLUS + .
    EQUALS("="), //!< Operator EQUALS = .
    MINUS("-"), //!< Operator MINUS - .
    FRACTION_LINE("\\frac{" + ArgumentPosition.ABOVE + "}{" + ArgumentPosition.BELOW + "}"), //!< Operator FRACTION_LINE .
    SQRT("\\sqrt{" + ArgumentPosition.INSIDE + "}^{" + ArgumentPosition.ABOVE_RIGHT + "}"), //!< Operator SQRT .
    LEFT_PARENTHESIS("("), //!< Operator LEFT_PARENTHESIS ( .
    RIGHT_PARENTHESIS(")^{" + ArgumentPosition.ABOVE_RIGHT + "}"); //!< Operator RIGHT_PARENTHESIS ) .

    /**
     *  @brief Constructor.
     *
     *  @param stringValue The String value for this type.
     */
    private Types(String stringValue){
      stringValue_ = stringValue;
    }

    /**
     *  @brief Returns the String value of this type.
     */
    @Override
    public String toString(){
      return stringValue_;
    }

    private String stringValue_; //!< The String value of this type.
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
    switch((Types)type_){
      case PLUS:
        // Get the relative position from the default implementation.
        ArgumentPosition relativePosition = super.relativePosition(symbol);

        // To make things easier, if the relative position is ABOVE_RIGHT or BELOW_RIGHT, continue as if it was RIGHT.
        // This is done to avoid missing a next symbol that it is drawn a little higher of a little lower than
        // this Symbol.
        if(relativePosition == ArgumentPosition.ABOVE_RIGHT || relativePosition == ArgumentPosition.BELOW_RIGHT){
          relativePosition = ArgumentPosition.RIGHT;
        }

        return relativePosition;
      case EQUALS:
        // The way the relative position of a Symbol to an EQUALS Operator is calculated is the follow:
        // Calculating x:
        // If the center of mass of the symbol is to the left of the top left corner of EQUALS, the x = -1.
        // If the center of mass of the symbol is to the right of the top left corner but to the left of the bottom right
        //   corner of EQUALS, then x = 0.
        // In any other case, x = 1.
        //
        // Calculating y:
        // if x = 1:
        // Let a line segment with a slope of 45 degrees start from the top right corner of the EQUALS Operator and extend
        //   to infinity. If the center of mass of the Symbol is above this line segment, then y = 1.
        // Let a line segment with a slope of -45 degrees start from the bottom right corner of the EQUALS Operator and
        //   extend to infinity. if the center of mass of the Symbol is below this line segment, the y = -1.
        // In any other case, y = 0.
        //
        // if x = 0:
        // If the center of mass of the Symbol is below the bottom right corner of EQUALS, then y = -1.
        // If the center of mass of the Symbol is above the bottom right corner of EQUALs but below the top left corner,
        // the y = 0.
        // In any other case, y = 1.
        //
        // If x = -1:
        // Let a line segment with a slope of 135 degrees start from the top left corner of the EQUALS Operator and extend
        //   to infinity. If the center of mass of the Symbol is above this line segment, then y = 1.
        // Let a line segment with a slope of -135 degrees start from the bottom left corner of the EQUALS OPerator and
        //   extend to infinity. If the center of mass of the Symbol is below this line segment, then y = -1.
        // In any other case, y = 0.
        //
        // Position is derived as follows:
        // x     y     position
        // -1    -1    BELOW_LEFT
        // -1    0     LEFT
        // -1    1     ABOVE_LEFT
        //
        // 0     -1    BELOW
        // 0     0     INSIDE
        // 0     1     ABOVE
        //
        // 1     -1    BELOW_RIGHT which gets changed to RIGHT. This is to avoid the case where a symbol is drawn a little
        //                         lower and thus is BELOW_RIGHT of equals symbol and not right.
        // 1     0     RIGHT
        // 1     1     ABOVE_RIGHT which gets changed to RIGHT. This is to avoid the case where a symbol is drawn a little
        //                         higher and thus is ABOVE_RIGHT of equals symbol and not right.
        traceGroup_.calculateCorners();

        int xPosition;
        int yPosition;
        if(symbol.traceGroup_.getCenterOfMass().x_ < traceGroup_.getTopLeftCorner().x_){
          xPosition = -1;

          if(symbol.traceGroup_.getCenterOfMass().y_ < Math.tan(Math.PI / 4) * (symbol.traceGroup_.getCenterOfMass().x_ -
                                                                                traceGroup_.getBottomLeftCorner().x_) +
                                                                               traceGroup_.getBottomLeftCorner().y_){
            yPosition = -1;
          }
          else if(symbol.traceGroup_.getCenterOfMass().y_ <= -Math.tan(Math.PI / 4) *
                  (symbol.traceGroup_.getCenterOfMass().x_ - traceGroup_.getTopLeftCorner().x_) +
                  traceGroup_.getTopLeftCorner().y_){
            yPosition = 0;
          }
          else{
            yPosition = 1;
          }
        }
        else if(symbol.traceGroup_.getCenterOfMass().x_ <= traceGroup_.getBottomRightCorner().x_){
          xPosition = 0;

          if(symbol.traceGroup_.getCenterOfMass().y_ < traceGroup_.getBottomRightCorner().y_){
            yPosition = -1;
          }
          else if(symbol.traceGroup_.getCenterOfMass().y_ <= traceGroup_.getTopLeftCorner().y_){
            yPosition = 0;
          }
          else{
            yPosition = 1;
          }
        }
        else{
          xPosition = 1;
          if(symbol.traceGroup_.getCenterOfMass().y_ < -Math.tan(Math.PI / 4) * (symbol.traceGroup_.getCenterOfMass().x_ -
                                                                                 traceGroup_.getBottomRightCorner().x_) +
                                                                                traceGroup_.getBottomRightCorner().y_){
            yPosition = -1;
          }
          else if(symbol.traceGroup_.getCenterOfMass().y_ <= Math.tan(Math.PI / 4) *
                  (symbol.traceGroup_.getCenterOfMass().x_ - traceGroup_.getTopRightCorner().x_) +
                  traceGroup_.getTopRightCorner().y_){
            yPosition = 0;
          }
          else{
            yPosition = 1;
          }
        }

        if(yPosition == 1){

          if(xPosition == -1){
            return ArgumentPosition.ABOVE_LEFT;
          }
          else if(xPosition == 0){
            return ArgumentPosition.ABOVE;
          }
          else{
            return ArgumentPosition.RIGHT;
          }

        }
        else if(yPosition == 0){

          if(xPosition == -1){
            return ArgumentPosition.LEFT;
          }
          else if(xPosition == 0){
            if(symbol.traceGroup_.getArea() > traceGroup_.getArea()){
              return Symbol.ArgumentPosition.OUTSIDE;
            }
            else{
              return Symbol.ArgumentPosition.INSIDE;
            }
          }
          else{
            return ArgumentPosition.RIGHT;
          }

        }
        else{

          if(xPosition == -1){
            return ArgumentPosition.BELOW_LEFT;
          }
          else if(xPosition == 0){
            return ArgumentPosition.BELOW;
          }
          else{
            return ArgumentPosition.RIGHT; //ArgumentPosition.BELOW_RIGHT; EQUALS symbol can't accept BELOW_RIGHT argument.
          }

        }
      case MINUS:
      case FRACTION_LINE:
        // The relative position between a symbol and a minus operator or a symbol and a fraction line operator depends
        // on two parameters, x and y.
        //
        // Calculating x:
        // If the center of mass of the symbol is to the left of the top left corner of the operator, the x = -1.
        // If the center of mass of the symbol is to the right of the top left corner but to the left of the bottom right
        //   corner of the operator, then x = 0.
        // In any other case, x = 1.
        //
        // Calculating y:
        // if x = 1:
        // Let a line segment with a slope of 45 degrees start from the top right corner of the operator and extend
        //   to infinity. If the center of mass of the Symbol is above this line segment, then y = 1.
        // Let a line segment with a slope of -45 degrees start from the top right corner of the operator and
        //   extend to infinity. if the center of mass of the Symbol is below this line segment, the y = -1.
        // In any other case, y = 0.
        //
        // if x = 0:
        // If the center of mass of the Symbol is below the bottom right corner of the operator, then y = -1.
        // If the center of mass of the Symbol is above the bottom right corner of the operator but below the top left
        // corner, the y = 0.
        // In any other case, y = 1.
        //
        // If x = -1:
        // Let a line segment with a slope of 135 degrees start from the top left corner of the operator and extend
        //   to infinity. If the center of mass of the Symbol is above this line segment, then y = 1.
        // Let a line segment with a slope of -135 degrees start from the top left corner of the oPerator and
        //   extend to infinity. If the center of mass of the Symbol is below this line segment, then y = -1.
        // In any other case, y = 0.
        //
        // Position is derived as follows:
        // x     y     position
        // -1    -1    BELOW_LEFT
        // -1    0     LEFT
        // -1    1     ABOVE_LEFT
        //
        // 0     -1    BELOW
        // 0     0     INSIDE
        // 0     1     ABOVE
        //
        // 1     -1    BELOW_RIGHT which gets changed to RIGHT. This is to avoid the case where a symbol is drawn a little
        //                         lower and thus is BELOW_RIGHT of the operator symbol and not right.
        // 1     0     RIGHT
        // 1     1     ABOVE_RIGHT which gets changed to RIGHT. This is to avoid the case where a symbol is drawn a little
        //                         higher and thus is ABOVE_RIGHT of the operator symbol and not right.
        traceGroup_.calculateCorners();

        if(symbol.traceGroup_.getCenterOfMass().x_ < traceGroup_.getTopLeftCorner().x_){
          xPosition = -1;

          if(symbol.traceGroup_.getCenterOfMass().y_ < Math.tan(Math.PI / 4) * (symbol.traceGroup_.getCenterOfMass().x_ -
                                                                                traceGroup_.getTopLeftCorner().x_) +
                                                                               traceGroup_.getTopLeftCorner().y_){
            yPosition = -1;
          }
          else if(symbol.traceGroup_.getCenterOfMass().y_ <= -Math.tan(Math.PI / 4) *
                  (symbol.traceGroup_.getCenterOfMass().x_ - traceGroup_.getTopLeftCorner().x_) +
                  traceGroup_.getTopLeftCorner().y_){
            yPosition = 0;
          }
          else{
            yPosition = 1;
          }
        }
        else if(symbol.traceGroup_.getCenterOfMass().x_ <= traceGroup_.getBottomRightCorner().x_){
          xPosition = 0;

          if(symbol.traceGroup_.getCenterOfMass().y_ < traceGroup_.getBottomRightCorner().y_){
            yPosition = -1;
          }
          else if(symbol.traceGroup_.getCenterOfMass().y_ <= traceGroup_.getTopLeftCorner().y_){
            yPosition = 0;
          }
          else{
            yPosition = 1;
          }
        }
        else{
          xPosition = 1;
          if(symbol.traceGroup_.getCenterOfMass().y_ < -Math.tan(Math.PI / 4) * (symbol.traceGroup_.getCenterOfMass().x_ -
                                                                                 traceGroup_.getBottomRightCorner().x_) +
                                                                                traceGroup_.getBottomRightCorner().y_){
            yPosition = -1;
          }
          else if(symbol.traceGroup_.getCenterOfMass().y_ <= Math.tan(Math.PI / 4) *
                  (symbol.traceGroup_.getCenterOfMass().x_ - traceGroup_.getBottomRightCorner().x_) +
                 traceGroup_.getBottomRightCorner().y_){
            yPosition = 0;
          }
          else{
            yPosition = 1;
          }
        }

        if(yPosition == 1){

          if(xPosition == -1){
            return ArgumentPosition.ABOVE_LEFT;
          }
          else if(xPosition == 0){
            return ArgumentPosition.ABOVE;
          }
          else{
            return ArgumentPosition.RIGHT;
          }

        }
        else if(yPosition == 0){

          if(xPosition == -1){
            return ArgumentPosition.LEFT;
          }
          else if(xPosition == 0){
            if(symbol.traceGroup_.getArea() > traceGroup_.getArea()){
              return Symbol.ArgumentPosition.OUTSIDE;
            }
            else{
              return Symbol.ArgumentPosition.INSIDE;
            }
          }
          else{
            return ArgumentPosition.RIGHT;
          }

        }
        else{

          if(xPosition == -1){
            return ArgumentPosition.BELOW_LEFT;
          }
          else if(xPosition == 0){
            return ArgumentPosition.BELOW;
          }
          else{
            return ArgumentPosition.RIGHT;
          }

        }
      case SQRT:
        // Get the relative position using the default implementation.
        relativePosition = super.relativePosition(symbol);

        // To make things easier, if the relative position is BELOW_RIGHT, continue as if it was RIGHT. This is done to
        // avoid missing a next symbol that is drawn a little below and get recognized as BELOW_RIGHT of the sqrt symbol
        // instead of RIGHT.
        if(relativePosition == ArgumentPosition.BELOW_RIGHT){
          relativePosition = ArgumentPosition.RIGHT;
        }

        return relativePosition;
      case LEFT_PARENTHESIS:
        // Get the relative position using the default implementation.
        relativePosition = super.relativePosition(symbol);

        // If the relative position is INSIDE, ABOVE_RIGHT or BELOW_RIGHT, continue as if it was RIGHT.
        // INSIDE: To avoid missing symbols too close to the left parenthesis.
        // ABOVE_RIGHT, BELOW_RIGHT: To avoid missing symbols drawn a little too higher or a little lower than they should.
        if(relativePosition == ArgumentPosition.INSIDE || relativePosition == ArgumentPosition.ABOVE_RIGHT ||
           relativePosition == ArgumentPosition.BELOW_RIGHT){
          relativePosition = ArgumentPosition.RIGHT;
        }

        return relativePosition;
      case RIGHT_PARENTHESIS:
        // Get the relative position using the default implementation.
        relativePosition = super.relativePosition(symbol);

        // If the relative position is INSIDE or BELOW_RIGHT, change it to LEFT.
        // This case will never be used in the current implementation since all the symbols are processed from left to
        // right.
        if(relativePosition == ArgumentPosition.INSIDE || relativePosition == ArgumentPosition.BELOW_RIGHT){
          relativePosition = ArgumentPosition.LEFT;
        }
        // If the relative position is ABOVE, change it to ABOVE_RIGHT. This is to avoid missing an exponent that is
        // drawn a little more to the left than it should.
        else if(relativePosition == ArgumentPosition.ABOVE){
          relativePosition = ArgumentPosition.ABOVE_RIGHT;
        }

        return relativePosition;
      default:
        return null;
    }
  }

}
