package org.hwer.engine.symbols.ambiguous;


import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.utilities.traces.TraceGroup;


/**
 * @class CLike
 * @brief Implements the ambiguous Symbol that looks like a c
 */
public class CLike extends Ambiguous {
    /**
     * @brief Constructor
     *
     * @param traceGroup
     *     The TraceGroup of this Symbol
     */
    public CLike (TraceGroup traceGroup) {
        super(traceGroup);

        SymbolFactory symbolFactory = SymbolFactory.getInstance();

        try {
            possibleSymbols_ = new Symbol[] {
                symbolFactory.create(Labels.LOWER_C, traceGroup),
                symbolFactory.create(Labels.LEFT_PARENTHESIS, traceGroup)
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

        if (getNextSymbol() == null) {
            this.choose(possibleSymbols_[1]);
        }
        else {
            Classes nextClass = getNextSymbol().getClazz();

            Labels nextLabel = getNextSymbol().getLabel();

            switch (nextClass) {
                case LETTER:
                    switch (nextLabel) {
                        case LOWER_O:
                            this.choose(possibleSymbols_[0]);
                            break;
                        default:
                            this.choose(possibleSymbols_[1]);
                            break;
                    }
                    break;
                case AMBIGUOUS:
                    switch (nextLabel) {
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
            return Labels.C_LIKE;
        }
    }

}
