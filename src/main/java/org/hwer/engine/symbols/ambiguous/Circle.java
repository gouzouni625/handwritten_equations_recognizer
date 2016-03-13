package org.hwer.engine.symbols.ambiguous;

import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


public class Circle extends Ambiguous {

    public Circle(TraceGroup traceGroup){
        super(traceGroup);

        SymbolFactory symbolFactory = SymbolFactory.getInstance();

        try{
            possibleSymbols_ = new Symbol[]{
                    symbolFactory.create(Labels.LOWER_O, traceGroup),
                    symbolFactory.create(Labels.ZERO, traceGroup)
            };
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }

    @Override
    public Labels getLabel () {
        return Labels.CIRCLE;
    }
}
