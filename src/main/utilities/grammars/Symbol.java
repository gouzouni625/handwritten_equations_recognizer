package main.utilities.grammars;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import main.utilities.traces.TraceGroup;

public abstract class Symbol{

  public Symbol(TraceGroup traceGroup){
    traceGroup_ = traceGroup;
  }

  public ArgumentType setArgument(ArgumentPosition relativePosition, Symbol symbol){
    if(Arrays.asList(childrenPositions_).contains(relativePosition)){
      int index = Arrays.asList(childrenPositions_).indexOf(relativePosition);

      children_.get(index).add(symbol);
      return ArgumentType.CHILD;
    }
    else if(Arrays.asList(nextSymbolPositions_).contains(relativePosition)){
      nextSymbol_ = symbol;

      return ArgumentType.NEXT_SYMBOL;
    }

    return ArgumentType.NONE;
  }

  public void setParent(Symbol symbol){
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
        childrenValue += children_.get(i).get(j).nextSymbol_.toString();
      }

      stringValue = stringValue.replaceAll(childrenPositions_[i].toString(), childrenValue);
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
    ABOVE_LEFT;
    //INSIDE,
    //OUTSIDE,

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
        default:
          return position;
      }
    }

  }

  public TraceGroup traceGroup_;

  public Symbol parent_;

  public List<List<Symbol>> children_;
  public ArgumentPosition[] childrenPositions_;

  public Symbol nextSymbol_;
  public ArgumentPosition[] nextSymbolPositions_;

  public Enum<?> type_;

}
