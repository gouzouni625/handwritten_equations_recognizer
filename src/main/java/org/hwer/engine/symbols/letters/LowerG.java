package org.hwer.engine.symbols.letters;

import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


public class LowerG extends Letter {

    public LowerG(TraceGroup traceGroup){
        super(traceGroup);
    }

    @Override
    public Labels getLabel () {
        return Labels.LOWER_G;
    }

    @Override
    public String toString () {
        return toString("g");
    }
}
