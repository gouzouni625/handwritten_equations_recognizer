package org.hwer.engine.symbols.ambiguous;


import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.utilities.traces.TraceGroup;

public class GLike extends Ambiguous {

    public GLike(TraceGroup traceGroup){
        super(traceGroup);

        SymbolFactory symbolFactory = SymbolFactory.getInstance();

        try{
            possibleSymbols_ = new Symbol[] {
                    symbolFactory.create(Labels.LOWER_G, traceGroup),
                    symbolFactory.create(Labels.NINE, traceGroup)
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

        // Choose LOWER_G only in case of log.
        if(getPreviousSymbol() == null){
            this.choose(possibleSymbols_[1]);
        }
        else{
            Classes previousClass = getPreviousSymbol().getClazz();

            Labels previousLabel = getPreviousSymbol().getLabel();

            switch(previousClass){
                case LETTER:
                    switch(previousLabel){
                        case LOWER_O:
                            this.choose(possibleSymbols_[0]);
                            break;
                        default:
                            this.choose(possibleSymbols_[1]);
                            break;
                    }
                    break;
                case AMBIGUOUS:
                    switch(previousLabel){
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
        return Labels.G_LIKE;
    }
}
