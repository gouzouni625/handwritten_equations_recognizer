package org.hwer.engine.parsers;


import org.hwer.engine.symbols.Symbol;

import java.util.logging.Logger;


/**
 * @class Parser
 * @brief Defines the API that every Parser should provide
 *        A Parser processes a set of Symbols their relative position an construct an equation.
 */
public abstract class Parser {
    public final Logger logger_ = Logger.getLogger(this.getClass().getName()); //!< A logger

    /**
     * @brief Parses a given set of Symbols setting the relationships between them
     */
    public abstract void parse (Symbol[] symbols);

    /**
     * @brief Returns the equation created by the parsed Symbols
     *
     * @return The equation created by the parsed Symbols
     */
    public abstract String getEquation ();

    /**
     * @brief Builds the equation based upon the current state of the Symbols
     */
    public abstract void buildEquation ();

    /**
     * @brief Resets this Parser
     *        Resetting a Parser should bring it to the state just after it was instantiated
     */
    public abstract void reset ();

    /**
     * @brief Getter method for the Symbols of this Parser
     *
     * @return The Symbols of this Parser
     */
    public abstract Symbol[] getSymbols ();

    protected Symbol[] symbols_; //!< The Symbols of this Parser

}
