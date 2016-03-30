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
 * @brief Implements a Symbol creation mechanism utilizing the Singleton and Factory design patterns
 */
public class SymbolFactory {
    /**
     * @brief Private Constructor to prevent this class from being instantiated
     */
    private SymbolFactory () {
    }

    /**
     * @brief Returns the SymbolFactory singleton
     *
     * @return The SymbolFactory singleton
     */
    public static SymbolFactory getInstance () {
        return instance_;
    }

    /**
     * @brief Creates a new Symbol defined by a given label
     *
     * @param label
     *     The label that defines what Symbol will be created
     * @param traceGroup
     *     The TraceGroup of the Symbol to be created
     *
     * @return The created Symbol
     */
    public Symbol create (Labels label, TraceGroup traceGroup) throws NoSuchMethodException,
        IllegalAccessException, InvocationTargetException, InstantiationException {
        return (Symbol) label.getClazz().getConstructor(TraceGroup.class).newInstance(traceGroup);
    }

    /**
     * @interface SymbolClass
     * @brief Defines the API to be implemented by classes that will represent a group of Symbols
     */
    public interface SymbolClass {
        /**
         * @brief Returns the clazz of this SymbolClass
         *
         * @return The clazz of this Symbol
         */
        Classes getClazz ();

        /**
         * @brief Returns a string representation of this SymbolClass
         *        The string representation can vary based on the Symbol of this SymbolClass that
         *        this method is called for.
         *
         * @param symbolString
         *     A string provided by the Symbol of this SymbolClass that this method is called for
         *
         * @return The string representation of this SymbolClass
         */
        String toString (String symbolString);
    }

    /**
     * @class Classes
     * @brief Defines the Symbol classes that this engine recognizes
     */
    public enum Classes {
        NUMBER,
        LETTER,
        VARIABLE,
        OPERATOR,
        AMBIGUOUS
    }

    /**
     * @class Labels
     * @brief Defines the Symbols that this engine recognizes
     */
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

        /**
         * @brief Constructor
         *
         * @param clazz
         *     The java class that implements this Symbol
         */
        Labels (Class<?> clazz) {
            clazz_ = clazz;
        }

        /**
         * @brief Returns the java class that implements this Symbol
         *
         * @return The java class that implements this Symbol
         */
        public Class<?> getClazz () {
            return clazz_;
        }

        private Class<?> clazz_; //!< The java class that implements this Symbol

    }

    private static SymbolFactory instance_ = new SymbolFactory(); //!< The singleton SymbolFactory

}
