package main.utilities.grammars;

import java.util.List;
import java.util.ArrayList;

import main.utilities.grammars.Symbol;
import main.utilities.grammars.Symbol.ArgumentPosition;
import main.utilities.grammars.Symbol.SymbolClass;
import main.utilities.traces.TraceGroup;

public class Operator extends Symbol{

  public Operator(Operator.Types type, TraceGroup traceGroup){
    super(traceGroup, SymbolClass.OPERATOR);

    type_ = type;

    parent_ = null;

    switch(type){
      case PLUS:
      case EQUALS:
      case MINUS:
        children_ = new ArrayList<List<Symbol>>();
        childrenPositions_ = new ArgumentPosition[] {};
        childrenClass_ = new SymbolClass[][] {};
        break;
      case FRACTION_LINE:
        children_ = new ArrayList<List<Symbol>>();
        children_.add(new ArrayList<Symbol>());
        children_.add(new ArrayList<Symbol>());
        childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.ABOVE, ArgumentPosition.BELOW};
        childrenClass_ = new SymbolClass[][] {{SymbolClass.NUMBER, SymbolClass.OPERATOR, SymbolClass.LETTER}, {SymbolClass.NUMBER, SymbolClass.OPERATOR, SymbolClass.LETTER}};
        break;
    }

    nextSymbol_ = null;
    nextSymbolPositions_ = new ArgumentPosition[] {ArgumentPosition.RIGHT};
  }

  public enum Types{
    PLUS("+"),
    EQUALS("="),
    MINUS("-"),
    FRACTION_LINE("\\frac{" + ArgumentPosition.ABOVE + "}{" + ArgumentPosition.BELOW + "}");

    private Types(String stringValue){
      stringValue_ = stringValue;
    }

    @Override
    public String toString(){
      return stringValue_;
    }

    private String stringValue_;
  }

  @Override
  public ArgumentPosition relativePosition(Symbol symbol){
    switch((Types)type_){
      case PLUS:
      case EQUALS:
        return super.relativePosition(symbol);
      case MINUS:
      case FRACTION_LINE:
        traceGroup_.calculateCorners();

        int xPosition;
        int yPosition;
        if(symbol.traceGroup_.getCenterOfMass().x_ < traceGroup_.getTopLeftCorner().x_){
          xPosition = -1;

          if(symbol.traceGroup_.getCenterOfMass().y_ < Math.tan(Math.PI / 6) * (symbol.traceGroup_.getCenterOfMass().x_ - traceGroup_.getTopLeftCorner().x_) + traceGroup_.getTopLeftCorner().y_){
            yPosition = -1;
          }
          else if(symbol.traceGroup_.getCenterOfMass().y_ <= -Math.tan(Math.PI / 6) * (symbol.traceGroup_.getCenterOfMass().x_ - traceGroup_.getTopLeftCorner().x_) + traceGroup_.getTopLeftCorner().y_){
            yPosition = 0;
          }
          else{
            yPosition = 1;
          }
        }
        else if(symbol.traceGroup_.getCenterOfMass().x_ <= traceGroup_.getBottomRightCorner().x_){
          xPosition = 0;

          if(symbol.traceGroup_.getCenterOfMass().y_ < traceGroup_.getBottomRightCorner().y_){
            yPosition = -1;
          }
          else if(symbol.traceGroup_.getCenterOfMass().y_ <= traceGroup_.getTopLeftCorner().y_){
            yPosition = 0;
          }
          else{
            yPosition = 1;
          }
        }
        else{
          xPosition = 1;
          if(symbol.traceGroup_.getCenterOfMass().y_ < -Math.tan(Math.PI / 6) * (symbol.traceGroup_.getCenterOfMass().x_ - traceGroup_.getBottomRightCorner().x_) + traceGroup_.getBottomRightCorner().y_){
            yPosition = -1;
          }
          else if(symbol.traceGroup_.getCenterOfMass().y_ <= Math.tan(Math.PI / 6) * (symbol.traceGroup_.getCenterOfMass().x_ - traceGroup_.getBottomRightCorner().x_) + traceGroup_.getBottomRightCorner().y_){
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
            return ArgumentPosition.ABOVE_RIGHT;
          }

        }
        else if(yPosition == 0){

          if(xPosition == -1){
            return ArgumentPosition.LEFT;
          }
          /*else if(xPosition == 0){
            return Symbol.ArgumentPosition.INSIDE;
          }*/
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
            return ArgumentPosition.BELOW_RIGHT;
          }

        }
      default:
        return null;
    }
  }

}
