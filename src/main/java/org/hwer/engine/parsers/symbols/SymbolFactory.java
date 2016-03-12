package org.hwer.engine.parsers.symbols;

import org.hwer.engine.utilities.traces.TraceGroup;

import java.lang.reflect.InvocationTargetException;

/**
 * @class SymbolFactory
 * @brief A class used to create Symbol objects.
 * <p>
 * This class also contains all the needed variables and methods to make the transition of data between the equation
 * partitioner and the parser.
 */
public class SymbolFactory {
    /**
     * @param traceGroup The TraceGroup of the Symbol to be created.
     * @param label      The label of the Symbol to be created.
     * @return Returns the created Symbol.
     * @brief Creates a Symbol object for a given label.
     * <p>
     * This label is the label used by the equation partitioner and the neural network, thus an integer.
     */
    public static Symbol createByLabel (TraceGroup traceGroup, int label) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Labels labelObject = Labels.getByLabel(label);

        if (labelObject == null) {
            throw new NullPointerException();
        }

        return (Symbol) labelObject.getClazz().getConstructor(TraceGroup.class).newInstance(traceGroup);
    }

    public enum Labels {
        LABEL_CIRCLE(0, org.hwer.engine.parsers.symbols.ambiguous.Circle.class),
        LABEL_ONE(1, org.hwer.engine.parsers.symbols.numbers.One.class),
        LABEL_TWO(2, org.hwer.engine.parsers.symbols.numbers.Two.class),
        LABEL_THREE(3, org.hwer.engine.parsers.symbols.numbers.Three.class),
        LABEL_FOUR(4, org.hwer.engine.parsers.symbols.numbers.Four.class),
        LABEL_S_LIKE(5, org.hwer.engine.parsers.symbols.ambiguous.SLike.class),
        LABEL_SIX(6, org.hwer.engine.parsers.symbols.numbers.Six.class),
        LABEL_SEVEN(7, org.hwer.engine.parsers.symbols.numbers.Seven.class),
        LABEL_EIGHT(8, org.hwer.engine.parsers.symbols.numbers.Eight.class),
        LABEL_G_LIKE(9, org.hwer.engine.parsers.symbols.ambiguous.GLike.class),
        LABEL_PLUS(10, org.hwer.engine.parsers.symbols.operators.Plus.class),
        LABEL_EQUALS(11, org.hwer.engine.parsers.symbols.operators.Equals.class),
        LABEL_LOWER_X(12, org.hwer.engine.parsers.symbols.letters.LowerX.class),
        LABEL_LOWER_Y(13, org.hwer.engine.parsers.symbols.letters.LowerY.class),
        LABEL_HORIZONTAL_LINE(14, org.hwer.engine.parsers.symbols.ambiguous.HorizontalLine.class),
        LABEL_SQRT(15, org.hwer.engine.parsers.symbols.operators.SquareRoot.class),
        LABEL_C_LIKE(16, org.hwer.engine.parsers.symbols.ambiguous.CLike.class),
        LABEL_RIGHT_PARENTHESIS(17, org.hwer.engine.parsers.symbols.operators.RightParenthesis.class),
        LABEL_GREATER_THAN(18, org.hwer.engine.parsers.symbols.operators.GreaterThan.class),
        LABEL_LESS_THAN(19, org.hwer.engine.parsers.symbols.operators.LessThan.class),
        LABEL_VERTICAL_LINE(20, org.hwer.engine.parsers.symbols.ambiguous.VerticalLine.class),
        LABEL_LOWER_A(21, org.hwer.engine.parsers.symbols.letters.LowerA.class),
        LABEL_LOWER_E(22, org.hwer.engine.parsers.symbols.letters.LowerE.class),
        LABEL_LOWER_I(23, org.hwer.engine.parsers.symbols.letters.LowerI.class),
        LABEL_LOWER_L(24, org.hwer.engine.parsers.symbols.letters.LowerL.class),
        LABEL_LOWER_N(25, org.hwer.engine.parsers.symbols.letters.LowerN.class),
        LABEL_LOWER_T(26, org.hwer.engine.parsers.symbols.letters.LowerT.class);

        Labels (int label, Class<?> clazz) {
            label_ = label;

            clazz_ = clazz;
        }

        public static Labels getByLabel (int label) {
            switch (label) {
                case 0:
                    return LABEL_CIRCLE;
                case 1:
                    return LABEL_ONE;
                case 2:
                    return LABEL_TWO;
                case 3:
                    return LABEL_THREE;
                case 4:
                    return LABEL_FOUR;
                case 5:
                    return LABEL_S_LIKE;
                case 6:
                    return LABEL_SIX;
                case 7:
                    return LABEL_SEVEN;
                case 8:
                    return LABEL_EIGHT;
                case 9:
                    return LABEL_G_LIKE;
                case 10:
                    return LABEL_PLUS;
                case 11:
                    return LABEL_EQUALS;
                case 12:
                    return LABEL_LOWER_X;
                case 13:
                    return LABEL_LOWER_Y;
                case 14:
                    return LABEL_HORIZONTAL_LINE;
                case 15:
                    return LABEL_SQRT;
                case 16:
                    return LABEL_C_LIKE;
                case 17:
                    return LABEL_RIGHT_PARENTHESIS;
                case 18:
                    return LABEL_GREATER_THAN;
                case 19:
                    return LABEL_LESS_THAN;
                case 20:
                    return LABEL_VERTICAL_LINE;
                case 21:
                    return LABEL_LOWER_A;
                case 22:
                    return LABEL_LOWER_E;
                case 23:
                    return LABEL_LOWER_I;
                case 24:
                    return LABEL_LOWER_L;
                case 25:
                    return LABEL_LOWER_N;
                case 26:
                    return LABEL_LOWER_T;
                default:
                    return null;
            }
        }

        public int getLabel () {
            return label_;
        }

        public Class<?> getClazz () {
            return clazz_;
        }

        private int label_;

        private Class<?> clazz_;

    }

}
