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
 * @class SquareRott
 * @brief Implements square root sign
 */
public class SquareRoot extends Operator {
    /**
     * @brief Constructor
     *
     * @param traceGroup
     *     The TraceGroup of this Symbol
     */
    public SquareRoot (TraceGroup traceGroup) {
        super(traceGroup);

        childrenPositions_ = new ArgumentPosition[] {
            ArgumentPosition.INSIDE,
            ArgumentPosition.ABOVE_RIGHT
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
                sizeChildAcceptanceCriterion,
                sizeChildAcceptanceCriterion,
                sizeChildAcceptanceCriterion,
                sizeChildAcceptanceCriterion,
                sizeChildAcceptanceCriterion
            },
            {
                sizeChildAcceptanceCriterion,
                sizeChildAcceptanceCriterion,
                sizeChildAcceptanceCriterion,
                sizeChildAcceptanceCriterion,
                sizeChildAcceptanceCriterion
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
        ArgumentPosition relativePosition = super.relativePosition(symbol);

        if (relativePosition == ArgumentPosition.BELOW_RIGHT) {
            relativePosition = ArgumentPosition.RIGHT;
        }

        return relativePosition;
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
            result = result.replaceAll(Pattern.quote("^{") + argumentPosition +
                Pattern.quote("}"), "");
            result = result.replaceAll(Pattern.quote("{") + argumentPosition +
                Pattern.quote("}"), "{}");
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
        return Labels.SQUARE_ROOT;
    }

    /**
     * @brief Returns a string representation of this Symbol
     *
     * @return A string representation of this Symbol
     */
    @Override
    public String toString () {
        return toString("\\sqrt{" + ArgumentPosition.INSIDE + "}^{" +
            ArgumentPosition.ABOVE_RIGHT + "}");
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
