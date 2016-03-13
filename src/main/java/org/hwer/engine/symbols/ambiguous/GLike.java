package org.hwer.engine.symbols.ambiguous;


import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;

public class GLike extends Ambiguous {

    public GLike(TraceGroup traceGroup){
        super(traceGroup);

        SymbolFactory symbolFactory = SymbolFactory.getInstance();

        try{
            possibleSymbols_ = new Symbol[] {
                    symbolFactory.create(Labels.LOWER_G, traceGroup),
                    symbolFactory.create(Labels.NINE, traceGroup)
            };
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    @Override
    public Labels getLabel () {
        return Labels.G_LIKE;
    }
}