package org.hwer.engine.symbols.variables;

import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


public class LowerY extends Variable {

    public LowerY(TraceGroup traceGroup){
        super(traceGroup);
    }

    @Override
    public Labels getLabel () {
        return Labels.LOWER_Y;
    }

    @Override
    public String toString () {
        return toString("y");
    }
}
