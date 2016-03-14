package org.hwer.engine.symbols.ambiguous;


import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;

public class SLike extends Ambiguous {

    public SLike(TraceGroup traceGroup){
        super(traceGroup);

        SymbolFactory symbolFactory = SymbolFactory.getInstance();

        try {
            possibleSymbols_ = new Symbol[] {
                    symbolFactory.create(Labels.LOWER_S, traceGroup),
                    symbolFactory.create(Labels.FIVE, traceGroup)
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

        // Choose LOWER_S only in case of sin or cos.
        if(getPreviousSymbol() == null && getNextSymbol() == null){
            this.choose(possibleSymbols_[1]);
        }
        else if(getPreviousSymbol() == null){
            // check for sin.
            Classes nextClass = getNextSymbol().getClazz();
            Labels nextLabel = getNextSymbol().getLabel();

            switch(nextClass){
                case LETTER:
                    switch(nextLabel){
                        case LOWER_I:
                            // We have an 'i'. Check if there is an 'n' after that.
                            if(getNextSymbol().getNextSymbol() == null){
                                this.choose(possibleSymbols_[1]);
                            }
                            else{
                                Classes nextNextClass = getNextSymbol().getNextSymbol().getClazz();
                                Labels nextNextLabel = getNextSymbol().getNextSymbol().getLabel();

                                switch(nextNextClass){
                                    case LETTER:
                                        switch(nextNextLabel){
                                            case LOWER_N:
                                                // We have a sin. Choose it.
                                                this.choose(possibleSymbols_[0]);
                                                break;
                                            default:
                                                this.choose(possibleSymbols_[1]);
                                                break;
                                        }
                                        break;
                                    default: // No need for AMBIGUOUS case, 'n' is not AMBIGUOUS.
                                        this.choose(possibleSymbols_[1]);
                                        break;
                                }
                            }
                            break;
                        default:
                            this.choose(possibleSymbols_[1]);
                            break;
                    }
                    break;
                case AMBIGUOUS:
                    switch(nextLabel){
                        case VERTICAL_LINE:
                            // We might have an 'i'. Check if there is an 'n' after it.
                            if(getNextSymbol().getNextSymbol() == null){
                                this.choose(possibleSymbols_[1]);
                            }
                            else{
                                Classes nextNextClass = getNextSymbol().getNextSymbol().getClazz();
                                Labels nextNextLabel = getNextSymbol().getNextSymbol().getLabel();

                                switch(nextNextClass){
                                    case LETTER:
                                        switch(nextNextLabel){
                                            case LOWER_N:
                                                // Don't choose yet, it is AMBIGUOUS...
                                                // unless you are force to do so...
                                                if(force) {
                                                    this.choose(possibleSymbols_[0]);
                                                }
                                                break;
                                            default:
                                                this.choose(possibleSymbols_[1]);
                                                break;
                                        }
                                        break;
                                    default: // No need for AMBIGUOUS case, 'n' is not AMBIGUOUS.
                                        this.choose(possibleSymbols_[1]);
                                        break;
                                }
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
        else if(getNextSymbol() == null){
            // check for cos.
            Classes previousClass = getPreviousSymbol().getClazz();
            Labels previousLabel = getPreviousSymbol().getLabel();

            switch(previousClass){
                case LETTER:
                    switch(previousLabel){
                        case LOWER_O:
                            // We have an 'o'. Check if there is an 'c' before that.
                            if(getPreviousSymbol().getPreviousSymbol() == null){
                                this.choose(possibleSymbols_[1]);
                            }
                            else{
                                Classes previousPreviousClass = getPreviousSymbol().getPreviousSymbol().getClazz();
                                Labels previousPreviousLabel = getPreviousSymbol().getPreviousSymbol().getLabel();

                                switch(previousPreviousClass){
                                    case LETTER:
                                        switch(previousPreviousLabel){
                                            case LOWER_C:
                                                // We have a cos. Choose it.
                                                this.choose(possibleSymbols_[0]);
                                                break;
                                            default:
                                                this.choose(possibleSymbols_[1]);
                                                break;
                                        }
                                        break;
                                    case AMBIGUOUS:
                                        switch(previousPreviousLabel){
                                            case C_LIKE:
                                                // Don't choose yet, it is AMBIGUOUS...
                                                // unless you are force to do so...
                                                if(force) {
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
                            break;
                        default:
                            this.choose(possibleSymbols_[1]);
                            break;
                    }
                    break;
                case AMBIGUOUS:
                    switch(previousLabel){
                        case CIRCLE:
                            // We have a possible 'o'. Check if there is an 'c' before that.
                            if(getPreviousSymbol().getPreviousSymbol() == null){
                                this.choose(possibleSymbols_[1]);
                            }
                            else{
                                Classes previousPreviousClass = getPreviousSymbol().getPreviousSymbol().getClazz();
                                Labels previousPreviousLabel = getPreviousSymbol().getPreviousSymbol().getLabel();

                                switch(previousPreviousClass){
                                    case LETTER:
                                        switch(previousPreviousLabel){
                                            case LOWER_C:
                                                // Don't choose yet, it is AMBIGUOUS...
                                                // unless you are force to do so...
                                                if(force) {
                                                    this.choose(possibleSymbols_[0]);
                                                }
                                                break;
                                            default:
                                                this.choose(possibleSymbols_[1]);
                                                break;
                                        }
                                        break;
                                    case AMBIGUOUS:
                                        switch(previousPreviousLabel){
                                            case C_LIKE:
                                                // Don't choose yet, it is AMBIGUOUS...
                                                // unless you are force to do so...
                                                if(force) {
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
                            break;
                        default:
                            this.choose(possibleSymbols_[1]);
                            break;
                    }
                    break;
                default: // It is neither sin nor cos.
                    this.choose(possibleSymbols_[1]);
                    break;
            }
        }
        else{
            Classes previousClass = getPreviousSymbol().getClazz();
            Classes nextClass = getNextSymbol().getClazz();

            Labels previousLabel = getPreviousSymbol().getLabel();
            Labels nextLabel = getNextSymbol().getLabel();

            switch(nextClass){
                case LETTER:
                    switch(nextLabel){
                        case LOWER_I:
                            // We have an 'i'. Check if there is an 'n' after that.
                            if(getNextSymbol().getNextSymbol() == null){
                                checkForCos(previousClass, previousLabel, force);
                            }
                            else{
                                Classes nextNextClass = getNextSymbol().getNextSymbol().getClazz();
                                Labels nextNextLabel = getNextSymbol().getNextSymbol().getLabel();

                                switch(nextNextClass){
                                    case LETTER:
                                        switch(nextNextLabel){
                                            case LOWER_N:
                                                // We have a sin. Choose it.
                                                this.choose(possibleSymbols_[0]);
                                                break;
                                            default:
                                                // It is definitely not sin, check for cos.
                                                checkForCos(previousClass, previousLabel, force);
                                                break;
                                        }
                                        break;
                                    default:
                                        // It is definitely not sin, check for cos.
                                        checkForCos(previousClass, previousLabel, force);
                                        break;
                                }
                            }
                            break;
                        default:
                            // It is definitely not sin, check for cos.
                            checkForCos(previousClass, previousLabel, force);
                            break;
                    }
                    break;
                case AMBIGUOUS:
                    switch(nextLabel){
                        case VERTICAL_LINE:
                            // It might be sin, but also check for cos.
                            checkForCos(previousClass, previousLabel, force);
                            break;
                        default:
                            // It is definitely not sin, check for cos.
                            checkForCos(previousClass, previousLabel, force);
                            break;
                    }
                    break;
                default:
                    // It is definitely not sin, check for cos.
                    checkForCos(previousClass, previousLabel, force);
                    break;
            }
        }
    }

    private void checkForCos(Classes previousClass, Labels previousLabel, boolean force){
        switch(previousClass){
            case LETTER:
                switch(previousLabel){
                    case LOWER_O:
                        // We have an 'o'. Check if there is an 'c' before that.
                        if(getPreviousSymbol().getPreviousSymbol() == null){
                            this.choose(possibleSymbols_[1]);
                        }
                        else{
                            Classes previousPreviousClass = getPreviousSymbol().getPreviousSymbol().getClazz();
                            Labels previousPreviousLabel = getPreviousSymbol().getPreviousSymbol().getLabel();

                            switch(previousPreviousClass){
                                case LETTER:
                                    switch(previousPreviousLabel){
                                        case LOWER_C:
                                            // We have a cos. Choose it.
                                            this.choose(possibleSymbols_[0]);
                                            break;
                                        default:
                                            this.choose(possibleSymbols_[1]);
                                            break;
                                    }
                                    break;
                                case AMBIGUOUS:
                                    switch(previousPreviousLabel){
                                        case C_LIKE:
                                            // Don't choose yet, it is AMBIGUOUS...
                                            // unless you are force to do so...
                                            if(force) {
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
                        break;
                    default:
                        this.choose(possibleSymbols_[1]);
                        break;
                }
                break;
            case AMBIGUOUS:
                switch(previousLabel){
                    case CIRCLE:
                        // At this point it has no meaning to check the previousPrevious symbol
                        // since it can't be decisive for selecting cos ('o' is ambiguous) but also
                        // it cannot be decisive for 5 since it might be a sin.
                        // Don't choose yet, it is AMBIGUOUS...
                        // unless you are force to do so...
                        if(force) {
                            this.choose(possibleSymbols_[0]);
                        }
                        break;
                    default:
                        this.choose(possibleSymbols_[1]);
                        break;
                }
                break;
            default: // It is neither sin nor cos.
                this.choose(possibleSymbols_[1]);
                break;
        }
    }

    @Override
    public Labels getLabel () {
        if (chosenSymbol_ != this) {
            return chosenSymbol_.getLabel();
        }
        else{
            return Labels.S_LIKE;
        }
    }
}
