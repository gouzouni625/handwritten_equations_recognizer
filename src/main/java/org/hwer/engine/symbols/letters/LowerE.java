package org.hwer.engine.symbols.letters;

import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


public class LowerE extends Letter {

    public LowerE(TraceGroup traceGroup){
        super(traceGroup);
    }

    @Override
    public Labels getLabel () {
        return Labels.LOWER_E;
    }

    @Override
    public String toString () {
        return toString("e");
    }
}
