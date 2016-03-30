package org.hwer.engine.symbols.letters;


import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


/**
 * @class LowerC
 * @brief Implements letter c
 */
public class LowerC extends Letter {
    /**
     * @brief Constructor
     *
     * @param traceGroup
     *     The TraceGroup of this Symbol
     */
    public LowerC (TraceGroup traceGroup) {
        super(traceGroup, true);
    }

    /**
     * @brief Returns the label of this Symbol
     *
     * @return The label of this Symbol
     */
    @Override
    public Labels getLabel () {
        return Labels.LOWER_C;
    }

    /**
     * @brief Returns a string representation of this Symbol
     *
     * @return A string representation of this Symbol
     */
    @Override
    public String toString () {
        return toString("c");
    }

}
