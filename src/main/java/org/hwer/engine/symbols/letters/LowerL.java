package org.hwer.engine.symbols.letters;

import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


public class LowerL extends Letter {

    public LowerL(TraceGroup traceGroup){
        super(traceGroup, true);
    }

    @Override
    public Labels getLabel () {
        return Labels.LOWER_L;
    }

    @Override
    public String toString () {
        return toString("l");
    }
}
