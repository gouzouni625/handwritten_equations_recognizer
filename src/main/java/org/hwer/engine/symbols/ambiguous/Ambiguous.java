package org.hwer.engine.symbols.ambiguous;

import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.engine.symbols.SymbolFactory.Classes;


/**
 * @class UnrecognizedSymbol
 * @brief Implements an UnrecognizedSymbol.
 * <p>
 * An UnrecognizedSymbol is a Symbol that its type is not decided yet. An example would be a horizontal line.
 * A horizontal line can be both a MINUS Symbol and a FRACTION_LINE. The arguments of this Symbol will decide its type.
 * Returning to the horizontal line example, if an argument is found ABOVE or BELOW, then the horizontal line is a
 * FRACTION_LINE. On the contrary, if no arguments are found, then, the horizontal line is a MINUS Operator. \n\n
 * <p>
 * An UnrecognizedSymbol has an array of possible symbols. These possible symbols should comply with the following rules:
 * - The possible symbols of an UnrecognizedSymbol should all have the same nextSymbol places.
 * - The possible symbols should have totally different children positions. For example, if there are five possible
 * symbols and one of them accepts a child ABOVE, then non of the other four symbols should accept a child ABOVE.
 * - If there are N possible symbols, then N-1 of them should accept at least 1 child.
 * - All possible symbols should have the same implementation of relativePosition method.
 */
public abstract class Ambiguous extends Symbol {
    /**
     * @param traceGroup The TraceGroup of this UnrecognizedSymbol.
     * @brief Constructor.
     */
    public Ambiguous (TraceGroup traceGroup) {
        super(traceGroup);

        chosenSymbol_ = this;
    }

    /**
     * @param relativePosition The relative position of this Symbol and the given Symbol.
     * @param symbol           The given Symbol.
     * @return Returns the ArgumentType of the given Symbol for this Symbol.
     * @brief Processes an argument at a given position for this Symbol.
     * This method concludes on the relation between this Symbol and a given Symbol.
     */
    @Override
    public ArgumentType setArgument (ArgumentPosition relativePosition, Symbol symbol) {
        // If one of the possible symbols has been chosen, use default relativePosion implementation.
        if (chosenSymbol_ != this) {
            return chosenSymbol_.setArgument(relativePosition, symbol);
        }

        // Find the relative position for each one of the possible symbols. If at least one of these symbols accepts the
        // given symbol as a child, then this symbol is chosen. If at least one of these symbols accepts the given symbol
        // as a NEXT_SYMBOL, then, the value returned from this method is NEXT_SYMBOL.
        ArgumentType argumentType;
        boolean nextArgumentFlag = false;
        for (Symbol possibleSymbol : possibleSymbols_) {
            argumentType = possibleSymbol.setArgument(relativePosition, symbol);

            if (argumentType == ArgumentType.CHILD) {
                this.choose(possibleSymbol);

                return (ArgumentType.CHILD);
            }

            nextArgumentFlag = (argumentType == ArgumentType.NEXT_SYMBOL);
        }

        if (nextArgumentFlag) {
            return ArgumentType.NEXT_SYMBOL;
        }
        else {
            return ArgumentType.NONE;
        }
    }

    /**
     * @param symbol The child to be removed.
     * @brief Removes a child from this Symbol.
     */
    @Override
    public void removeChild (Symbol symbol) {
        // If one of the possible symbols has been chosen, use default removeChild implementation.
        if (chosenSymbol_ != this) {
            chosenSymbol_.removeChild(symbol);
        }
        else {
            // Call removeChild for every possible symbol.
            for (Symbol symbolIterator : possibleSymbols_) {
                symbolIterator.removeChild(symbol);
            }
        }
    }

    /**
     * @param parent The Symbol to be set as parent for this Symbol.
     * @brief Sets the parent of this Symbol.
     */
    @Override
    public void setParent (Symbol parent) {
        // If one of the possible symbols has been chosen, use the default setParent implementation.
        if (chosenSymbol_ != this) {
            chosenSymbol_.setParent(parent);
        }
        else{
            // Call setParent for every possible symbol.
            for (Symbol symbolIterator : possibleSymbols_) {
                symbolIterator.setParent(parent);
            }
        }
    }

    /**
     * @brief Chooses the type of this UnrecognizedSymbol.
     */
    @Override
    public void reEvaluate () {
        if (chosenSymbol_ != this) {
            return;
        }

        // Came here means that no child has been assigned to any of the possible symbols.
        // That is because, if at least 1 child had been assigned in setArgument method,
        // then the symbol accepting the child would have become the chosen symbol. So, now,
        // choose the symbol that accepts no children.
        for (Symbol possibleSymbol : possibleSymbols_) {
            if (possibleSymbol.childrenPositions_.length == 0) {
                this.choose(possibleSymbol);
                return;
            }
        }
    }

    /**
     * @param symbol The position of the chosen symbol inside possibleSymbols_ array.
     * @brief Copies the parameters of the chosen symbol to this Symbol.
     */
    public void choose (Symbol symbol) {
        chosenSymbol_ = symbol;
    }

    /**
     * @param symbol The given Symbol.
     * @return Returns the relative position between this Symbol and the given one.
     * @brief Finds the relative position between this Symbol and a given Symbol.
     */
    @Override
    public ArgumentPosition relativePosition (Symbol symbol) {
        // If one of the possible symbols has been chosen, use its implementation of relativePosition. In any other case,
        // use the default implementation.
        if (chosenSymbol_ != this) {
            return chosenSymbol_.relativePosition(symbol);
        }
        else {
            return (super.relativePosition(symbol));
        }
    }

    @Override
    public String clearString (String string) {
        if (chosenSymbol_ != this) {
            return chosenSymbol_.clearString(string);
        }
        else {
            return super.clearString(string);
        }
    }

    public Classes getClazz () {
        if(chosenSymbol_ != this){
            return chosenSymbol_.getClazz();
        }
        else{
            return Classes.AMBIGUOUS;
        }
    }

    /**
     * @return Returns the String representation of this Symbol.
     * @brief Returns the String representation of this Symbol.
     */
    @Override
    public String toString () {
        // If one of the possible symbols has been chosen, use the default toString implementation. In any other case, return
        // a constant String.
        if (chosenSymbol_ != this) {
            return chosenSymbol_.toString();
        }
        else {
            return Classes.AMBIGUOUS.toString();
        }
    }

    public String toString(String symbolString){
        return toString();
    }

    protected Symbol[] possibleSymbols_; //!< An array of all the possible symbols for this UnrecognizedSymbol.
    protected Symbol chosenSymbol_;

}
