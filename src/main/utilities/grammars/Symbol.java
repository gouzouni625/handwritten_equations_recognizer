package main.utilities.grammars;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import main.utilities.traces.TraceGroup;

public abstract class Symbol{

  public Symbol(TraceGroup traceGroup, SymbolClass symbolClass){
    traceGroup_ = traceGroup;

    symbolClass_ = symbolClass;
  }

  public ArgumentType setArgument(ArgumentPosition relativePosition, Symbol symbol){
    if(Arrays.asList(childrenPositions_).contains(relativePosition)){
      int index = Arrays.asList(childrenPositions_).indexOf(relativePosition);

      if(Arrays.asList(childrenClass_[index]).contains(symbol.symbolClass_)){
        int index2 = Arrays.asList(childrenClass_[index]).indexOf(symbol.symbolClass_);

        if(childrenAcceptanceCriteria_[index][index2].accept(this, symbol, relativePosition)){
          if(!children_.get(index).contains(symbol)){
            children_.get(index).add(symbol);
          }

          return ArgumentType.CHILD;
        }
        else{
          return ArgumentType.NONE;
        }
      }
      else{
        return ArgumentType.NONE;
      }
    }
    else if(Arrays.asList(nextSymbolPositions_).contains(relativePosition)){
      nextSymbol_ = symbol;

      return ArgumentType.NEXT_SYMBOL;
    }

    return ArgumentType.NONE;
  }

  public void removeChild(Symbol symbol){
    for(List<Symbol> childrenList : children_){
      for(int child = 0;child < childrenList.size();child++){
        if(childrenList.get(child) == symbol){
          childrenList.remove(symbol);

          if(child > 0 && childrenList.get(child - 1).nextSymbol_ == symbol){
            childrenList.get(child - 1).nextSymbol_ = null;
          }

          break;
        }
      }
    }

  }

  public void setParent(Symbol symbol){
    // If I already have a parent, and my current parent is different from
    // my upcoming parent(meaning that I am changing a parent) then, my current
    // parent should remove me from being his child.
    if(parent_ != null && parent_ != symbol){
      parent_.removeChild(this);
    }

    parent_ = symbol;
  }

  public String toString(){
    String stringValue = type_.toString();

    for(int i = 0;i < childrenPositions_.length;i++){
      if(children_.get(i).size() == 0){
        continue;
      }

      String childrenValue = children_.get(i).get(0).toString();

      for(int j = 0;j < children_.get(i).size() - 1;j++){
        if(children_.get(i).get(j).nextSymbol_ != null){
          childrenValue += children_.get(i).get(j).nextSymbol_.toString();
        }
        else{
          if(j <= children_.get(i).size() - 2){
            childrenValue += children_.get(i).get(j + 1).toString();
          }
        }
      }

      stringValue = stringValue.replace(childrenPositions_[i].toString(), childrenValue);
    }

    return (this.clearString(stringValue));
  }

  public String clearString(String string){
    String result = new String(string);

    for(ArgumentPosition argumentPosition : childrenPositions_){
      result = result.replaceAll(Pattern.quote("^{") + argumentPosition + Pattern.quote("}"), "");
      result = result.replaceAll(Pattern.quote("_{") + argumentPosition + Pattern.quote("}"), "");
    }

    return result;
  }

  public enum SymbolClass{
    NUMBER,
    LETTER,
    OPERATOR,
    UNRECOGNIZED;
  }

  public enum ArgumentType{
    NONE,
    CHILD,
    NEXT_SYMBOL;
  }

  public enum ArgumentPosition{
    ABOVE,
    ABOVE_RIGHT,
    RIGHT,
    BELOW_RIGHT,
    BELOW,
    BELOW_LEFT,
    LEFT,
    ABOVE_LEFT,
    INSIDE,
    OUTSIDE;

    public static ArgumentPosition oppositePosition(ArgumentPosition position){
      switch(position){
        case ABOVE:
          return ArgumentPosition.BELOW;
        case ABOVE_RIGHT:
          return ArgumentPosition.BELOW_LEFT;
        case RIGHT:
          return ArgumentPosition.LEFT;
        case BELOW_RIGHT:
          return ArgumentPosition.ABOVE_LEFT;
        case BELOW:
          return ArgumentPosition.ABOVE;
        case BELOW_LEFT:
          return ArgumentPosition.ABOVE_RIGHT;
        case LEFT:
          return ArgumentPosition.RIGHT;
        case ABOVE_LEFT:
          return ArgumentPosition.BELOW_RIGHT;
        case INSIDE:
          return ArgumentPosition.OUTSIDE;
        case OUTSIDE:
          return ArgumentPosition.INSIDE;
        default:
          return position;
      }
    }

  }

  // TODO
  // relativePosition should be embedded into setArgument.
  public ArgumentPosition relativePosition(Symbol symbol){
    traceGroup_.calculateCorners();

    int yPosition;
    if(symbol.traceGroup_.getCenterOfMass().y_ < traceGroup_.getBottomRightCorner().y_){
      yPosition = -1;
    }
    else if(symbol.traceGroup_.getCenterOfMass().y_ <= traceGroup_.getTopLeftCorner().y_){
      yPosition = 0;
    }
    else{
      yPosition = 1;
    }

    int xPosition;
    if(symbol.traceGroup_.getCenterOfMass().x_ < traceGroup_.getTopLeftCorner().x_){
      xPosition = -1;
    }
    else if(symbol.traceGroup_.getCenterOfMass().x_ <= traceGroup_.getBottomRightCorner().x_){
      xPosition = 0;
    }
    else{
      xPosition = 1;
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
  }

  public void reEvaluate(){}

  public interface ChildAcceptanceCriterion{
    public boolean accept(Symbol symbol, Symbol child, ArgumentPosition relativePosition);
  };

  public ChildAcceptanceCriterion sizeChildAcceptanceCriterion = new ChildAcceptanceCriterion(){

    @Override
    public boolean accept(Symbol symbol, Symbol child, ArgumentPosition relativePosition){
      return (symbol.traceGroup_.getArea() > 2 * child.traceGroup_.getArea());
    }
  };

  public ChildAcceptanceCriterion sizeWidthChildAcceptanceCriterion = new ChildAcceptanceCriterion() {

    @Override
    public boolean accept(Symbol symbol, Symbol child, ArgumentPosition relativePosition){
      return ((symbol.traceGroup_.getArea() > 2 * child.traceGroup_.getArea()) &&
              (symbol.traceGroup_.getWidth() > 2 * child.traceGroup_.getWidth()));
    }
  };

  public ChildAcceptanceCriterion widthChildAcceptanceCriterion = new ChildAcceptanceCriterion() {

    @Override
    public boolean accept(Symbol symbol, Symbol child, ArgumentPosition relativePosition){
      return (symbol.traceGroup_.getWidth() > 2 * child.traceGroup_.getWidth());
    }
  };

  public ChildAcceptanceCriterion allChildAcceptanceCriterion = new ChildAcceptanceCriterion(){

    @Override
    public boolean accept(Symbol symbol, Symbol child, ArgumentPosition relativePosition){
      return (true);
    }
  };

  public final SymbolClass symbolClass_;

  public TraceGroup traceGroup_;

  public Symbol parent_;

  public List<List<Symbol>> children_;
  public ArgumentPosition[] childrenPositions_;
  public SymbolClass[][] childrenClass_;
  public ChildAcceptanceCriterion[][] childrenAcceptanceCriteria_;

  public Symbol nextSymbol_;
  public ArgumentPosition[] nextSymbolPositions_;

  public Enum<?> type_;

}
