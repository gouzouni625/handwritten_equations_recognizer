package main.utilities.grammars;

import java.util.List;
import java.util.ArrayList;

import main.utilities.grammars.Symbol;
import main.utilities.grammars.Symbol.ArgumentPosition;
import main.utilities.grammars.Symbol.ChildAcceptanceCriterion;
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
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {};
        break;
      case FRACTION_LINE:
        children_ = new ArrayList<List<Symbol>>();
        children_.add(new ArrayList<Symbol>());
        children_.add(new ArrayList<Symbol>());
        childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.ABOVE, ArgumentPosition.BELOW};
        // TODO
        // Should also accept Unrecognized symbols as children.
        childrenClass_ = new SymbolClass[][] {{SymbolClass.NUMBER, SymbolClass.OPERATOR, SymbolClass.LETTER, SymbolClass.UNRECOGNIZED}, {SymbolClass.NUMBER, SymbolClass.OPERATOR, SymbolClass.LETTER, SymbolClass.UNRECOGNIZED}};
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{widthChildAcceptanceCriterion, widthChildAcceptanceCriterion, widthChildAcceptanceCriterion, widthChildAcceptanceCriterion},
                                                                        {widthChildAcceptanceCriterion, widthChildAcceptanceCriterion, widthChildAcceptanceCriterion, widthChildAcceptanceCriterion}};
        break;
      case SQRT:
        children_ = new ArrayList<List<Symbol>>();
        children_.add(new ArrayList<Symbol>());
        childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.INSIDE};
        // TODO
        // Should also accept Unrecognized symbols as children.
        childrenClass_ = new SymbolClass[][] {{SymbolClass.NUMBER, SymbolClass.OPERATOR, SymbolClass.LETTER, SymbolClass.UNRECOGNIZED}};
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{sizeChildAcceptanceCriterion, sizeChildAcceptanceCriterion, sizeChildAcceptanceCriterion, sizeChildAcceptanceCriterion}};
        break;
      case LEFT_PARENTHESIS:
        children_ = new ArrayList<List<Symbol>>();
        childrenPositions_ = new ArgumentPosition[] {};
        childrenClass_ = new SymbolClass[][] {};
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {};
        break;
      case RIGHT_PARENTHESIS:
        children_ = new ArrayList<List<Symbol>>();
        children_.add(new ArrayList<Symbol>());
        childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.ABOVE_RIGHT};
        childrenClass_ = new SymbolClass[][] {{SymbolClass.NUMBER, SymbolClass.LETTER, SymbolClass.OPERATOR, SymbolClass.UNRECOGNIZED}};
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{allChildAcceptanceCriterion, allChildAcceptanceCriterion, allChildAcceptanceCriterion, allChildAcceptanceCriterion}};
        break;
    }

    nextSymbol_ = null;
    nextSymbolPositions_ = new ArgumentPosition[] {ArgumentPosition.RIGHT};
  }

  public enum Types{
    PLUS("+"),
    EQUALS("="),
    MINUS("-"),
    FRACTION_LINE("\\frac{" + ArgumentPosition.ABOVE + "}{" + ArgumentPosition.BELOW + "}"),
    SQRT("\\sqrt{" + ArgumentPosition.INSIDE + "}"),
    LEFT_PARENTHESIS("("),
    RIGHT_PARENTHESIS(")^{" + ArgumentPosition.ABOVE_RIGHT + "}");

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
        return super.relativePosition(symbol);
      case EQUALS:
        traceGroup_.calculateCorners();

        int xPosition;
        int yPosition;
        if(symbol.traceGroup_.getCenterOfMass().x_ < traceGroup_.getTopLeftCorner().x_){
          xPosition = -1;

          if(symbol.traceGroup_.getCenterOfMass().y_ < Math.tan(Math.PI / 4) * (symbol.traceGroup_.getCenterOfMass().x_ - traceGroup_.getBottomLeftCorner().x_) + traceGroup_.getBottomLeftCorner().y_){
            yPosition = -1;
          }
          else if(symbol.traceGroup_.getCenterOfMass().y_ <= -Math.tan(Math.PI / 4) * (symbol.traceGroup_.getCenterOfMass().x_ - traceGroup_.getTopLeftCorner().x_) + traceGroup_.getTopLeftCorner().y_){
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
          if(symbol.traceGroup_.getCenterOfMass().y_ < -Math.tan(Math.PI / 4) * (symbol.traceGroup_.getCenterOfMass().x_ - traceGroup_.getBottomRightCorner().x_) + traceGroup_.getBottomRightCorner().y_){
            yPosition = -1;
          }
          else if(symbol.traceGroup_.getCenterOfMass().y_ <= Math.tan(Math.PI / 4) * (symbol.traceGroup_.getCenterOfMass().x_ - traceGroup_.getTopRightCorner().x_) + traceGroup_.getTopRightCorner().y_){
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
          else if(xPosition == 0){
            if(symbol.traceGroup_.getArea() > traceGroup_.getArea()){
              return Symbol.ArgumentPosition.OUTSIDE;
            }
            else{
              return Symbol.ArgumentPosition.INSIDE;
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
            return ArgumentPosition.BELOW_RIGHT;
          }

        }
      case MINUS:
      case FRACTION_LINE:
        traceGroup_.calculateCorners();

        if(symbol.traceGroup_.getCenterOfMass().x_ < traceGroup_.getTopLeftCorner().x_){
          xPosition = -1;

          if(symbol.traceGroup_.getCenterOfMass().y_ < Math.tan(Math.PI / 4) * (symbol.traceGroup_.getCenterOfMass().x_ - traceGroup_.getTopLeftCorner().x_) + traceGroup_.getTopLeftCorner().y_){
            yPosition = -1;
          }
          else if(symbol.traceGroup_.getCenterOfMass().y_ <= -Math.tan(Math.PI / 4) * (symbol.traceGroup_.getCenterOfMass().x_ - traceGroup_.getTopLeftCorner().x_) + traceGroup_.getTopLeftCorner().y_){
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
          if(symbol.traceGroup_.getCenterOfMass().y_ < -Math.tan(Math.PI / 4) * (symbol.traceGroup_.getCenterOfMass().x_ - traceGroup_.getBottomRightCorner().x_) + traceGroup_.getBottomRightCorner().y_){
            yPosition = -1;
          }
          else if(symbol.traceGroup_.getCenterOfMass().y_ <= Math.tan(Math.PI / 4) * (symbol.traceGroup_.getCenterOfMass().x_ - traceGroup_.getBottomRightCorner().x_) + traceGroup_.getBottomRightCorner().y_){
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
          else if(xPosition == 0){
            if(symbol.traceGroup_.getArea() > traceGroup_.getArea()){
              return Symbol.ArgumentPosition.OUTSIDE;
            }
            else{
              return Symbol.ArgumentPosition.INSIDE;
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
            return ArgumentPosition.BELOW_RIGHT;
          }

        }
      case SQRT:
        return super.relativePosition(symbol);
        // Parenthesis don't support inside position, so translate it to right if we have a left parenthesis
        // or left if we have a right parenthesis.
      case LEFT_PARENTHESIS:
        ArgumentPosition relativePosition = super.relativePosition(symbol);
        if(relativePosition == ArgumentPosition.INSIDE){
          relativePosition = ArgumentPosition.RIGHT;
        }

        return relativePosition;
      case RIGHT_PARENTHESIS:
        relativePosition = super.relativePosition(symbol);
        if(relativePosition == ArgumentPosition.INSIDE){
          relativePosition = ArgumentPosition.LEFT;
        }

        return relativePosition;
      default:
        return null;
    }
  }

}
