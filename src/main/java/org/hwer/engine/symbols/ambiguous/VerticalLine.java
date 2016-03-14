package org.hwer.engine.symbols.ambiguous;

import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;

public class VerticalLine extends Ambiguous {

    public VerticalLine(TraceGroup traceGroup){
        super(traceGroup);

        SymbolFactory symbolFactory = SymbolFactory.getInstance();

        try {
            possibleSymbols_ = new Symbol[] {
                    symbolFactory.create(Labels.LOWER_I, traceGroup),
                    symbolFactory.create(Labels.LOWER_L, traceGroup),
                    symbolFactory.create(Labels.ONE, traceGroup)
            };
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Chooses the type of this UnrecognizedSymbol.
     */
    @Override
    public void reEvaluate (boolean force) {
        if (chosenSymbol_ != this) {
            return;
        }

        // Choose LOWER_I only for sin, LOWER_L only for log.
        if(getPreviousSymbol() == null && getNextSymbol() == null){
            this.choose(possibleSymbols_[2]);
        }
        else if(getPreviousSymbol() == null){
            // Check for log.
            Classes nextClass = getNextSymbol().getClazz();
            Labels nextLabel = getNextSymbol().getLabel();

            switch(nextClass){
                case LETTER:
                    switch (nextLabel){
                        case LOWER_O:
                            // We have an 'o'. Check if there is a 'g' after it.
                            if(getNextSymbol().getNextSymbol() == null){
                                this.choose(possibleSymbols_[2]);
                            }
                            else{
                                Classes nextNextClass = getNextSymbol().getNextSymbol().getClazz();
                                Labels nextNextLabel = getNextSymbol().getNextSymbol().getLabel();

                                switch(nextNextClass){
                                    case LETTER:
                                        switch (nextNextLabel){
                                            case LOWER_G:
                                                this.choose(possibleSymbols_[1]);
                                                break;
                                            default:
                                                this.choose(possibleSymbols_[2]);
                                                break;
                                        }
                                        break;
                                    case AMBIGUOUS:
                                        switch(nextNextLabel){
                                            case G_LIKE:
                                                // Don't choose yet, it is AMBIGUOUS...
                                                // unless you are force to do so...
                                                if(force) {
                                                    this.choose(possibleSymbols_[1]);
                                                }
                                                break;
                                        }
                                        break;
                                    default:
                                        this.choose(possibleSymbols_[2]);
                                        break;
                                }
                            }
                            break;
                        default:
                            this.choose(possibleSymbols_[2]);
                            break;
                    }
                    break;
                case AMBIGUOUS:
                    switch(nextLabel){
                        case CIRCLE:
                            // We have a possible 'o'. Check if there is a 'g' after it.
                            if(getNextSymbol().getNextSymbol() == null){
                                this.choose(possibleSymbols_[2]);
                            }
                            else{
                                Classes nextNextClass = getNextSymbol().getNextSymbol().getClazz();
                                Labels nextNextLabel = getNextSymbol().getNextSymbol().getLabel();

                                switch(nextNextClass){
                                    case LETTER:
                                        switch (nextNextLabel){
                                            case LOWER_G:
                                                // Don't choose yet, it is AMBIGUOUS...
                                                // unless you are force to do so...
                                                if(force) {
                                                    this.choose(possibleSymbols_[1]);
                                                }
                                                break;
                                            default:
                                                this.choose(possibleSymbols_[2]);
                                                break;
                                        }
                                        break;
                                    case AMBIGUOUS:
                                        switch(nextNextLabel){
                                            case G_LIKE:
                                                // Don't choose yet, it is AMBIGUOUS...
                                                // unless you are force to do so...
                                                if(force) {
                                                    this.choose(possibleSymbols_[1]);
                                                }
                                                break;
                                        }
                                        break;
                                    default:
                                        this.choose(possibleSymbols_[2]);
                                        break;
                                }
                            }
                            break;
                        default:
                            this.choose(possibleSymbols_[2]);
                            break;
                    }
                    break;
                default:
                    this.choose(possibleSymbols_[2]);
                    break;
            }
        }
        else if(getNextSymbol() == null){
            this.choose(possibleSymbols_[2]);
        }
        else{
            Classes nextClass = getNextSymbol().getClazz();
            Labels nextLabel = getNextSymbol().getLabel();

            switch(nextClass){
                case LETTER:
                    switch (nextLabel){
                        case LOWER_O:
                            // It is definitely not a sin. Check for log.
                            // We have an 'o'. Check if there is a 'g' after it.
                            if(getNextSymbol().getNextSymbol() == null){
                                this.choose(possibleSymbols_[2]);
                            }
                            else{
                                Classes nextNextClass = getNextSymbol().getNextSymbol().getClazz();
                                Labels nextNextLabel = getNextSymbol().getNextSymbol().getLabel();

                                switch(nextNextClass){
                                    case LETTER:
                                        switch (nextNextLabel){
                                            case LOWER_G:
                                                this.choose(possibleSymbols_[1]);
                                                break;
                                            default:
                                                this.choose(possibleSymbols_[2]);
                                                break;
                                        }
                                        break;
                                    case AMBIGUOUS:
                                        switch(nextNextLabel){
                                            case G_LIKE:
                                                // Don't choose yet, it is AMBIGUOUS...
                                                // unless you are force to do so...
                                                if(force) {
                                                    this.choose(possibleSymbols_[1]);
                                                }
                                                break;
                                        }
                                        break;
                                    default:
                                        this.choose(possibleSymbols_[2]);
                                        break;
                                }
                            }
                            break;
                        case LOWER_N:
                            // It is definitely not a log. Check for sin.
                            Classes previousClass = getPreviousSymbol().getClazz();
                            Labels previousLabel = getPreviousSymbol().getLabel();

                            switch(previousClass){
                                case LETTER:
                                    switch(previousLabel){
                                        case LOWER_S:
                                            this.choose(possibleSymbols_[0]);
                                            break;
                                        default:
                                            this.choose(possibleSymbols_[2]);
                                            break;
                                    }
                                    break;
                                case AMBIGUOUS:
                                    switch(previousLabel){
                                        case S_LIKE:
                                            // Don't choose yet, it is AMBIGUOUS...
                                            // unless you are force to do so...
                                            if(force) {
                                                this.choose(possibleSymbols_[0]);
                                            }
                                            break;
                                        default:
                                            this.choose(possibleSymbols_[2]);
                                            break;
                                    }
                                    break;
                                default:
                                    this.choose(possibleSymbols_[2]);
                                    break;
                            }
                            break;
                        default:
                            this.choose(possibleSymbols_[2]);
                            break;
                    }
                    break;
                case AMBIGUOUS:
                    // It is definitely not a sin since 'n' is not AMBIGUOUS.
                    // Check for log.
                    switch(nextLabel){
                        case CIRCLE:
                            // We have a possible 'o'. Check if there is a 'g' after it.
                            if(getNextSymbol().getNextSymbol() == null){
                                this.choose(possibleSymbols_[2]);
                            }
                            else{
                                Classes nextNextClass = getNextSymbol().getNextSymbol().getClazz();
                                Labels nextNextLabel = getNextSymbol().getNextSymbol().getLabel();

                                switch(nextNextClass){
                                    case LETTER:
                                        switch (nextNextLabel){
                                            case LOWER_G:
                                                // Don't choose yet, it is AMBIGUOUS...
                                                // unless you are force to do so...
                                                if(force) {
                                                    this.choose(possibleSymbols_[1]);
                                                }
                                                break;
                                            default:
                                                this.choose(possibleSymbols_[2]);
                                                break;
                                        }
                                        break;
                                    case AMBIGUOUS:
                                        switch(nextNextLabel){
                                            case G_LIKE:
                                                // Don't choose yet, it is AMBIGUOUS...
                                                // unless you are force to do so...
                                                if(force) {
                                                    this.choose(possibleSymbols_[1]);
                                                }
                                                break;
                                        }
                                        break;
                                    default:
                                        this.choose(possibleSymbols_[2]);
                                        break;
                                }
                            }
                            break;
                        default:
                            this.choose(possibleSymbols_[2]);
                            break;
                    }
                    break;
                default:
                    this.choose(possibleSymbols_[2]);
                    break;
            }
        }
    }

    @Override
    public Labels getLabel () {
        if (chosenSymbol_ != this) {
            return chosenSymbol_.getLabel();
        }
        else{
            return Labels.VERTICAL_LINE;
        }
    }
}
