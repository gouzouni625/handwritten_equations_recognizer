package org.hwer.engine.symbols.letters;


import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * @class LowerE
 * @brief Implements letter e
 */
public class LowerE extends Letter {
    /**
     * @brief Constructor
     *
     * @param traceGroup
     *     The TraceGroup of this Symbol
     */
    public LowerE (TraceGroup traceGroup) {
        super(traceGroup, false);

        childrenPositions_ = new ArgumentPosition[] {
            ArgumentPosition.ABOVE_RIGHT
        };

        childrenClasses_ = new Classes[][] {{
            Classes.NUMBER,
            Classes.LETTER,
            Classes.OPERATOR,
            Classes.AMBIGUOUS,
            Classes.VARIABLE
        }};

        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{
            sizeChildAcceptanceCriterion,
            sizeChildAcceptanceCriterion,
            widthSizeExceptSQRTFractionLine,
            sizeWidthChildAcceptanceCriterion,
            sizeChildAcceptanceCriterion
        }};
    }

    /**
     * @brief Resets this Symbol
     *        Resetting a Symbols means to bring the Symbol back at the state that was the moment
     *        right after it was instantiated
     */
    @Override
    public void reset () {
        setParent(null);
        setPreviousSymbol(null);
        setNextSymbol(null);

        children_ = new ArrayList<List<Symbol>>();
        children_.add(new ArrayList<Symbol>());
    }

    /**
     * @brief Returns the label of this Symbol
     *
     * @return The label of this Symbol
     */
    @Override
    public Labels getLabel () {
        return Labels.LOWER_E;
    }

    /**
     * @brief Returns a string representation of this Symbol
     *
     * @return A string representation of this Symbol
     */
    @Override
    public String toString () {
        return toString("e^{" + ArgumentPosition.ABOVE_RIGHT + "}");
    }

}
