package org.hwer.engine.symbols.operators;


import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.symbols.SymbolFactory.SymbolClass;
import org.hwer.engine.utilities.traces.TraceGroup;


/**
 * @class Operator
 * @brief Implements the Symbol class of operators
 */
public abstract class Operator extends Symbol implements SymbolClass {
    /**
     * @brief Constructor
     *
     * @param traceGroup
     *     The TraceGroup of this Operator
     */
    public Operator (TraceGroup traceGroup) {
        super(traceGroup);
    }

    /**
     * @brief Returns the clazz of this SymbolClass
     *
     * @return The clazz of this Symbol
     */
    public Classes getClazz () {
        return Classes.OPERATOR;
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
     * @brief Use this method to give the ability to Symbols for internal changes and decisions
     *
     * @param force
     *     If there are any decisions to be made based on context, force this Symbol to take them
     */
    @Override
    public void reEvaluate (boolean force) {
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

}
