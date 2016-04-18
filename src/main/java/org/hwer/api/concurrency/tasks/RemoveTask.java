package org.hwer.api.concurrency.tasks;


import org.hwer.engine.parsers.Parser;
import org.hwer.engine.partitioners.Partitioner;
import org.hwer.engine.utilities.traces.TraceGroup;


/**
 * @class RemoveTask
 * @brief Task that removed a group of Traces from the already parsed Symbols
 */
public class RemoveTask implements Runnable {
    /**
     * @brief Constructor
     *
     * @param partitioner
     *     The Partitioner to be used
     * @param parser
     *     The Parser to be used
     * @param traceGroup
     *     The group of Traces to be removed
     */
    public RemoveTask (Partitioner partitioner, Parser parser, TraceGroup traceGroup) {
        partitioner_ = partitioner;
        parser_ = parser;
        traceGroup_ = traceGroup;
    }

    /**
     * @brief Runnable's run method
     */
    @Override
    public void run () {
        parser_.parse(partitioner_.remove(parser_.getSymbols(), traceGroup_));

        parser_.buildEquation();
    }

    private final Partitioner partitioner_; //!< The partitioner of this task
    private final Parser parser_; //!< The parser of this task
    private final TraceGroup traceGroup_; //!< The group of Traces to be removed

}
