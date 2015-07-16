package main.utilities.grammars;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import main.utilities.traces.TraceGroup;

public abstract class Symbol{

  public Symbol(TraceGroup traceGroup){
    traceGroup_ = traceGroup;

    activeArgument_ = null;
  }

  public void reEvaluate(){}

  public Enum<?> getType(){
    return type_;
  }

  public void setArgument(ArgumentPosition argumentPosition, Symbol symbol){
    int index = Arrays.asList(positionOfPassiveArguments_).indexOf(argumentPosition);

    if(index == -1){
      index = Arrays.asList(positionOfActiveArguments_).indexOf(argumentPosition);

      if(index == -1){
        switch(argumentPosition){
          case ABOVE_RIGHT:
          case BELOW_RIGHT:
            this.setArgument(Symbol.ArgumentPosition.RIGHT, symbol);
            break;
          case ABOVE_LEFT:
          case BELOW_LEFT:
            this.setArgument(Symbol.ArgumentPosition.LEFT, symbol);
            break;
          default:
            return;
        }
      }
      else{
        activeArgument_ = symbol;
      }
    }
    else{
      passiveArguments_.get(index).add(symbol);
    }
  }

  /**
   * Calls print passive on the passive arguments. There is no need to print the active argument
   * of the passive arguments since this is the current Symbol.
   * @return
   */
  public String printPassive(ArgumentPosition[] argumentPosition, String... argument){
    String stringValue = type_.toString();

    for(int i = 0;i < positionOfPassiveArguments_.length;i++){
      String argumentValue;
      int index;

      if(argumentPosition != null){
        index = Arrays.asList(argumentPosition).indexOf(positionOfPassiveArguments_[i]);

        argumentValue = argument[index];
      }
      else{
        argumentValue = "";

        for(int j = 0;j < passiveArguments_.get(i).size();j++){
          argumentValue += passiveArguments_.get(i).get(j).printPassive(null);
        }
      }

      stringValue = stringValue.replaceAll(positionOfPassiveArguments_[i].toString(), argumentValue);
    }

    return stringValue;
  }

  public String toString(){
    String stringValue;

    if(activeArgument_ == null){
      stringValue = this.printPassive(null);
    }
    else{
      stringValue = activeArgument_.printPassive(null);
    }

    return (this.clearString(stringValue));
  }

  public String clearString(String string){
    String result = new String(string);

    result = result.replaceAll(Pattern.quote("^{}"), "");
    result = result.replaceAll(Pattern.quote("_{}"), "");

    return result;
  }

  protected Enum<?> type_;

  public List<List<Symbol>> passiveArguments_;
  Symbol activeArgument_;
  public ArgumentPosition[] positionOfActiveArguments_;  // Symbols to which, this symbol, is an argument.
  public ArgumentPosition[] positionOfPassiveArguments_; // Arguments of this symbol.
  public enum ArgumentPosition{
    ABOVE,
    ABOVE_RIGHT,
    RIGHT,
    BELOW_RIGHT,
    BELOW,
    BELOW_LEFT,
    LEFT,
    ABOVE_LEFT;
    //INSIDE
  }

  public TraceGroup traceGroup_;

}
