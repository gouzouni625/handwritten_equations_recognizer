package org.hwer.engine.symbols.operators;

import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.util.ArrayList;
import java.util.List;


public class Dot extends Operator {
    public Dot (TraceGroup traceGroup) {
        super(traceGroup);

        children_ = new ArrayList<List<Symbol>>();
        childrenPositions_ = new ArgumentPosition[]{};
        childrenClasses_ = new Classes[][]{};
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][]{};
    }

    @Override
    public ArgumentPosition relativePosition (Symbol symbol) {
        TraceGroup traceGroup = getTraceGroup();
        TraceGroup symbolTraceGroup = symbol.getTraceGroup();

        if (symbolTraceGroup.getCenterOfMass().x_ < traceGroup.getCenterOfMass().x_) {
            return ArgumentPosition.LEFT;
        }
        else {
            return ArgumentPosition.RIGHT;
        }
    }

    @Override
    public Labels getLabel () {
        return Labels.DOT;
    }

    @Override
    public String toString () {
        return toString(".");
    }

}
