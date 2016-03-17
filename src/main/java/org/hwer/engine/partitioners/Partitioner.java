package org.hwer.engine.partitioners;

import org.hwer.engine.classifiers.Classifier;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.utilities.traces.Trace;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.util.ArrayList;

/**
 * @class Partitioner
 * @brief A Partitioner splits a set of traces to groups. Each group of these traces represents a
 * symbol.
 */
public abstract class Partitioner {
    public Partitioner () {
    }

    public Partitioner (Classifier classifier) {
        classifier_ = classifier;
    }

    /**
     * @param expression The main.java.utilities.traces.TraceGroup with the ink traces of the
     *                   equation.
     * @return Returns an array of main.java.utilities.traces.TraceGroup each one of which contains
     * the traces of a single symbol.
     * @brief Partitions a group of ink traces of an equation.
     * <p>
     * The result is an array of groups of ink traces each one of which represents a symbol of the
     * equation.
     */
    public abstract Symbol[] partition (TraceGroup expression);

    public abstract Symbol[] append (Symbol[] symbols, TraceGroup newTraces);

    public abstract Symbol[] remove (Symbol[] symbols, TraceGroup tracesToBeRemoved);

    /**
     * @param silent The value for the silent mode of this Partitioner.
     * @brief Setter method for the silent mode of this Partitioner.
     */
    public void setSilent (boolean silent) {
        silent_ = silent;
    }

    /**
     * @return Returns true if this Partitioner is in silent mode.
     * @brief Getter method for the silent mode of this Partitioner.
     */
    public boolean isSilent () {
        return silent_;
    }

    public void setClassifier (Classifier classifier) {
        classifier_ = classifier;
    }

    public Classifier getClassifier () {
        return classifier_;
    }

    public int getMaxTracesInSymbol () {
        return maxTracesInSymbol_;
    }

    public void setMaxTracesInSymbol (int maxTracesInSymbol) {
        maxTracesInSymbol_ = maxTracesInSymbol;
    }

    protected boolean silent_ = true; //!< Flag defining the silent mode of this Partitioner.

    protected Classifier classifier_; //!< The Classifier used by this Partitioner to partition the
    //!< given equation.

    protected int maxTracesInSymbol_ = 3; //!< The maximum number of traces allowed in a symbol.

}
