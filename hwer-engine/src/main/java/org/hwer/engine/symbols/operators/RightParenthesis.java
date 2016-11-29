package org.hwer.engine.symbols.operators;


import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * @class RightParenthesis
 * @brief Implements right parenthesis sign
 */
public class RightParenthesis extends Operator {
    /**
     * @brief Constructor
     *
     * @param traceGroup
     *     The TraceGroup of this Symbol
     */
    public RightParenthesis (TraceGroup traceGroup) {
        super(traceGroup);

        childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.ABOVE_RIGHT};

        childrenClasses_ = new Classes[][] {{
            Classes.NUMBER,
            Classes.LETTER,
            Classes.OPERATOR,
            Classes.AMBIGUOUS,
            Classes.VARIABLE
        }};

        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{
            allChildAcceptanceCriterion,
            allChildAcceptanceCriterion,
            allChildAcceptanceCriterion,
            allChildAcceptanceCriterion,
            allChildAcceptanceCriterion
        }};
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

        if (relativePosition == ArgumentPosition.INSIDE ||
            relativePosition == ArgumentPosition.BELOW_RIGHT) {
            relativePosition = ArgumentPosition.LEFT;
        }
        else if (relativePosition == ArgumentPosition.ABOVE) {
            relativePosition = ArgumentPosition.ABOVE_RIGHT;
        }

        return relativePosition;
    }

    /**
     * @brief Returns the label of this Symbol
     *
     * @return The label of this Symbol
     */
    @Override
    public Labels getLabel () {
        return Labels.RIGHT_PARENTHESIS;
    }

    /**
     * @brief Returns a string representation of this Symbol
     *
     * @return A string representation of this Symbol
     */
    @Override
    public String toString () {
        return toString(")^{" + ArgumentPosition.ABOVE_RIGHT + "}");
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
    }

}
