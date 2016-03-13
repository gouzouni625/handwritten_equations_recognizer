package org.hwer.engine.symbols.numbers;

import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


public class Four extends Number {

    public Four (TraceGroup traceGroup) {
        super(traceGroup);
    }

    @Override
    public Labels getLabel () {
        return Labels.FOUR;
    }

    @Override
    public String toString () {
        return toString("4");
    }
}
