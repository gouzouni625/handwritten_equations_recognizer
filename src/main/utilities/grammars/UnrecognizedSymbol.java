package main.utilities.grammars;

import java.util.ArrayList;
import java.util.List;

import main.utilities.grammars.Symbol.ChildAcceptanceCriterion;
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
        chosenSymbol_ = null;
        break;
    }
  }

  public enum Types{
    HORIZONTAL_LINE;
  }

  @Override
  public ArgumentType setArgument(Symbol.ArgumentPosition relativePosition, Symbol symbol){
    if(chosenSymbol_ != null){
      ArgumentType argumentType = chosenSymbol_.setArgument(relativePosition, symbol);

      copy();

      return argumentType;
    }

    ArgumentType argumentType;
    boolean nextArgumentFlag = false;
    for(Symbol symbolIterator : possibleSymbols_){
      argumentType = symbolIterator.setArgument(relativePosition, symbol);

      if(argumentType == ArgumentType.CHILD){
        this.choose(symbolIterator);

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
  public String toString(){
    if(chosenSymbol_ != null){
      return (chosenSymbol_.toString());
    }
    else{
      return (SymbolClass.UNRECOGNIZED.toString());
    }
  }

  @Override
  public void reEvaluate(){
    if(chosenSymbol_ != null){
      return;
    }

    // Came here means that no child has been assigned to any of the possible symbols.
    // That is because, if at least 1 child had been assigned in setArgument method,
    // then the symbol accepting the child would have became the chosen symbol. So, now,
    // choose the symbol that accepts no children.
    for(Symbol symbol : possibleSymbols_){
      if(symbol.childrenPositions_.length == 0){
        this.choose(symbol);
        return;
      }
    }
  }

  public void choose(Symbol symbol){
    chosenSymbol_ = symbol;

    copy();
  }

  private void copy(){
    type_ = chosenSymbol_.type_;
    parent_ = chosenSymbol_.parent_;
    children_ = chosenSymbol_.children_;
    childrenPositions_ = chosenSymbol_.childrenPositions_;
    nextSymbol_ = chosenSymbol_.nextSymbol_;
    nextSymbolPositions_ = chosenSymbol_.nextSymbolPositions_;
    childrenAcceptanceCriteria_ = chosenSymbol_.childrenAcceptanceCriteria_;
  }

  @Override
  public ArgumentPosition relativePosition(Symbol symbol){
    // Given that all the possible symbols have the same implementation
    // for relativePosition method, use the first possible symbol.
    return (possibleSymbols_[0].relativePosition(symbol));
  }

  public Symbol[] possibleSymbols_;
  public Symbol chosenSymbol_;

}
