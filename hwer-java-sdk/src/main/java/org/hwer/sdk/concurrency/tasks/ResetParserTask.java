package org.hwer.sdk.concurrency.tasks;


import org.hwer.engine.parsers.Parser;


/**
 * @class ResetParserTask
 * @brief Task that resets a Parser
 */
public class ResetParserTask implements Runnable {
    /**
     * @brief Constructor
     *
     * @param parser
     *     The Parser to be reset
     */
    public ResetParserTask (Parser parser) {
        parser_ = parser;
    }

    /**
     * @brief Runnable's run method
     */
    @Override
    public void run () {
        parser_.reset();

        parser_.buildEquation();
    }

    private final Parser parser_; //!< The Parser of this task

}
