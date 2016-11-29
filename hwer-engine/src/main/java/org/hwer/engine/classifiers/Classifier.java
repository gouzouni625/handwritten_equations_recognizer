package org.hwer.engine.classifiers;


import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.utilities.traces.TraceGroup;


/**
 * @class Classifier
 * @brief Defines the API that every classifier should provide
 */
public abstract class Classifier {
    /**
     * @brief Classifies a given TraceGroup
     *        The context of a TraceGroup contains all the Traces that are near but not a part of
     *        the TraceGroup.
     *
     * @param traceGroup
     *     The TraceGroup to classify
     * @param context
     *     The context of the given TraceGroup
     * @param subSymbolCheck
     *     Check sub-groups of the given TraceGroup
     * @param subContextCheck
     *     Check sub-groups of the given context
     *
     * @return The Symbol that the given TraceGroup was classified
     */
    public abstract Symbol classify (TraceGroup traceGroup, TraceGroup context,
                                     boolean subSymbolCheck, boolean subContextCheck);

}
