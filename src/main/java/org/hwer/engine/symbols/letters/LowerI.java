package org.hwer.engine.symbols.letters;

import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;

public class LowerI extends Letter {

    public LowerI(TraceGroup traceGroup){
        super(traceGroup, true);
    }

    @Override
    public Labels getLabel () {
        return Labels.LOWER_I;
    }

    @Override
    public String toString () {
        return toString("i");
    }
}
