package org.hwer.engine.symbols.numbers;

import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


public class One extends Number {

    public One(TraceGroup traceGroup){
        super(traceGroup);
    }

    @Override
    public Labels getLabel () {
        return Labels.ONE;
    }

    @Override
    public String toString () {
        return toString("1");
    }

}
