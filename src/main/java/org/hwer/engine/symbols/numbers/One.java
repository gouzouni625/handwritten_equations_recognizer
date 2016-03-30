package org.hwer.engine.symbols.numbers;


import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


/**
 * @class One
 * @brief Implements number one
 */
public class One extends Number {
    /**
     * @brief Constructor
     *
     * @param traceGroup
     *     The TraceGroup of this Symbol
     */
    public One (TraceGroup traceGroup) {
        super(traceGroup);
    }

    /**
     * @brief Returns the label of this Symbol
     *
     * @return The label of this Symbol
     */
    @Override
    public Labels getLabel () {
        return Labels.ONE;
    }

    /**
     * @brief Returns a string representation of this Symbol
     *
     * @return A string representation of this Symbol
     */
    @Override
    public String toString () {
        return toString("1");
    }

}
