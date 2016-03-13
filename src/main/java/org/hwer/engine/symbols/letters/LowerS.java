package org.hwer.engine.symbols.letters;

import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


public class LowerS extends Letter {

    public LowerS(TraceGroup traceGroup){
        super(traceGroup);
    }

    @Override
    public Labels getLabel () {
        return Labels.LOWER_S;
    }

    @Override
    public String toString () {
        return toString("s");
    }
}
