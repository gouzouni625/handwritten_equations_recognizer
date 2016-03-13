package org.hwer.engine.symbols.letters;

import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


public class LowerC extends Letter {

    public LowerC(TraceGroup traceGroup){
        super(traceGroup);
    }

    @Override
    public Labels getLabel () {
        return Labels.LOWER_C;
    }

    @Override
    public String toString () {
        return toString("c");
    }
}
