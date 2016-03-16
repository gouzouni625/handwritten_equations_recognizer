package org.hwer.engine.symbols.letters;

import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.util.ArrayList;
import java.util.List;


public class LowerE extends Letter {

    public LowerE(TraceGroup traceGroup){
        super(traceGroup, false);

        children_ = new ArrayList<List<Symbol>>();
        children_.add(new ArrayList<Symbol>());

        // Accept children only on ABOVE_RIGHT(exponent).
        childrenPositions_ = new ArgumentPosition[]
                {
                    ArgumentPosition.ABOVE_RIGHT
                };

        childrenClasses_ = new Classes[][]
                {
                        {
                                Classes.NUMBER,
                                Classes.LETTER,
                                Classes.OPERATOR,
                                Classes.AMBIGUOUS,
                                Classes.VARIABLE
                        }
                };

        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][]
                {
                        {
                                sizeChildAcceptanceCriterion,
                                sizeChildAcceptanceCriterion,
                                widthSizeExceptSQRTFractionLine,
                                sizeWidthChildAcceptanceCriterion,
                                sizeChildAcceptanceCriterion
                        }
                };
    }

    @Override
    public Labels getLabel () {
        return Labels.LOWER_E;
    }

    @Override
    public String toString () {
        return toString("e^{" + ArgumentPosition.ABOVE_RIGHT + "}");
    }
}
