package org.hwer.engine.symbols.operators;

import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.util.ArrayList;
import java.util.List;


public class Plus extends Operator {

    public Plus(TraceGroup traceGroup){
        super(traceGroup);

        children_ = new ArrayList<List<Symbol>>();
        childrenPositions_ = new ArgumentPosition[] {};
        childrenClass_ = new Classes[][] {};
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {};
    }

    @Override
    public ArgumentPosition relativePosition(Symbol symbol){
        // Get the relative position from the default implementation.
        ArgumentPosition relativePosition = super.relativePosition(symbol);

        // To make things easier, if the relative position is ABOVE_RIGHT or BELOW_RIGHT, continue as if it was RIGHT.
        // This is done to avoid missing a next symbol that it is drawn a little higher of a little lower than
        // this Symbol.
        if(relativePosition == ArgumentPosition.ABOVE_RIGHT || relativePosition == ArgumentPosition.BELOW_RIGHT){
            relativePosition = ArgumentPosition.RIGHT;
        }

        return relativePosition;
    }

    @Override
    public Labels getLabel () {
        return Labels.PLUS;
    }

    @Override
    public String toString(){
        return toString("+");
    }

}
