package org.hwer.engine.symbols.numbers;


import java.util.ArrayList;
import java.util.List;

import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.symbols.SymbolFactory.SymbolClass;
import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;


/**
 * @class Number
 * @brief Implements the Symbol class of numbers
 */
public abstract class Number extends Symbol implements SymbolClass {
    /**
     * @brief Constructor
     *
     * @param traceGroup
     *     The TraceGroup of this Number
     */
    public Number (TraceGroup traceGroup) {
        super(traceGroup);

        childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.ABOVE_RIGHT};
        childrenClasses_ = new Classes[][] {{
            Classes.NUMBER, Classes.LETTER, Classes.OPERATOR, Classes.AMBIGUOUS, Classes.VARIABLE
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

        if (relativePosition == ArgumentPosition.ABOVE) {
            relativePosition = ArgumentPosition.ABOVE_RIGHT;
        }
        else if (relativePosition == ArgumentPosition.BELOW_RIGHT) {
            relativePosition = ArgumentPosition.RIGHT;
        }

        return relativePosition;
    }

    /**
     * @brief Returns the clazz of this SymbolClass
     *
     * @return The clazz of this Symbol
     */
    public Classes getClazz () {
        return Classes.NUMBER;
    }

    /**
     * @brief Returns a string representation of this SymbolClass
     *        The string representation can vary based on the Symbol of this SymbolClass that
     *        this method is called for.
     *
     * @param symbolString
     *     A string provided by the Symbol of this SymbolClass that this method is called for
     *
     * @return The string representation of this SymbolClass
     */
    public String toString (String symbolString) {
        return (symbolString + "^{" + ArgumentPosition.ABOVE_RIGHT + "}");
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
     * @brief Use this method to give the ability to Symbols for internal changes and decisions
     *
     * @param force
     *     If there are any decisions to be made based on context, force this Symbol to take them
     */
    @Override
    public void reEvaluate (boolean force) {}

}
