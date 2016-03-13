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

    @Override
    public Labels getLabel () {
        return Labels.HORIZONTAL_LINE;
    }
}
