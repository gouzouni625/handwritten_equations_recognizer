package org.hwer.engine.symbols.numbers;

import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


public class Zero extends Number {

    public Zero(TraceGroup traceGroup){
        super(traceGroup);
    }

    @Override
    public Labels getLabel () {
        return Labels.ZERO;
    }

    @Override
    public String toString () {
        return toString("0");
    }
}
