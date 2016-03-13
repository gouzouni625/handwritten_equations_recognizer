package org.hwer.engine.symbols.ambiguous;

import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory;
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

    @Override
    public Labels getLabel () {
        return Labels.VERTICAL_LINE;
    }
}
