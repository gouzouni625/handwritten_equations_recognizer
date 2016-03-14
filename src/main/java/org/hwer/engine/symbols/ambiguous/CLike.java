package org.hwer.engine.symbols.ambiguous;

import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.utilities.traces.TraceGroup;


public class CLike extends Ambiguous {

    public CLike(TraceGroup traceGroup){
        super(traceGroup);

        SymbolFactory symbolFactory = SymbolFactory.getInstance();

        try{
            possibleSymbols_ = new Symbol[] {
                    symbolFactory.create(Labels.LOWER_C, traceGroup),
                    symbolFactory.create(Labels.LEFT_PARENTHESIS, traceGroup)
            };
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    /**
     * @brief Chooses the type of this UnrecognizedSymbol.
     */
    @Override
    public void reEvaluate () {
        if (chosenSymbol_ != this) {
            return;
        }

        // Came here means that no child has been assigned to any of the possible symbols.
        // That is because, if at least 1 child had been assigned in setArgument method,
        // then the symbol accepting the child would have become the chosen symbol. Choose
        // LOWER_C only in cases of cos or cot.
        if(getNextSymbol() == null){
            this.choose(possibleSymbols_[1]);
        }
        else{
            Classes nextClass = getNextSymbol().getClazz();

            Labels nextLabel = getNextSymbol().getLabel();

            switch(nextClass){
                case LETTER:
                    switch(nextLabel){
                        case LOWER_O:
                            this.choose(possibleSymbols_[0]);
                            break;
                        default:
                            this.choose(possibleSymbols_[1]);
                            break;
                    }
                    break;
                case AMBIGUOUS:
                    switch(nextLabel){
                        case CIRCLE: // Don't choose yet, it is AMBIGUOUS...
                            // this.choose(possibleSymbols_[0]);
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
        return Labels.C_LIKE;
    }
}
