package org.hwer.engine.symbols.variables;

import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


public class LowerX extends Variable {

    public LowerX(TraceGroup traceGroup){
        super(traceGroup);
    }

    @Override
    public Labels getLabel () {
        return Labels.LOWER_X;
    }

    @Override
    public String toString () {
        return toString("x");
    }

}
