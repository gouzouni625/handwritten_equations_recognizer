package org.hwer.api.concurrency.tasks;


import org.hwer.engine.parsers.Parser;
import org.hwer.engine.partitioners.Partitioner;
import org.hwer.engine.utilities.traces.TraceGroup;


/**
 * @class EvaluateTask
 * @brief Task that evaluates a group of Traces
 */
public class EvaluateTask implements Runnable {
    /**
     * @brief Constructor
     *
     * @param partitioner
     *     The Partitioner to be used
     * @param parser
     *     The Parser to be used
     * @param traceGroup
     *     The group of Traces to be evaluated
     */
    public EvaluateTask (Partitioner partitioner, Parser parser, TraceGroup traceGroup) {
        partitioner_ = partitioner;
        parser_ = parser;
        traceGroup_ = traceGroup;
    }

    /**
     * @brief Runnable's run method
     */
    @Override
    public void run () {
        parser_.parse(partitioner_.append(parser_.getSymbols(), traceGroup_));

        parser_.buildEquation();
    }

    private final Partitioner partitioner_; //!< The partitioner of this task
    private final Parser parser_; //!< The parser of this task
    private final TraceGroup traceGroup_; //!< The group of Traces to be evaluated

}
