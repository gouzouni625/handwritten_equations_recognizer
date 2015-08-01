package main.utilities.grammars;

import java.util.ArrayList;
import java.util.List;

import main.utilities.traces.TraceGroup;

public class UnrecognizedSymbol extends Symbol{

  // The possible symbols of an unrecognized symbol should all have the same
  // nextSymbol places. If setArgument returns nextSymbol for one of the possibleSymbols.
  // Then it should return nextSymbol for every other one.
  // Moreover, they should have totally different children places. That is, a position that
  // accepts a child should be assign to only one of the possible symbols.
  // Moreover, if there are N possible symbols, then then N-1 should accept at least 1 child.
  // Moreover, all the possibleSymbols should have the same implementation of relative position.
  public UnrecognizedSymbol(UnrecognizedSymbol.Types type, TraceGroup traceGroup){
    super(traceGroup, SymbolClass.UNRECOGNIZED);

    symbolClass_ = SymbolClass.UNRECOGNIZED;

    type_ = type;

    parent_ = null;

    children_ = new ArrayList<List<Symbol>>();
    childrenPositions_ = new ArgumentPosition[] {};
    childrenClass_ = new SymbolClass[][] {};
    nextSymbol_ = null;
    nextSymbolPositions_ = new ArgumentPosition[] {};
    childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {};

    switch(type){
      case HORIZONTAL_LINE:
        possibleSymbols_ = new Symbol[] {SymbolFactory.createByType(Operator.Types.MINUS, traceGroup), SymbolFactory.createByType(Operator.Types.FRACTION_LINE, traceGroup)};
        break;
    }
  }

  public enum Types{
    HORIZONTAL_LINE;
  }

  @Override
  public ArgumentType setArgument(Symbol.ArgumentPosition relativePosition, Symbol symbol){
    if(chosenSymbol_ != -1){
      return super.setArgument(relativePosition, symbol);
    }

    ArgumentType argumentType;
    boolean nextArgumentFlag = false;
    for(int i = 0;i < possibleSymbols_.length;i++){
      argumentType = possibleSymbols_[i].setArgument(relativePosition, symbol);

      if(argumentType == ArgumentType.CHILD){
        this.choose(i);

        return (ArgumentType.CHILD);
      }

      nextArgumentFlag = (argumentType == ArgumentType.NEXT_SYMBOL);
    }

    if(nextArgumentFlag){
      return ArgumentType.NEXT_SYMBOL;
    }
    else{
      return ArgumentType.NONE;
    }
  }

  @Override
  public void removeChild(Symbol symbol){
    if(chosenSymbol_ != -1){
      super.removeChild(symbol);

      return;
    }

    for(Symbol symbolIterator : possibleSymbols_){
      symbolIterator.removeChild(symbol);
    }
  }

  @Override
  public void setParent(Symbol symbol){
    if(chosenSymbol_ != -1){
      super.setParent(symbol);

      return;
    }

    for(Symbol symbolIterator : possibleSymbols_){
      symbolIterator.setParent(symbol);
    }
  }

  @Override
  public String toString(){
    if(chosenSymbol_ != -1){
      return (super.toString());
    }
    else{
      return (SymbolClass.UNRECOGNIZED.toString());
    }
  }

  @Override
  public void reEvaluate(){
    if(chosenSymbol_ != -1){
      return;
    }

    // Came here means that no child has been assigned to any of the possible symbols.
    // That is because, if at least 1 child had been assigned in setArgument method,
    // then the symbol accepting the child would have became the chosen symbol. So, now,
    // choose the symbol that accepts no children.
    for(int i = 0;i < possibleSymbols_.length;i++){
      if(possibleSymbols_[i].childrenPositions_.length == 0){
        this.choose(i);
        return;
      }
    }
  }

  public void choose(int symbol){
    chosenSymbol_ = symbol;

    type_ = possibleSymbols_[chosenSymbol_].type_;
    symbolClass_ = possibleSymbols_[chosenSymbol_].symbolClass_;
    parent_ = possibleSymbols_[chosenSymbol_].parent_;
    children_ = possibleSymbols_[chosenSymbol_].children_;
    childrenPositions_ = possibleSymbols_[chosenSymbol_].childrenPositions_;
    childrenClass_ = possibleSymbols_[chosenSymbol_].childrenClass_;
    nextSymbol_ = possibleSymbols_[chosenSymbol_].nextSymbol_;
    nextSymbolPositions_ = possibleSymbols_[chosenSymbol_].nextSymbolPositions_;
    childrenAcceptanceCriteria_ = possibleSymbols_[chosenSymbol_].childrenAcceptanceCriteria_;
  }

  @Override
  public ArgumentPosition relativePosition(Symbol symbol){
    if(chosenSymbol_ != -1){
      return possibleSymbols_[chosenSymbol_].relativePosition(symbol);
    }

    return (super.relativePosition(symbol));
  }

  public Symbol[] possibleSymbols_;
  public int chosenSymbol_ = -1;

}
