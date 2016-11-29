package org.hwer.engine.symbols.ambiguous;


import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.utilities.traces.TraceGroup;


/**
 * @class GLike
 * @brief Implements the ambiguous Symbol that looks like g
 */
public class GLike extends Ambiguous {
    /**
     * @brief Constructor
     *
     * @param traceGroup
     *     The TraceGroup of this Symbol
     */
    public GLike (TraceGroup traceGroup) {
        super(traceGroup);

        SymbolFactory symbolFactory = SymbolFactory.getInstance();

        try {
            possibleSymbols_ = new Symbol[] {
                symbolFactory.create(Labels.LOWER_G, traceGroup),
                symbolFactory.create(Labels.NINE, traceGroup)
            };
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * @brief Use this method to give the ability to Symbols for internal changes and decisions
     *
     * @param force
     *     If there are any decisions to be made based on context, force this Symbol to take them
     */
    @Override
    public void reEvaluate (boolean force) {
        if (chosenSymbol_ != this) {
            return;
        }

        // Choose LOWER_G only in case of log.
        if (getPreviousSymbol() == null) {
            this.choose(possibleSymbols_[1]);
        }
        else {
            Classes previousClass = getPreviousSymbol().getClazz();

            Labels previousLabel = getPreviousSymbol().getLabel();

            switch (previousClass) {
                case LETTER:
                    switch (previousLabel) {
                        case LOWER_O:
                            this.choose(possibleSymbols_[0]);
                            break;
                        default:
                            this.choose(possibleSymbols_[1]);
                            break;
                    }
                    break;
                case AMBIGUOUS:
                    switch (previousLabel) {
                        case CIRCLE:
                            // Don't choose yet, it is AMBIGUOUS...
                            // unless you are force to do so...
                            if (force) {
                                this.choose(possibleSymbols_[0]);
                            }
                            break;
                        default:
                            this.choose(possibleSymbols_[1]);
                            break;
                    }
                    break;
                default:
                    this.choose(possibleSymbols_[1]);
                    break;
            }
        }
    }

    /**
     * @brief Returns the label of this Symbol
     *
     * @return The label of this Symbol
     */
    @Override
    public Labels getLabel () {
        if (chosenSymbol_ != this) {
            return chosenSymbol_.getLabel();
        }
        else {
            return Labels.G_LIKE;
        }
    }

}
