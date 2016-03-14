package org.hwer.engine.symbols.variables;

import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;

import java.util.ArrayList;
import java.util.List;


public abstract  class Variable extends Symbol{
    /**
     * @param traceGroup The TraceGroup of this Letter.
     * @brief Constructor.
     */
    public Variable (TraceGroup traceGroup) {
        super(traceGroup);

        children_ = new ArrayList<List<Symbol>>();
        children_.add(new ArrayList<Symbol>());
        children_.add(new ArrayList<Symbol>());
        // Accept children only on ABOVE_RIGHT(exponent) and BELOW_RIGHT(index) position.
        childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.ABOVE_RIGHT, ArgumentPosition.BELOW_RIGHT};
        // Accept as exponent a Number, or another Letter, or an Operator or an UnrecognizedSymbol.
        // Accept as index a Number or a Letter.
        childrenClasses_ = new Classes[][] {{Classes.NUMBER, Classes.LETTER, Classes.OPERATOR,
                Classes.AMBIGUOUS}, {Classes.NUMBER, Classes.LETTER}};
        // - Use sizeChildAcceptanceCriterion for accepting a Number in ABOVE_RIGHT position. That means that, when
        //     drawing an equation, a Number, as an exponent, should have, at max, half the size of the base Letter.
        // - Use sizeChildAcceptanceCriterion for accepting a Letter in ABOVE_RIGHT position. That means that, when drawing
        //     an equation, a Letter, as an exponent, should have, at max, half the size of the base Letter.
        // - Use widthSizeExceptSQRTFractionLine for accepting an Operator in ABOVE_RIGHT position. That means that, when
        //     drawing an equation, an Operator, as an exponent, should have, at max, half the width of the base Letter.
        //     This doesn't apply for the square root and fraction line Symbol. The exclusion of these two is done to
        //     avoid the case of long fractions or square roots not being accepted as exponents to a Letter(e.g.
        //     5^{sqrt{5x^{2} + 2x + 6}}). The size-width criterion is done avoid the case where x_{x}=5 gets recognized as
        //     x_{x=}. In this situation, '=' will be bigger, or, at least, not twice as small as 'x', so 'x' will not
        //     accept '=' as a child.
        // - Use sizeWidthChildAcceptanceCriterion for accepting an UnrecognizedSymbol in ABOVE_RIGHT position. That means
        //     that, when drawing an equation, an UnrecognizedSymbol, as an exponent, should have, at max, half the width
        //     and half the size of the base Letter.
        // --------------------------------------------------------------------------------------------------------------
        // - Use sizeChildAcceptanceCriterion for accepting a Number in BELOW_RIGHT position. That means that, when drawing
        //     an equation, a Number, as an index, should have, at max, half the size of the base Letter.
        // - Use sizeChildAcceptanceCriterion for accepting a Letter in BELOW_RIGHT position. That means that, when drawing
        //     an equation, a Letter, as an index, should have, at max, half the size of the base Letter.
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{sizeChildAcceptanceCriterion,
                sizeChildAcceptanceCriterion,
                widthSizeExceptSQRTFractionLine,
                sizeWidthChildAcceptanceCriterion},
                {sizeChildAcceptanceCriterion,
                        sizeChildAcceptanceCriterion}};
    }

    /**
     *  @brief Finds the relative position between this Symbol and a given Symbol.
     *
     *  Override the default implementation to treat some special cases.
     *
     *  @param symbol The given Symbol.
     *
     *  @return Returns the relative position between this Symbol and the given one.
     */
    @Override
    public ArgumentPosition relativePosition(Symbol symbol){
        // Get the relative position from the default implementation.
        ArgumentPosition relativePosition = super.relativePosition(symbol);

        // To make things easier, if the relative position is ABOVE, continue as if it was ABOVE_RIGHT. This is done to avoid
        // missing an exponent that is drawn a little to the left, thus ABOVE this Letter.
        if(relativePosition == ArgumentPosition.ABOVE){
            relativePosition = ArgumentPosition.ABOVE_RIGHT;
        }
        // To make things easier, if the relative position is BELOW, continue as if it was BELOW_RIGHT. This is done to avoid
        // missing an index Symbol that is drawn a little to the left, thus BELOW this Letter.
        else if(relativePosition == ArgumentPosition.BELOW){
            relativePosition = ArgumentPosition.BELOW_RIGHT;
        }

        return relativePosition;
    }

    public String toString(String symbolString){
        return (symbolString + "^{" + ArgumentPosition.ABOVE_RIGHT + "}_{" + ArgumentPosition.BELOW_RIGHT + "}");
    }

    public Classes getClazz(){
        return Classes.VARIABLE;
    }

}
