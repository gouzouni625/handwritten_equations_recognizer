package org.hwer.engine.symbols.operators;


import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * @class FractionLine
 * @brief Implements the horizontal line that separates the numerator from the denominator of a
 *        fraction
 */
public class FractionLine extends Operator {
    /**
     * @brief Constructor
     *
     * @param traceGroup
     *     The TraceGroup of this Symbol
     */
    public FractionLine (TraceGroup traceGroup) {
        super(traceGroup);

        childrenPositions_ = new ArgumentPosition[] {
            ArgumentPosition.ABOVE,
            ArgumentPosition.BELOW
        };

        childrenClasses_ = new Classes[][] {
            {
                Classes.NUMBER,
                Classes.OPERATOR,
                Classes.LETTER,
                Classes.AMBIGUOUS,
                Classes.VARIABLE
            },
            {
                Classes.NUMBER,
                Classes.OPERATOR,
                Classes.LETTER,
                Classes.AMBIGUOUS,
                Classes.VARIABLE
            }
        };

        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {
            {
                allChildAcceptanceCriterion,
                allChildAcceptanceCriterion,
                allChildAcceptanceCriterion,
                allChildAcceptanceCriterion,
                allChildAcceptanceCriterion
            },
            {
                allChildAcceptanceCriterion,
                allChildAcceptanceCriterion,
                allChildAcceptanceCriterion,
                allChildAcceptanceCriterion,
                allChildAcceptanceCriterion
            }
        };
    }

    /**
     * @brief Returns the relative position of a given Symbol to this Symbol
     *
     * @param symbol
     *     The given Symbol
     *
     * @return The relative position of a given Symbol to this Symbol
     */
    @Override
    public ArgumentPosition relativePosition (Symbol symbol) {
        TraceGroup traceGroup = getTraceGroup();
        TraceGroup symbolTraceGroup = symbol.getTraceGroup();

        int xPosition;
        int yPosition;
        if (symbolTraceGroup.getCenterOfMass().x_ < traceGroup.getTopLeftCorner().x_) {
            xPosition = - 1;

            if (symbolTraceGroup.getCenterOfMass().y_ < Math.tan(Math.PI / 4) *
                (symbolTraceGroup.getCenterOfMass().x_ - traceGroup.getTopLeftCorner().x_) +
                traceGroup.getTopLeftCorner().y_) {
                yPosition = - 1;
            }
            else if (symbolTraceGroup.getCenterOfMass().y_ <= - Math.tan(Math.PI / 4) *
                (symbolTraceGroup.getCenterOfMass().x_ - traceGroup.getTopLeftCorner().x_) +
                traceGroup.getTopLeftCorner().y_) {
                yPosition = 0;
            }
            else {
                yPosition = 1;
            }
        }
        else if (symbolTraceGroup.getCenterOfMass().x_ <= traceGroup.getBottomRightCorner().x_) {
            xPosition = 0;

            if (symbolTraceGroup.getCenterOfMass().y_ < traceGroup.getBottomRightCorner().y_) {
                yPosition = - 1;
            }
            else if (symbolTraceGroup.getCenterOfMass().y_ <= traceGroup.getTopLeftCorner().y_) {
                yPosition = 0;
            }
            else {
                yPosition = 1;
            }
        }
        else {
            xPosition = 1;
            if (symbolTraceGroup.getCenterOfMass().y_ < - Math.tan(Math.PI / 4) *
                (symbolTraceGroup.getCenterOfMass().x_ - traceGroup.getBottomRightCorner().x_) +
                traceGroup.getBottomRightCorner().y_) {
                yPosition = - 1;
            }
            else if (symbolTraceGroup.getCenterOfMass().y_ <= Math.tan(Math.PI / 4) *
                (symbolTraceGroup.getCenterOfMass().x_ - traceGroup.getBottomRightCorner().x_) +
                traceGroup.getBottomRightCorner().y_) {
                yPosition = 0;
            }
            else {
                yPosition = 1;
            }
        }

        if (yPosition == 1) {

            if (xPosition == - 1) {
                return ArgumentPosition.ABOVE_LEFT;
            }
            else if (xPosition == 0) {
                return ArgumentPosition.ABOVE;
            }
            else {
                return ArgumentPosition.RIGHT;
            }

        }
        else if (yPosition == 0) {

            if (xPosition == - 1) {
                return ArgumentPosition.LEFT;
            }
            else if (xPosition == 0) {
                if (symbolTraceGroup.getArea() > traceGroup.getArea()) {
                    return ArgumentPosition.OUTSIDE;
                }
                else {
                    return ArgumentPosition.INSIDE;
                }
            }
            else {
                return ArgumentPosition.RIGHT;
            }

        }
        else {

            if (xPosition == - 1) {
                return ArgumentPosition.BELOW_LEFT;
            }
            else if (xPosition == 0) {
                return ArgumentPosition.BELOW;
            }
            else {
                return ArgumentPosition.RIGHT;
            }

        }
    }

    /**
     * @brief Clears a String that represents a Symbol from unneeded characters
     *
     * @param string
     *     The String to be cleared
     *
     * @return The cleared String
     */
    @Override
    public String clearString (String string) {
        String result = string;

        for (ArgumentPosition argumentPosition : childrenPositions_) {
            result = result.replaceAll(Pattern.quote("{") + argumentPosition + Pattern.quote("}"), "{}");
        }

        return result;
    }

    /**
     * @brief Returns the label of this Symbol
     *
     * @return The label of this Symbol
     */
    @Override
    public Labels getLabel () {
        return Labels.FRACTION_LINE;
    }

    /**
     * @brief Returns a string representation of this Symbol
     *
     * @return A string representation of this Symbol
     */
    @Override
    public String toString () {
        return toString("\\frac{" + ArgumentPosition.ABOVE + "}{" + ArgumentPosition.BELOW + "}");
    }

    /**
     * @brief Resets this Symbol
     *        Resetting a Symbols means to bring the Symbol back at the state that was the moment
     *        right after it was instantiated
     */
    @Override
    public void reset () {
        super.reset();

        children_ = new ArrayList<List<Symbol>>();
        children_.add(new ArrayList<Symbol>());
        children_.add(new ArrayList<Symbol>());
    }

}
