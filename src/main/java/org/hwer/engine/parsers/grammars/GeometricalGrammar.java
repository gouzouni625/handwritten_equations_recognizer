package org.hwer.engine.parsers.grammars;


import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.parsers.GrammarParser.Grammar;

import java.util.logging.Logger;
import java.util.logging.Level;


/**
 * @class GeometricalGrammar
 * @brief Implements a Symbol grammar based on geometrical positioning between Symbols
 */
public class GeometricalGrammar implements Grammar {
    public final Logger logger_ = Logger.getLogger(this.getClass().getName()); //!< A logger

    /**
     * @brief Parses two Symbols defining the relationship between them
     *
     * @param primary
     *     The first Symbol
     * @param secondary
     *     The second Symbol
     */
    public void parse (Symbol primary, Symbol secondary) {
        ArgumentPosition relativePosition = primary.relativePosition(secondary);

        /* ===== Logs Start ===== */
        if (logger_.getLevel() != Level.OFF) {
            logger_.fine("GeometricalGrammar: Relative position between: " + primary + ", " +
                secondary + " : " + relativePosition);
        }
        /* ===== Logs End ===== */

        Symbol.ArgumentType argumentType = primary.setArgument(relativePosition, secondary);
        switch (argumentType) {
            case CHILD:
                secondary.setParent(primary);
                break;
            case NEXT_SYMBOL:
                if (primary.getParent() != null) {
                    relativePosition = primary.getParent().relativePosition(secondary);
                    argumentType = primary.getParent().setArgument(relativePosition, secondary);

                    switch (argumentType) {
                        case CHILD:
                            secondary.setParent(primary.getParent());
                        case NONE:
                            break;
                        case NEXT_SYMBOL:
                            primary.setNextSymbol(null);
                            break;
                    }
                }
                break;
            case NONE:
                if (primary.getParent() != null) {
                    this.parse(primary.getParent(), secondary);
                }
                break;
            default:
                break;
        }
    }

    /**
     * @class ArgumentPosition
     * @brief Holds all the possible relative positions between two Symbols
     */
    public enum ArgumentPosition {
        ABOVE,
        ABOVE_RIGHT,
        RIGHT,
        BELOW_RIGHT,
        BELOW,
        BELOW_LEFT,
        LEFT,
        ABOVE_LEFT,
        INSIDE,
        OUTSIDE
    }

}
