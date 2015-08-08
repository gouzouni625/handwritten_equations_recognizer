package main.utilities.symbols;

import java.util.List;
import java.util.ArrayList;

import main.utilities.symbols.Symbol;
import main.utilities.symbols.Symbol.ArgumentPosition;
import main.utilities.symbols.Symbol.ChildAcceptanceCriterion;
import main.utilities.symbols.Symbol.SymbolClass;
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
        childrenClass_ = new SymbolClass[][] {{SymbolClass.NUMBER, SymbolClass.OPERATOR, SymbolClass.LETTER, SymbolClass.UNRECOGNIZED}, {SymbolClass.NUMBER, SymbolClass.OPERATOR, SymbolClass.LETTER, SymbolClass.UNRECOGNIZED}};
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{allChildAcceptanceCriterion, allChildAcceptanceCriterion, allChildAcceptanceCriterion, allChildAcceptanceCriterion},
                                                                        {allChildAcceptanceCriterion, allChildAcceptanceCriterion, allChildAcceptanceCriterion, allChildAcceptanceCriterion}};
        break;
      case SQRT:
        children_ = new ArrayList<List<Symbol>>();
        children_.add(new ArrayList<Symbol>());
        children_.add(new ArrayList<Symbol>());
        childrenPositions_ = new ArgumentPosition[] {ArgumentPosition.INSIDE, ArgumentPosition.ABOVE_RIGHT};
        childrenClass_ = new SymbolClass[][] {{SymbolClass.NUMBER, SymbolClass.OPERATOR, SymbolClass.LETTER, SymbolClass.UNRECOGNIZED}, {SymbolClass.NUMBER, SymbolClass.OPERATOR, SymbolClass.LETTER, SymbolClass.UNRECOGNIZED}};
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {{sizeChildAcceptanceCriterion, sizeChildAcceptanceCriterion, sizeChildAcceptanceCriterion, sizeChildAcceptanceCriterion},
                                                                        {sizeChildAcceptanceCriterion, sizeChildAcceptanceCriterion, sizeChildAcceptanceCriterion, sizeChildAcceptanceCriterion}};
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
    SQRT("\\sqrt{" + ArgumentPosition.INSIDE + "}^{" + ArgumentPosition.ABOVE_RIGHT + "}"),
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
        ArgumentPosition relativePosition = super.relativePosition(symbol);

        if(relativePosition == ArgumentPosition.ABOVE_RIGHT || relativePosition == ArgumentPosition.BELOW_RIGHT){
          relativePosition = ArgumentPosition.RIGHT;
        }

        return relativePosition;
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
            return ArgumentPosition.RIGHT; //ArgumentPosition.ABOVE_RIGHT; EQUALS symbol can't accept ABOVE_RIGHT argument.
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
            return ArgumentPosition.RIGHT; //ArgumentPosition.BELOW_RIGHT; EQUALS symbol can't accept BELOW_RIGHT argument.
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
            return ArgumentPosition.RIGHT; // MINUS and FRACTION_LINE can't accept ABOVE_RIGHT arguments. ArgumentPosition.ABOVE_RIGHT;
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
            return ArgumentPosition.RIGHT; //ArgumentPosition.BELOW_RIGHT; MINUS and FRACTION_LINE symbols can't accept BELOW_RIGHT arguments.
          }

        }
      case SQRT:
        relativePosition = super.relativePosition(symbol);
        if(relativePosition == ArgumentPosition.BELOW_RIGHT){
          relativePosition = ArgumentPosition.RIGHT;
        }

        return relativePosition;
      case LEFT_PARENTHESIS:
        relativePosition = super.relativePosition(symbol);
        if(relativePosition == ArgumentPosition.INSIDE || relativePosition == ArgumentPosition.ABOVE_RIGHT || relativePosition == ArgumentPosition.BELOW_RIGHT){
          relativePosition = ArgumentPosition.RIGHT;
        }

        return relativePosition;
      case RIGHT_PARENTHESIS:
        relativePosition = super.relativePosition(symbol);
        if(relativePosition == ArgumentPosition.INSIDE || relativePosition == ArgumentPosition.BELOW_RIGHT){
          relativePosition = ArgumentPosition.LEFT;
        }
        else if(relativePosition == ArgumentPosition.ABOVE){
          relativePosition = ArgumentPosition.ABOVE_RIGHT;
        }

        return relativePosition;
      default:
        return null;
    }
  }

}
