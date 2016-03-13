package org.hwer.engine.symbols.numbers;

import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


public class Three extends Number {

    public Three (TraceGroup traceGroup) {
        super(traceGroup);
    }

    @Override
    public Labels getLabel () {
        return Labels.THREE;
    }

    @Override
    public String toString () {
        return toString("3");
    }
}
