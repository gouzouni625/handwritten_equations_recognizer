package org.hwer.engine.symbols.numbers;

import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


public class Six extends Number {

    public Six(TraceGroup traceGroup){
        super(traceGroup);
    }

    @Override
    public Labels getLabel () {
        return Labels.SIX;
    }

    @Override
    public String toString () {
        return toString("6");
    }

}
