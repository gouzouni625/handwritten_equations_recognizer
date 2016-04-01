package org.hwer.engine.partitioners;


import org.hwer.engine.classifiers.Classifier;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.util.logging.Logger;


/**
 * @class Partitioner
 * @brief Defines the API that every partitioner should provide
 *        A partitioner is used to separate the input Traces to Symbols. To do that, a partitioner
 *        uses a classifier to decide the best combination of Traces that create a Symbol.
 */
public abstract class Partitioner {
    public final Logger logger_ = Logger.getLogger(this.getClass().getName()); //!< A logger

    /**
     * @brief Constructor
     *
     * @param classifier
     *     The classifier of this Partitioner
     */
    public Partitioner (Classifier classifier) {
        classifier_ = classifier;
    }

    /**
     * @brief Partitions a TraceGroup to Symbols
     *
     * @param traceGroup
     *     The TraceGroup to be partitioned
     *
     * @return The Symbols that the given TraceGroup is partitioned to
     */
    public abstract Symbol[] partition (TraceGroup traceGroup);

    /**
     * @brief Appends a group of Traces to an existent group of Symbols
     *
     * @param symbols
     *     The Symbols already identified
     * @param newTraces
     *     The group of Traces to be partitioned
     *
     * @return All the Symbols including the identified and the newly identified
     */
    public abstract Symbol[] append (Symbol[] symbols, TraceGroup newTraces);

    /**
     * @brief Removes a group of Traces from an existent group of Symbols
     *
     * @param symbols
     *     The Symbols already identified
     * @param tracesToBeRemoved
     *     The group of Traces to be removed
     *
     * @return The re-evaluated Symbols after the Traces were removed
     */
    public abstract Symbol[] remove (Symbol[] symbols, TraceGroup tracesToBeRemoved);

    /**
     * @brief Getter method for the maximum number of Traces in a Symbol
     *
     * @return The maximum number of Traces in a Symbol
     */
    public int getMaxTracesInSymbol () {
        return maxTracesInSymbol_;
    }

    /**
     * @brief Setter method for the maximum number of Traces in a Symbol
     *
     * @param maxTracesInSymbol
     *     The new value for the maximum number of Traces in a Symbol
     */
    public void setMaxTracesInSymbol (int maxTracesInSymbol) {
        maxTracesInSymbol_ = maxTracesInSymbol;
    }

    protected final Classifier classifier_; //!< The classifier of this Partitioner

    protected int maxTracesInSymbol_ = 3; //!< The maximum number of Traces a Symbol has

}
