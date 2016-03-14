package org.hwer.engine.symbols.ambiguous;

import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


public class HorizontalLine extends Ambiguous {

    public HorizontalLine(TraceGroup traceGroup){
        super(traceGroup);

        SymbolFactory symbolFactory = SymbolFactory.getInstance();

        try {
            possibleSymbols_ = new Symbol[] {
                    symbolFactory.create(Labels.MINUS, traceGroup),
                    symbolFactory.create(Labels.FRACTION_LINE, traceGroup)
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
    public void reEvaluate () {
        if (chosenSymbol_ != this) {
            return;
        }

        // Came here means that no child has been assigned to any of the possible symbols.
        // That is because, if at least 1 child had been assigned in setArgument method,
        // then the symbol accepting the child would have become the chosen symbol. So, now,
        // choose the symbol that accepts no children. In our case, choose MINUS.
        this.choose(possibleSymbols_[0]);
    }

    @Override
    public Labels getLabel () {
        if (chosenSymbol_ != this) {
            return chosenSymbol_.getLabel();
        }
        else{
            return Labels.HORIZONTAL_LINE;
        }
    }
}
