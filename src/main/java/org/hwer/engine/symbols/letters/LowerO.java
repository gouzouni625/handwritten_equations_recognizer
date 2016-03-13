package org.hwer.engine.symbols.letters;

import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


public class LowerO extends Letter {

    public LowerO(TraceGroup traceGroup){
        super(traceGroup);
    }

    @Override
    public Labels getLabel () {
        return Labels.LOWER_O;
    }

    @Override
    public String toString () {
        return toString("o");
    }
}
