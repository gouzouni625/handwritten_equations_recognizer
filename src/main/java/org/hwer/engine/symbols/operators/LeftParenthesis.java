package org.hwer.engine.symbols.operators;

import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.util.ArrayList;
import java.util.List;


public class LeftParenthesis extends Operator {

    public LeftParenthesis(TraceGroup traceGroup){
        super(traceGroup);

        // LEFT_PARENTHESIS Operator objects do not accept children.
        children_ = new ArrayList<List<Symbol>>();
        childrenPositions_ = new ArgumentPosition[] {};
        childrenClass_ = new Classes[][] {};
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {};
    }

    @Override
    public ArgumentPosition relativePosition(Symbol symbol){
        // Get the relative position using the default implementation.
        ArgumentPosition relativePosition = super.relativePosition(symbol);

        // If the relative position is INSIDE, ABOVE_RIGHT or BELOW_RIGHT, continue as if it was RIGHT.
        // INSIDE: To avoid missing symbols too close to the left parenthesis.
        // ABOVE_RIGHT, BELOW_RIGHT: To avoid missing symbols drawn a little too higher or a little lower than they should.
        if(relativePosition == ArgumentPosition.INSIDE || relativePosition == ArgumentPosition.ABOVE_RIGHT ||
                relativePosition == ArgumentPosition.BELOW_RIGHT){
            relativePosition = ArgumentPosition.RIGHT;
        }

        return relativePosition;
    }

    @Override
    public Labels getLabel () {
        return Labels.LEFT_PARENTHESIS;
    }

    @Override
    public String toString(){
        return toString("(");
    }
}
