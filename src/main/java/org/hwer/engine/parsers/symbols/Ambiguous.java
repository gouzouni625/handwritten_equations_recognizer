package org.hwer.engine.parsers.symbols;

import java.util.ArrayList;
import java.util.List;

import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.implementations.classifiers.nnclassifier.symbols.SymbolFactory;

/** @class UnrecognizedSymbol
 *
 *  @brief Implements an UnrecognizedSymbol.
 *
 *  An UnrecognizedSymbol is a Symbol that its type is not decided yet. An example would be a horizontal line.
 *  A horizontal line can be both a MINUS Symbol and a FRACTION_LINE. The arguments of this Symbol will decide its type.
 *  Returning to the horizontal line example, if an argument is found ABOVE or BELOW, then the horizontal line is a
 *  FRACTION_LINE. On the contrary, if no arguments are found, then, the horizontal line is a MINUS Operator. \n\n
 *
 *  An UnrecognizedSymbol has an array of possible symbols. These possible symbols should comply with the following rules:
 *  - The possible symbols of an UnrecognizedSymbol should all have the same nextSymbol places.
 *  - The possible symbols should have totally different children positions. For example, if there are five possible
 *     symbols and one of them accepts a child ABOVE, then non of the other four symbols should accept a child ABOVE.
 *  - If there are N possible symbols, then N-1 of them should accept at least 1 child.
 *  - All possible symbols should have the same implementation of relativePosition method.
 */
public abstract class Ambiguous extends Symbol {
  /**
   *  @brief Constructor.
   *
   *  @param type The type of this UnrecognizedSymbol.
   *  @param traceGroup The TraceGroup of this UnrecognizedSymbol.
   */
  public Ambiguous (TraceGroup traceGroup){
    super(traceGroup);

    type_ = type;

    // Initially the UnrecognizedSymbol has no parent.
    parent_ = null;

    // Initially all the fields of UnrecognizedSymbol are empty.
    children_ = new ArrayList<List<Symbol>>();
    childrenPositions_ = new ArgumentPosition[] {};
    childrenClass_ = new SymbolClass[][] {};
    nextSymbol_ = null;
    nextSymbolPositions_ = new ArgumentPosition[] {};
    childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {};

    // Create the possible symbols base on the type of this UnrecognizedSymbol.
    switch(type){
      case HORIZONTAL_LINE:
        // A horizontal line can be either a MINUS Operator or a FRACTION_LINE.
        possibleSymbols_ = new Symbol[] {SymbolFactory.createByType(Operator.Types.MINUS, traceGroup),
                                         SymbolFactory.createByType(Operator.Types.FRACTION_LINE, traceGroup)};
        break;
    }
  }

  /** @class Types
   *
   *  @brief Contains all the different types of UnrecognizedSymbol objects.
   */
  public enum Types{
    HORIZONTAL_LINE;
  }

  /**
   *  @brief Processes an argument at a given position for this Symbol.
   *  This method concludes on the relation between this Symbol and a given Symbol.
   *
   *  @param relativePosition The relative position of this Symbol and the given Symbol.
   *  @param symbol The given Symbol.
   *
   *  @return Returns the ArgumentType of the given Symbol for this Symbol.
   */
  @Override
  public ArgumentType setArgument(Symbol.ArgumentPosition relativePosition, Symbol symbol){
    // If one of the possible symbols has been chosen, use default relativePosion implementation.
    if(chosenSymbol_ != -1){
      return super.setArgument(relativePosition, symbol);
    }

    // Find the relative position for each one of the possible symbols. If at least one of these symbols accepts the
    // given symbol as a child, then this symbol is chosen. If at least one of these symbols accepts the given symbol
    // as a NEXT_SYMBOL, then, the value returned from this method is NEXT_SYMBOL.
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

  /**
   *  @brief Removes a child from this Symbol.
   *
   *  @param symbol The child to be removed.
   */
  @Override
  public void removeChild(Symbol symbol){
    // If one of the possible symbols has been chosen, use default removeChild implementation.
    if(chosenSymbol_ != -1){
      super.removeChild(symbol);

      return;
    }

    // Call removeChild for every possible symbol.
    for(Symbol symbolIterator : possibleSymbols_){
      symbolIterator.removeChild(symbol);
    }
  }

  /**
   *  @brief Sets the parent of this Symbol.
   *
   *  @param symbol The Symbol to be set as parent for this Symbol.
   */
  @Override
  public void setParent(Symbol symbol){
    // If one of the possible symbols has been chosen, use the default setParent implementation.
    if(chosenSymbol_ != -1){
      super.setParent(symbol);

      return;
    }

    // Call setParent for every possible symbol.
    for(Symbol symbolIterator : possibleSymbols_){
      symbolIterator.setParent(symbol);
    }
  }

  /**
   *  @brief Returns the String representation of this Symbol.
   *
   *  @return Returns the String representation of this Symbol.
   */
  @Override
  public String toString(){
    // If one of the possible symbols has been chosen, use the default toString implementation. In any other case, return
    // a constant String.
    if(chosenSymbol_ != -1){
      return (super.toString());
    }
    else{
      return (SymbolClass.UNRECOGNIZED.toString());
    }
  }

  /**
   *  @brief Chooses the type of this UnrecognizedSymbol.
   */
  @Override
  public void reEvaluate(){
    if(chosenSymbol_ != -1){
      return;
    }

    // Came here means that no child has been assigned to any of the possible symbols.
    // That is because, if at least 1 child had been assigned in setArgument method,
    // then the symbol accepting the child would have become the chosen symbol. So, now,
    // choose the symbol that accepts no children.
    for(int i = 0;i < possibleSymbols_.length;i++){
      if(possibleSymbols_[i].childrenPositions_.length == 0){
        this.choose(i);
        return;
      }
    }
  }

  /**
   *  @brief Copies the parameters of the chosen symbol to this Symbol.
   *
   *  @param symbol The position of the chosen symbol inside possibleSymbols_ array.
   */
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

  /**
   *  @brief Finds the relative position between this Symbol and a given Symbol.
   *
   *  @param symbol The given Symbol.
   *
   *  @return Returns the relative position between this Symbol and the given one.
   */
  @Override
  public ArgumentPosition relativePosition(Symbol symbol){
    // If one of the possible symbols has been chosen, use its implementation of relativePosition. In any other case,
    // use the default implementation.
    if(chosenSymbol_ != -1){
      return possibleSymbols_[chosenSymbol_].relativePosition(symbol);
    }
    else{
      return (super.relativePosition(symbol));
    }
  }

  @Override
  public String clearString(String string){
    if(chosenSymbol_ != -1){
      return possibleSymbols_[chosenSymbol_].clearString(string);
    }
    else{
      return super.clearString(string);
    }
  }

  public Symbol[] possibleSymbols_; //!< An array of all the possible symbols for this UnrecognizedSymbol.
  public int chosenSymbol_ = -1; //!< The position of the chosen symbol inside possibleSymbols_ array.

}
