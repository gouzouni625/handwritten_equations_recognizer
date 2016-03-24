package org.hwer.engine.symbols;

import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.engine.symbols.numbers.*;
import org.hwer.engine.symbols.letters.*;
import org.hwer.engine.symbols.operators.*;
import org.hwer.engine.symbols.ambiguous.*;
import org.hwer.engine.symbols.variables.*;

import java.lang.reflect.InvocationTargetException;

/**
 * @class SymbolFactory
 * @brief A class used to create Symbol objects.
 * <p>
 * This class also contains all the needed variables and methods to make the transition of data between the equation
 * partitioner and the parser.
 */
public class SymbolFactory {

    private SymbolFactory(){}

    public static SymbolFactory getInstance(){
        return instance_;
    }

    /**
     * @param traceGroup The TraceGroup of the Symbol to be created.
     * @param label      The label of the Symbol to be created.
     * @return Returns the created Symbol.
     * @brief Creates a Symbol object for a given label.
     * <p>
     * This label is the label used by the equation partitioner and the neural network, thus an integer.
     */
    public Symbol create (Labels label, TraceGroup traceGroup) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        return (Symbol) label.getClazz().getConstructor(TraceGroup.class).newInstance(traceGroup);
    }

    public interface SymbolClass{
        Classes getClazz();

        String toString(String SymbolString);
    }

    public enum Classes {
        NUMBER,
        LETTER,
        VARIABLE,
        OPERATOR,
        AMBIGUOUS
    }

    public enum Labels {
        ZERO(Zero.class),
        ONE(One.class),
        TWO(Two.class),
        THREE(Three.class),
        FOUR(Four.class),
        FIVE(Five.class),
        SIX(Six.class),
        SEVEN(Seven.class),
        EIGHT(Eight.class),
        NINE(Nine.class),

        LOWER_A(LowerA.class),
        LOWER_C(LowerC.class),
        LOWER_E(LowerE.class),
        LOWER_G(LowerG.class),
        LOWER_I(LowerI.class),
        LOWER_L(LowerL.class),
        LOWER_N(LowerN.class),
        LOWER_O(LowerO.class),
        LOWER_S(LowerS.class),
        LOWER_T(LowerT.class),
        LOWER_X(LowerX.class),
        LOWER_Y(LowerY.class),

        PLUS(Plus.class),
        EQUALS(Equals.class),
        MINUS(Minus.class),
        SQUARE_ROOT(SquareRoot.class),
        LEFT_PARENTHESIS(LeftParenthesis.class),
        RIGHT_PARENTHESIS(RightParenthesis.class),
        FRACTION_LINE(FractionLine.class),
        GREATER_THAN(GreaterThan.class),
        LESS_THAN(LessThan.class),
        DOT(Dot.class),

        S_LIKE(SLike.class),
        G_LIKE(GLike.class),
        C_LIKE(CLike.class),
        CIRCLE(Circle.class),
        HORIZONTAL_LINE(HorizontalLine.class),
        VERTICAL_LINE(VerticalLine.class);

        Labels (Class<?> clazz) {
            clazz_ = clazz;
        }

        public Class<?> getClazz () {
            return clazz_;
        }

        private Class<?> clazz_;

    }

    private static SymbolFactory instance_ = new SymbolFactory();

}
