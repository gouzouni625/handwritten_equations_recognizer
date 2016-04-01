package org.hwer.engine.utilities.logging;


import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;


/**
 * @class SingleLineFormatter
 * @brief Implements a Formatter for the engine's log records
 */
public class SingleLineFormatter extends Formatter{
    /**
     * @brief Returns a string representation of a formatted LogRecord
     *
     * @param logRecord
     *     The LogRecord
     *
     * @return A string representation of a formatted LogRecord
     */
    @Override
    public String format (LogRecord logRecord) {
        return new Date() + " " + logRecord.getLevel() + " " + logRecord.getMessage() + "\r\n";
    }

}
