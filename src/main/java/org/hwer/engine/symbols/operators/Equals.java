package org.hwer.engine.symbols.operators;


import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * @class Equals
 * @brief Implements the equals sign
 */
public class Equals extends Operator {
    /**
     * @brief Constructor
     *
     * @param traceGroup
     *     The TraceGroup of this Symbol
     */
    public Equals (TraceGroup traceGroup) {
        super(traceGroup);

        children_ = new ArrayList<List<Symbol>>();
        childrenPositions_ = new ArgumentPosition[] {};
        childrenClasses_ = new Classes[][] {};
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {};
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
                (symbolTraceGroup.getCenterOfMass().x_ - traceGroup.getBottomLeftCorner().x_) +
                traceGroup.getBottomLeftCorner().y_) {
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
                (symbolTraceGroup.getCenterOfMass().x_ - traceGroup.getTopRightCorner().x_) +
                traceGroup.getTopRightCorner().y_) {
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
     * @brief Returns the label of this Symbol
     *
     * @return The label of this Symbol
     */
    @Override
    public Labels getLabel () {
        return Labels.EQUALS;
    }

    /**
     * @brief Returns a string representation of this Symbol
     *
     * @return A string representation of this Symbol
     */
    @Override
    public String toString () {
        return toString("=");
    }

}
