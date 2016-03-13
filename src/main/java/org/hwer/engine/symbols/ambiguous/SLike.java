package org.hwer.engine.symbols.ambiguous;


import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory;
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

    @Override
    public Labels getLabel () {
        return Labels.S_LIKE;
    }
}
