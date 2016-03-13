package org.hwer.engine.symbols.operators;

import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.util.ArrayList;
import java.util.List;

public class RightParenthesis extends Operator {

    public RightParenthesis(TraceGroup traceGroup){
        super(traceGroup);

        // RIGHT_PARENTHESIS Operator objects accept children ABOVE_RIGHT.
        children_ = new ArrayList<List<Symbol>>();
        children_.add(new ArrayList<Symbol>());
        childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.ABOVE_RIGHT};
        // SymbolClass Accepted as ABOVE_RIGHT child: Number, Operator, Letter, UnrecognizedSymbol.
        childrenClass_ = new Classes[][] {{Classes.NUMBER, Classes.LETTER, Classes.OPERATOR,
                Classes.AMBIGUOUS}};
        // Use no criteria for accepting any child.
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{allChildAcceptanceCriterion,
                allChildAcceptanceCriterion,
                allChildAcceptanceCriterion,
                allChildAcceptanceCriterion}};
    }

    @Override
    public ArgumentPosition relativePosition(Symbol symbol){
        // Get the relative position using the default implementation.
        ArgumentPosition relativePosition = super.relativePosition(symbol);

        // If the relative position is INSIDE or BELOW_RIGHT, change it to LEFT.
        // This case will never be used in the current implementation since all the symbols are processed from left to
        // right.
        if(relativePosition == ArgumentPosition.INSIDE || relativePosition == ArgumentPosition.BELOW_RIGHT){
            relativePosition = ArgumentPosition.LEFT;
        }
        // If the relative position is ABOVE, change it to ABOVE_RIGHT. This is to avoid missing an exponent that is
        // drawn a little more to the left than it should.
        else if(relativePosition == ArgumentPosition.ABOVE){
            relativePosition = ArgumentPosition.ABOVE_RIGHT;
        }

        return relativePosition;
    }

    @Override
    public Labels getLabel () {
        return Labels.RIGHT_PARENTHESIS;
    }

    @Override
    public String toString(){
        return toString(")^{" + ArgumentPosition.ABOVE_RIGHT + "}");
    }
}
