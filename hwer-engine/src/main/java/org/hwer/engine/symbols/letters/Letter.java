package org.hwer.engine.symbols.letters;


import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * @class Letter
 * @brief Implements the Symbol class of Letters
 */
public abstract class Letter extends Symbol {
    /**
     * @brief Constructor
     *
     * @param traceGroup
     *     The TraceGroup of this Letter
     * @param init
     *     Whether this Symbol's values should be initialized according to the Number class
     */
    public Letter (TraceGroup traceGroup, boolean init) {
        super(traceGroup);

        if (init) {
            children_ = new ArrayList<List<Symbol>>();
            childrenPositions_ = new ArgumentPosition[] {};
            childrenClasses_ = new Classes[][] {};
            childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {};
        }
    }

    /**
     * @brief Returns the clazz of this SymbolClass
     *
     * @return The clazz of this Symbol
     */
    public Classes getClazz () {
        return Classes.LETTER;
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
        return symbolString;
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
    }

    /**
     * @brief Use this method to give the ability to Symbols for internal changes and decisions
     *
     * @param force
     *     If there are any decisions to be made based on context, force this Symbol to take them
     */
    @Override
    public void reEvaluate (boolean force) {
    }

}
