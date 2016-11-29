package org.hwer.engine.symbols.ambiguous;


import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.engine.symbols.SymbolFactory.Classes;

import java.util.ArrayList;
import java.util.List;


/**
 * @class Ambiguous
 * @brief Implements the Symbol class of ambiguous Symbols
 *        An ambiguous Symbol is a Symbol which could be more than one Symbols depending on the
 *        context. This class holds these possible Symbols until it is clear which one this
 *        ambiguous Symbol is, or it is forced to decide.
 */
public abstract class Ambiguous extends Symbol {
    /**
     * @brief Constructor
     *
     * @param traceGroup
     *     The TraceGroup of this Symbol
     */
    public Ambiguous (TraceGroup traceGroup) {
        super(traceGroup);

        children_ = new ArrayList<List<Symbol>>();
        childrenPositions_ = new ArgumentPosition[] {};
        childrenClasses_ = new Classes[][] {};
        childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {};
    }

    /**
     * @brief Processes an argument at a given position for this Symbol
     *        This method concludes on the relation between this Symbol and the given one
     *
     * @param relativePosition
     *     The relative position of this Symbol and the given Symbol
     * @param symbol
     *     The given Symbol
     *
     * @return The ArgumentType of the given Symbol for this Symbol
     */
    @Override
    public ArgumentType setArgument (ArgumentPosition relativePosition, Symbol symbol) {
        if (chosenSymbol_ != this) {
            ArgumentType argumentType = chosenSymbol_.setArgument(relativePosition, symbol);

            switch (argumentType) {
                case NEXT_SYMBOL:
                    setNextSymbol(symbol);
                    break;
            }

            return argumentType;
        }

        ArgumentType argumentType;
        boolean nextArgumentFlag = false;
        for (Symbol possibleSymbol : possibleSymbols_) {
            argumentType = possibleSymbol.setArgument(relativePosition, symbol);

            if (argumentType == ArgumentType.CHILD) {
                this.choose(possibleSymbol);

                return (ArgumentType.CHILD);
            }

            nextArgumentFlag = (argumentType == ArgumentType.NEXT_SYMBOL) || nextArgumentFlag;
        }

        if (nextArgumentFlag) {
            setNextSymbol(symbol);

            return ArgumentType.NEXT_SYMBOL;
        }
        else {
            return ArgumentType.NONE;
        }
    }

    /**
     * @brief Removes a child from this Symbol
     *
     * @param symbol
     *     The child to be removed
     */
    @Override
    public void removeChild (Symbol symbol) {
        if (chosenSymbol_ != this) {
            chosenSymbol_.removeChild(symbol);
        }
    }

    /**
     * @brief Chooses one of the possible Symbols as this Symbol
     *
     * @param symbol
     *     The chosen possible Symbol
     */
    public void choose (Symbol symbol) {
        chosenSymbol_ = symbol;
    }

    /**
     * @brief Returns the relative position of a given Symbol to this Symbol
     *
     * @param symbol
     *     The given Symbol
     *
     * @return The relative position of a given Symbol to this Symbol
     */
    @Override
    public ArgumentPosition relativePosition (Symbol symbol) {
        if (chosenSymbol_ != this) {
            return chosenSymbol_.relativePosition(symbol);
        }
        else {
            return (super.relativePosition(symbol));
        }
    }

    /**
     * @brief Clears a String that represents a Symbol from unneeded characters
     *
     * @param string
     *     The String to be cleared
     *
     * @return The cleared String
     */
    @Override
    public String clearString (String string) {
        if (chosenSymbol_ != this) {
            return chosenSymbol_.clearString(string);
        }
        else {
            return super.clearString(string);
        }
    }

    /**
     * @brief Returns the clazz of this SymbolClass
     *
     * @return The clazz of this Symbol
     */
    public Classes getClazz () {
        if (chosenSymbol_ != this) {
            return chosenSymbol_.getClazz();
        }
        else {
            return Classes.AMBIGUOUS;
        }
    }

    /**
     * @brief Builds the equation beginning from this Symbol
     *
     * @return The String representation of the equation
     */
    @Override
    public String buildExpression () {
        if (chosenSymbol_ != this) {
            return chosenSymbol_.buildExpression();
        }
        else {
            return super.buildExpression();
        }
    }

    /**
     * @brief Returns a string representation of this Symbol
     *
     * @return A string representation of this Symbol
     */
    @Override
    public String toString () {
        if (chosenSymbol_ != this) {
            return chosenSymbol_.toString();
        }
        else {
            return Classes.AMBIGUOUS.toString();
        }
    }

    /**
     * @brief Resets this Symbol
     *        Resetting a Symbols means to bring the Symbol back at the state that was the moment
     *        right after it was instantiated
     */
    @Override
    public void reset () {
        setParent(null);
        setPreviousSymbol(null);
        setNextSymbol(null);

        if (possibleSymbols_ != null) {
            for (Symbol possibleSymbol : possibleSymbols_) {
                possibleSymbol.reset();
            }
        }

        chosenSymbol_ = this;
    }

    /**
     * @brief Getter method for the children Symbols of this Symbol
     *
     * @return The children Symbols of this Symbol
     */
    @Override
    public List<List<Symbol>> getChildren () {
        if (chosenSymbol_ != this) {
            return chosenSymbol_.getChildren();
        }
        else {
            return super.getChildren();
        }
    }

    /**
     * @brief Returns true if this Symbol has at least one child
     *
     * @return True if this Symbol has at least one child
     */
    @Override
    public boolean hasChildren () {
        return chosenSymbol_ != this && chosenSymbol_.hasChildren();
    }

    /**
     * @brief Returns a string representation of this SymbolClass
     *        The string representation can vary based on the Symbol of this SymbolClass that
     *        this method is called for.
     *
     * @param symbolString
     *     A string provided by the Symbol of this SymbolClass that this method is called for
     *
     * @return The string representation of this SymbolClass
     */
    public String toString (String symbolString) {
        return toString();
    }

    protected Symbol[] possibleSymbols_; //!< The possible Symbols that this Symbol could be
    protected Symbol chosenSymbol_; //!< The chosen Symbol of the possible Symbols

}
