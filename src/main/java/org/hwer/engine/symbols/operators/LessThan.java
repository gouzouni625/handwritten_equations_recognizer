package org.hwer.engine.symbols.operators;


import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * @class LessThan
 * @brief Implements less than sign
 */
public class LessThan extends Operator {
    /**
     * @brief Constructor
     *
     * @param traceGroup
     *     The TraceGroup of this Symbol
     */
    public LessThan (TraceGroup traceGroup) {
        super(traceGroup);

        children_ = new ArrayList<List<Symbol>>();
        childrenPositions_ = new ArgumentPosition[] {};
        childrenClasses_ = new Classes[][] {};
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {};
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

        if (relativePosition == ArgumentPosition.ABOVE_RIGHT ||
            relativePosition == ArgumentPosition.BELOW_RIGHT) {
            relativePosition = ArgumentPosition.RIGHT;
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
        return Labels.LESS_THAN;
    }

    /**
     * @brief Returns a string representation of this Symbol
     *
     * @return A string representation of this Symbol
     */
    @Override
    public String toString () {
        return toString("<");
    }

}
