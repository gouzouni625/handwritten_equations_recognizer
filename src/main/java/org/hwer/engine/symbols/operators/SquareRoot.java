package org.hwer.engine.symbols.operators;

import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class SquareRoot extends Operator {

    public SquareRoot(TraceGroup traceGroup){
        super(traceGroup);

        // SQRT operator accept children in positions INSIDE and ABOVE_RIGHT.
        children_ = new ArrayList<List<Symbol>>();
        children_.add(new ArrayList<Symbol>());
        children_.add(new ArrayList<Symbol>());
        childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.INSIDE, ArgumentPosition.ABOVE_RIGHT};
        // Symbols accepted as children in ABOVE position: Number, Operator, Letter, UnrecognizedSymbol.
        // Symbols accepted as children in ABOVE_RIGHT position: Number, Operator, Letter, UnrecognizedSymbol.
        childrenClasses_ = new Classes[][] {{Classes.NUMBER, Classes.OPERATOR, Classes.LETTER,
                Classes.AMBIGUOUS},
                {Classes.NUMBER, Classes.OPERATOR, Classes.LETTER,
                        Classes.AMBIGUOUS}};
        // Use sizeChildAcceptanceCriterion for accepting a child. That means that, when drawing an equation, a child
        // of an SQRT Symbol should have, at most, half the size of the SQRT Symbol.
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{sizeChildAcceptanceCriterion,
                sizeChildAcceptanceCriterion,
                sizeChildAcceptanceCriterion,
                sizeChildAcceptanceCriterion},
                {sizeChildAcceptanceCriterion,
                        sizeChildAcceptanceCriterion,
                        sizeChildAcceptanceCriterion,
                        sizeChildAcceptanceCriterion}};
    }

    @Override
    public ArgumentPosition relativePosition(Symbol symbol){
        // Get the relative position using the default implementation.
        ArgumentPosition relativePosition = super.relativePosition(symbol);

        // To make things easier, if the relative position is BELOW_RIGHT, continue as if it was RIGHT. This is done to
        // avoid missing a next symbol that is drawn a little below and get recognized as BELOW_RIGHT of the sqrt symbol
        // instead of RIGHT.
        if(relativePosition == ArgumentPosition.BELOW_RIGHT){
            relativePosition = ArgumentPosition.RIGHT;
        }

        return relativePosition;
    }

    @Override
    public String clearString(String string){
        String result = string;

        for(ArgumentPosition argumentPosition : childrenPositions_){
            result = result.replaceAll(Pattern.quote("^{") + argumentPosition + Pattern.quote("}"), "");
            result = result.replaceAll(Pattern.quote("{") + argumentPosition + Pattern.quote("}"), "{}");
        }

        return result;
    }

    @Override
    public Labels getLabel () {
        return Labels.SQUARE_ROOT;
    }

    @Override
    public String toString(){
        return toString("\\sqrt{" + ArgumentPosition.INSIDE + "}^{" + ArgumentPosition.ABOVE_RIGHT + "}");
    }
}
