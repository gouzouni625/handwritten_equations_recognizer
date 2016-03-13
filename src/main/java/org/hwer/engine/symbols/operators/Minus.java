package org.hwer.engine.symbols.operators;

import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.util.ArrayList;
import java.util.List;


public class Minus extends Operator {

    public Minus(TraceGroup traceGroup){
        super(traceGroup);

        children_ = new ArrayList<List<Symbol>>();
        childrenPositions_ = new ArgumentPosition[] {};
        childrenClass_ = new Classes[][] {};
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {};
    }

    @Override
    public ArgumentPosition relativePosition(Symbol symbol){
        // The relative position between a symbol and a minus operator or a symbol and a fraction line operator depends
        // on two parameters, x and y.
        //
        // Calculating x:
        // If the center of mass of the symbol is to the left of the top left corner of the operator, the x = -1.
        // If the center of mass of the symbol is to the right of the top left corner but to the left of the bottom right
        //   corner of the operator, then x = 0.
        // In any other case, x = 1.
        //
        // Calculating y:
        // if x = 1:
        // Let a line segment with a slope of 45 degrees start from the top right corner of the operator and extend
        //   to infinity. If the center of mass of the Symbol is above this line segment, then y = 1.
        // Let a line segment with a slope of -45 degrees start from the top right corner of the operator and
        //   extend to infinity. if the center of mass of the Symbol is below this line segment, the y = -1.
        // In any other case, y = 0.
        //
        // if x = 0:
        // If the center of mass of the Symbol is below the bottom right corner of the operator, then y = -1.
        // If the center of mass of the Symbol is above the bottom right corner of the operator but below the top left
        // corner, the y = 0.
        // In any other case, y = 1.
        //
        // If x = -1:
        // Let a line segment with a slope of 135 degrees start from the top left corner of the operator and extend
        //   to infinity. If the center of mass of the Symbol is above this line segment, then y = 1.
        // Let a line segment with a slope of -135 degrees start from the top left corner of the oPerator and
        //   extend to infinity. If the center of mass of the Symbol is below this line segment, then y = -1.
        // In any other case, y = 0.
        //
        // Position is derived as follows:
        // x     y     position
        // -1    -1    BELOW_LEFT
        // -1    0     LEFT
        // -1    1     ABOVE_LEFT
        //
        // 0     -1    BELOW
        // 0     0     INSIDE
        // 0     1     ABOVE
        //
        // 1     -1    BELOW_RIGHT which gets changed to RIGHT. This is to avoid the case where a symbol is drawn a little
        //                         lower and thus is BELOW_RIGHT of the operator symbol and not right.
        // 1     0     RIGHT
        // 1     1     ABOVE_RIGHT which gets changed to RIGHT. This is to avoid the case where a symbol is drawn a little
        //                         higher and thus is ABOVE_RIGHT of the operator symbol and not right.
        traceGroup_.calculateCorners();

        int xPosition;
        int yPosition;
        if(symbol.getTraceGroup().getCenterOfMass().x_ < traceGroup_.getTopLeftCorner().x_){
            xPosition = -1;

            if(symbol.getTraceGroup().getCenterOfMass().y_ < Math.tan(Math.PI / 4) * (symbol.getTraceGroup().getCenterOfMass().x_ -
                    traceGroup_.getTopLeftCorner().x_) +
                    traceGroup_.getTopLeftCorner().y_){
                yPosition = -1;
            }
            else if(symbol.getTraceGroup().getCenterOfMass().y_ <= -Math.tan(Math.PI / 4) *
                    (symbol.getTraceGroup().getCenterOfMass().x_ - traceGroup_.getTopLeftCorner().x_) +
                    traceGroup_.getTopLeftCorner().y_){
                yPosition = 0;
            }
            else{
                yPosition = 1;
            }
        }
        else if(symbol.getTraceGroup().getCenterOfMass().x_ <= traceGroup_.getBottomRightCorner().x_){
            xPosition = 0;

            if(symbol.getTraceGroup().getCenterOfMass().y_ < traceGroup_.getBottomRightCorner().y_){
                yPosition = -1;
            }
            else if(symbol.getTraceGroup().getCenterOfMass().y_ <= traceGroup_.getTopLeftCorner().y_){
                yPosition = 0;
            }
            else{
                yPosition = 1;
            }
        }
        else{
            xPosition = 1;
            if(symbol.getTraceGroup().getCenterOfMass().y_ < -Math.tan(Math.PI / 4) * (symbol.getTraceGroup().getCenterOfMass().x_ -
                    traceGroup_.getBottomRightCorner().x_) +
                    traceGroup_.getBottomRightCorner().y_){
                yPosition = -1;
            }
            else if(symbol.getTraceGroup().getCenterOfMass().y_ <= Math.tan(Math.PI / 4) *
                    (symbol.getTraceGroup().getCenterOfMass().x_ - traceGroup_.getBottomRightCorner().x_) +
                    traceGroup_.getBottomRightCorner().y_){
                yPosition = 0;
            }
            else{
                yPosition = 1;
            }
        }

        if(yPosition == 1){

            if(xPosition == -1){
                return ArgumentPosition.ABOVE_LEFT;
            }
            else if(xPosition == 0){
                return ArgumentPosition.ABOVE;
            }
            else{
                return ArgumentPosition.RIGHT;
            }

        }
        else if(yPosition == 0){

            if(xPosition == -1){
                return ArgumentPosition.LEFT;
            }
            else if(xPosition == 0){
                if(symbol.getTraceGroup().getArea() > traceGroup_.getArea()){
                    return ArgumentPosition.OUTSIDE;
                }
                else{
                    return ArgumentPosition.INSIDE;
                }
            }
            else{
                return ArgumentPosition.RIGHT;
            }

        }
        else{

            if(xPosition == -1){
                return ArgumentPosition.BELOW_LEFT;
            }
            else if(xPosition == 0){
                return ArgumentPosition.BELOW;
            }
            else{
                return ArgumentPosition.RIGHT;
            }

        }
    }

    @Override
    public Labels getLabel () {
        return Labels.MINUS;
    }

    @Override
    public String toString(){
        return toString("-");
    }
}