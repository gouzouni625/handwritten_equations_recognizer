package org.hwer.engine.symbols.ambiguous;


import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.utilities.traces.TraceGroup;


/**
 * @class HorizontalLine
 * @brief Implements the ambiguous Symbol of horizontal line
 */
public class HorizontalLine extends Ambiguous {
    /**
     * @brief Constructor
     *
     * @param traceGroup
     *     The TraceGroup of this Symbol
     */
    public HorizontalLine (TraceGroup traceGroup) {
        super(traceGroup);

        SymbolFactory symbolFactory = SymbolFactory.getInstance();

        try {
            possibleSymbols_ = new Symbol[] {
                symbolFactory.create(Labels.MINUS, traceGroup),
                symbolFactory.create(Labels.FRACTION_LINE, traceGroup)
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Returns the relative position of a given Symbol to this Symbol
     *
     * @param symbol
     *     The given Symbol
     *
     * @return The relative position of a given Symbol to this Symbol
     */
    @Override
    public ArgumentPosition relativePosition (Symbol symbol) {
        TraceGroup traceGroup = getTraceGroup();
        TraceGroup symbolTraceGroup = symbol.getTraceGroup();

        int xPosition;
        int yPosition;
        if (symbolTraceGroup.getCenterOfMass().x_ < traceGroup.getTopLeftCorner().x_) {
            xPosition = - 1;

            if (symbolTraceGroup.getCenterOfMass().y_ < Math.tan(Math.PI / 4) *
                (symbolTraceGroup.getCenterOfMass().x_ - traceGroup.getTopLeftCorner().x_) +
                traceGroup.getTopLeftCorner().y_) {
                yPosition = - 1;
            }
            else if (symbolTraceGroup.getCenterOfMass().y_ <= - Math.tan(Math.PI / 4) *
                (symbolTraceGroup.getCenterOfMass().x_ - traceGroup.getTopLeftCorner().x_) +
                traceGroup.getTopLeftCorner().y_) {
                yPosition = 0;
            }
            else {
                yPosition = 1;
            }
        }
        else if (symbolTraceGroup.getCenterOfMass().x_ <= traceGroup.getBottomRightCorner().x_) {
            xPosition = 0;

            if (symbolTraceGroup.getCenterOfMass().y_ < traceGroup.getBottomRightCorner().y_) {
                yPosition = - 1;
            }
            else if (symbolTraceGroup.getCenterOfMass().y_ <= traceGroup.getTopLeftCorner().y_) {
                yPosition = 0;
            }
            else {
                yPosition = 1;
            }
        }
        else {
            xPosition = 1;
            if (symbolTraceGroup.getCenterOfMass().y_ < - Math.tan(Math.PI / 4) *
                (symbolTraceGroup.getCenterOfMass().x_ - traceGroup.getBottomRightCorner().x_) +
                traceGroup.getBottomRightCorner().y_) {
                yPosition = - 1;
            }
            else if (symbolTraceGroup.getCenterOfMass().y_ <= Math.tan(Math.PI / 4) *
                (symbolTraceGroup.getCenterOfMass().x_ - traceGroup.getBottomRightCorner().x_) +
                traceGroup.getBottomRightCorner().y_) {
                yPosition = 0;
            }
            else {
                yPosition = 1;
            }
        }

        if (yPosition == 1) {

            if (xPosition == - 1) {
                return ArgumentPosition.ABOVE_LEFT;
            }
            else if (xPosition == 0) {
                return ArgumentPosition.ABOVE;
            }
            else {
                return ArgumentPosition.RIGHT;
            }

        }
        else if (yPosition == 0) {

            if (xPosition == - 1) {
                return ArgumentPosition.LEFT;
            }
            else if (xPosition == 0) {
                if (symbolTraceGroup.getArea() > traceGroup.getArea()) {
                    return ArgumentPosition.OUTSIDE;
                }
                else {
                    return ArgumentPosition.INSIDE;
                }
            }
            else {
                return ArgumentPosition.RIGHT;
            }

        }
        else {

            if (xPosition == - 1) {
                return ArgumentPosition.BELOW_LEFT;
            }
            else if (xPosition == 0) {
                return ArgumentPosition.BELOW;
            }
            else {
                return ArgumentPosition.RIGHT;
            }

        }
    }

    /**
     * @brief Use this method to give the ability to Symbols for internal changes and decisions
     *
     * @param force
     *     If there are any decisions to be made based on context, force this Symbol to take them
     */
    @Override
    public void reEvaluate (boolean force) {
        if (chosenSymbol_ != this) {
            return;
        }

        this.choose(possibleSymbols_[0]);
    }

    /**
     * @brief Returns the label of this Symbol
     *
     * @return The label of this Symbol
     */
    @Override
    public Labels getLabel () {
        if (chosenSymbol_ != this) {
            return chosenSymbol_.getLabel();
        }
        else {
            return Labels.HORIZONTAL_LINE;
        }
    }

}
