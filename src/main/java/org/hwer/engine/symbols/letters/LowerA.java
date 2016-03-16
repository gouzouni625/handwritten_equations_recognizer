package org.hwer.engine.symbols.letters;

import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


public class LowerA extends Letter {

    public LowerA(TraceGroup traceGroup){
        super(traceGroup, true);
    }

    @Override
    public Labels getLabel () {
        return Labels.LOWER_A;
    }

    @Override
    public String toString () {
        return toString("a");
    }
}
