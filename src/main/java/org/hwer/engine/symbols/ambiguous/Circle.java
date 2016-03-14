package org.hwer.engine.symbols.ambiguous;

import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


public class Circle extends Ambiguous {

    public Circle(TraceGroup traceGroup){
        super(traceGroup);

        SymbolFactory symbolFactory = SymbolFactory.getInstance();

        try{
            possibleSymbols_ = new Symbol[]{
                    symbolFactory.create(Labels.LOWER_O, traceGroup),
                    symbolFactory.create(Labels.ZERO, traceGroup)
            };
        }
        catch (Exception exception){
            exception.printStackTrace();
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

        // Came here means that no child has been assigned to any of the possible symbols.
        // That is because, if at least 1 child had been assigned in setArgument method,
        // then the symbol accepting the child would have become the chosen symbol. Choose
        // LOWER_O only in cases of cos, cot or log.

        // If there is no symbol before or no symbol after, then cos, cot or log cannot be
        // created and so, choose ZERO.
        if(getPreviousSymbol() == null || getNextSymbol() == null){
            this.choose(possibleSymbols_[1]);
        }
        else{
            Classes previousClass = getPreviousSymbol().getClazz();
            Classes nextClass = getNextSymbol().getClazz();

            Labels previousLabel = getPreviousSymbol().getLabel();
            Labels nextLabel = getNextSymbol().getLabel();

            switch(previousClass){
                case LETTER:
                    switch(previousLabel){
                        case LOWER_C:
                            switch(nextClass){
                                case LETTER:
                                    switch(nextLabel){
                                        case LOWER_S:
                                        case LOWER_T:
                                            this.choose(possibleSymbols_[0]);
                                            break;
                                        default:
                                            this.choose(possibleSymbols_[1]);
                                            break;
                                    }
                                    break;
                                case AMBIGUOUS:
                                    switch(nextLabel){
                                        case S_LIKE:
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
                            break;
                        case LOWER_L:
                            switch(nextClass){
                                case LETTER:
                                    switch(nextLabel){
                                        case LOWER_G:
                                            this.choose(possibleSymbols_[0]);
                                            break;
                                        default:
                                            this.choose(possibleSymbols_[1]);
                                            break;
                                    }
                                    break;
                                case AMBIGUOUS:
                                    switch(nextLabel){
                                        case G_LIKE:
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
                            break;
                        default:
                            this.choose(possibleSymbols_[1]);
                            break;
                    }
                    break;
                case AMBIGUOUS:
                    switch(previousLabel){
                        case C_LIKE:
                            switch(nextClass){
                                case LETTER:
                                    switch(nextLabel){
                                        case LOWER_S:
                                        case LOWER_T:
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
                                    switch(nextLabel){
                                        case S_LIKE:
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
                            break;
                        case VERTICAL_LINE:
                            switch(nextClass){
                                case LETTER:
                                    switch(nextLabel){
                                        case LOWER_G:
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
                                    switch(nextLabel){
                                        case G_LIKE:
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

    @Override
    public Labels getLabel () {
        if (chosenSymbol_ != this) {
            return chosenSymbol_.getLabel();
        }
        else{
            return Labels.CIRCLE;
        }
    }
}
