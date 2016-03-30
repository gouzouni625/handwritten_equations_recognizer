package org.hwer.engine.symbols;


import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.symbols.SymbolFactory.SymbolClass;
import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;


/**
 * @class Symbol
 * @brief Implements an abstract Symbol
 */
public abstract class Symbol implements SymbolClass {
    /**
     * @brief Constructor
     *
     * @param traceGroup
     *     The TraceGroup of this Symbol
     */
    public Symbol (TraceGroup traceGroup) {
        traceGroup_ = traceGroup;

        reset();
    }

    /**
     * @brief Returns the label of this Symbol
     *
     * @return The label of this Symbol
     */
    public abstract Labels getLabel ();

    /**
     * @brief Returns a string representation of this Symbol
     *
     * @return A string representation of this Symbol
     */
    public abstract String toString ();

    /**
     * @brief Resets this Symbol
     *        Resetting a Symbols means to bring the Symbol back at the state that was the moment
     *        right after it was instantiated
     */
    public abstract void reset ();

    /**
     * @brief Use this method to give the ability to Symbols for internal changes and decisions
     *
     * @param force
     *     If there are any decisions to be made based on context, force this Symbol to take them
     */
    public abstract void reEvaluate (boolean force);

    /**
     * @brief Setter method for the confidence of this Symbol
     *
     * @param confidence
     *     The value of the confidence
     */
    public void setConfidence (double confidence) {
        confidence_ = confidence;
    }

    /**
     * @brief Getter method for the confidence of this Symbol
     *
     * @return The confidence of this Symbol
     */
    public double getConfidence () {
        return confidence_;
    }

    /**
     * @brief Getter method for the TraceGroup of this Symbol
     *
     * @return The TraceGroup of this Symbol
     */
    public TraceGroup getTraceGroup () {
        return traceGroup_;
    }

    /**
     * @brief Setter method for the parent of this Symbol
     *
     * @param parent
     *     The new parent of this Symbol
     */
    public void setParent (Symbol parent) {
        if (parent_ != null && parent_ != parent) {
            parent_.removeChild(this);
        }

        parent_ = parent;
    }

    /**
     * @brief Getter method for the parent of this Symbol
     *
     * @return The parent of this Symbol
     */
    public Symbol getParent () {
        return parent_;
    }

    /**
     * @brief Setter method for the previous Symbol of this Symbol
     *
     * @param previousSymbol
     *     The new previous Symbol of this Symbol
     */
    public void setPreviousSymbol (Symbol previousSymbol) {
        previousSymbol_ = previousSymbol;

        if (previousSymbol != null && previousSymbol.getNextSymbol() != this) {
            previousSymbol.setNextSymbol(this);
        }
    }

    /**
     * @brief Getter method for the previous Symbol of this Symbol
     *
     * @return The previous Symbol of this Symbol
     */
    public Symbol getPreviousSymbol () {
        return previousSymbol_;
    }

    /**
     * @brief Setter method for the next Symbol of this Symbol
     *
     * @param nextSymbol
     *     The new next Symbol of this Symbol
     */
    public void setNextSymbol (Symbol nextSymbol) {
        nextSymbol_ = nextSymbol;

        if (nextSymbol != null && nextSymbol.getPreviousSymbol() != this) {
            nextSymbol.setPreviousSymbol(this);
        }
    }

    /**
     * @brief Getter method for the next Symbol of this Symbol
     *
     * @return The next Symbol of this Symbol
     */
    public Symbol getNextSymbol () {
        return nextSymbol_;
    }

    /**
     * @brief Getter method for the children Symbols of this Symbol
     *
     * @return The children Symbols of this Symbol
     */
    public List<List<Symbol>> getChildren () {
        return children_;
    }

    /**
     * @brief Returns true if this Symbol has at least one child
     *
     * @return True if this Symbol has at least one child
     */
    public boolean hasChildren () {
        for (List<Symbol> samePositionChildren : children_) {
            if (samePositionChildren.size() != 0) {
                return true;
            }
        }

        return false;
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
    public ArgumentType setArgument (ArgumentPosition relativePosition, Symbol symbol) {
        if (Arrays.asList(childrenPositions_).contains(relativePosition)) {
            int index = Arrays.asList(childrenPositions_).indexOf(relativePosition);

            if (Arrays.asList(childrenClasses_[index]).contains(symbol.getClazz())) {
                int index2 = Arrays.asList(childrenClasses_[index]).indexOf(symbol.getClazz());

                if (childrenAcceptanceCriteria_[index][index2].accept(
                    this, symbol, relativePosition)) {
                    if (! children_.get(index).contains(symbol)) {
                        children_.get(index).add(symbol);
                    }

                    return ArgumentType.CHILD;
                }
                else {
                    return ArgumentType.NONE;
                }
            }
            else {
                return ArgumentType.NONE;
            }
        }
        else if (Arrays.asList(nextSymbolPositions_).contains(relativePosition)) {
            setNextSymbol(symbol);

            return ArgumentType.NEXT_SYMBOL;
        }

        return ArgumentType.NONE;
    }

    /**
     * @brief Removes a child from this Symbol
     *
     * @param symbol
     *     The child to be removed
     */
    public void removeChild (Symbol symbol) {
        for (List<Symbol> childrenList : children_) {
            for (int child = 0; child < childrenList.size(); child++) {
                if (childrenList.get(child) == symbol) {
                    childrenList.remove(symbol);

                    if (child > 0 && childrenList.get(child - 1).getNextSymbol() == symbol) {
                        childrenList.get(child - 1).setNextSymbol(null);
                    }

                    break;
                }
            }
        }

    }

    /**
     * @brief Builds the equation beginning from this Symbol
     *
     * @return The String representation of the equation
     */
    public String buildExpression () {
        String stringValue = this.toString();

        for (int i = 0; i < childrenPositions_.length; i++) {
            if (children_.get(i).size() == 0) {
                continue;
            }

            String childrenValue = children_.get(i).get(0).buildExpression();

            for (int j = 0; j < children_.get(i).size() - 1; j++) {
                if (children_.get(i).get(j).getNextSymbol() != null) {
                    childrenValue += children_.get(i).get(j).getNextSymbol().buildExpression();
                }
                else {
                    if (j <= children_.get(i).size() - 2) {
                        childrenValue += children_.get(i).get(j + 1).buildExpression();
                    }
                }
            }

            stringValue = stringValue.replace(childrenPositions_[i].toString(), childrenValue);
        }

        return (this.clearString(stringValue));
    }

    /**
     * @brief Clears a String that represents a Symbol from unneeded characters
     *
     * @param string
     *     The String to be cleared
     *
     * @return The cleared String
     */
    public String clearString (String string) {
        String result = string;

        for (ArgumentPosition argumentPosition : childrenPositions_) {
            result = result.replaceAll(
                Pattern.quote("^{") + argumentPosition + Pattern.quote("}"),"");
            result = result.replaceAll(
                Pattern.quote("_{") + argumentPosition + Pattern.quote("}"), "");
            result = result.replaceAll(
                Pattern.quote("{") + argumentPosition + Pattern.quote("}"), "");
        }

        return result;
    }

    /**
     * @class ArgumentType
     * @brief Holds all the possible types of argument that a Symbol can be to another
     */
    public enum ArgumentType {
        NONE, //!< A Symbol has no relation with another Symbol.
        CHILD, //!< A Symbol is a child of another Symbol.
        NEXT_SYMBOL //!< A Symbol is the next Symbol of another Symbol.
    }

    /**
     * @brief Returns the relative position of a given Symbol to this Symbol
     *
     * @param symbol
     *     The given Symbol
     *
     * @return The relative position of a given Symbol to this Symbol
     */
    public ArgumentPosition relativePosition (Symbol symbol) {
        TraceGroup traceGroup = getTraceGroup();
        TraceGroup symbolTraceGroup = symbol.getTraceGroup();

        int yPosition;
        if (symbolTraceGroup.getCenterOfMass().y_ < traceGroup.getBottomRightCorner().y_) {
            yPosition = - 1;
        }
        else if (symbolTraceGroup.getCenterOfMass().y_ <= traceGroup.getTopLeftCorner().y_) {
            yPosition = 0;
        }
        else {
            yPosition = 1;
        }

        int xPosition;
        if (symbolTraceGroup.getCenterOfMass().x_ < traceGroup.getTopLeftCorner().x_) {
            xPosition = - 1;
        }
        else if (symbolTraceGroup.getCenterOfMass().x_ <= traceGroup.getBottomRightCorner().x_) {
            xPosition = 0;
        }
        else {
            xPosition = 1;
        }

        if (yPosition == 1) {

            if (xPosition == - 1) {
                return ArgumentPosition.ABOVE_LEFT;
            }
            else if (xPosition == 0) {
                return ArgumentPosition.ABOVE;
            }
            else {
                return ArgumentPosition.ABOVE_RIGHT;
            }

        }
        else if (yPosition == 0) {

            if (xPosition == - 1) {
                return ArgumentPosition.LEFT;
            }
            else if (xPosition == 0) {
                if (symbolTraceGroup.getArea() > traceGroup.getArea()) {
                    return ArgumentPosition.OUTSIDE;
                }
                else {
                    return ArgumentPosition.INSIDE;
                }
            }
            else {
                return ArgumentPosition.RIGHT;
            }

        }
        else {

            if (xPosition == - 1) {
                return ArgumentPosition.BELOW_LEFT;
            }
            else if (xPosition == 0) {
                return ArgumentPosition.BELOW;
            }
            else {
                return ArgumentPosition.BELOW_RIGHT;
            }

        }
    }

    /**
     * @interface ChildAcceptanceCriterion
     * @brief Defines the API that should be implemented from a criterion that will be applied on
     *        a candidate child
     */
    public interface ChildAcceptanceCriterion {
        /**
         * @brief Returns true if the given Symbol should be accepted as a child
         *
         * @param symbol
         *     The Symbol to accept the child
         * @param child
         *     The Symbol to be accepted as a Child
         * @param relativePosition
         *     The relative position between symbol and child
         *
         * @return True if the given Symbol should be accepted as a child
         */
        boolean accept (Symbol symbol, Symbol child, ArgumentPosition relativePosition);
    }

    /**
     * @brief Implemented child acceptance criterion
     *        Accepts a child only if the candidate parent's area is, at least, twice as big as the
     *        child's.
     */
    public ChildAcceptanceCriterion sizeChildAcceptanceCriterion = new ChildAcceptanceCriterion() {
        public boolean accept (Symbol symbol, Symbol child, ArgumentPosition relativePosition) {
            return (symbol.getTraceGroup().getArea() > 2 * child.getTraceGroup().getArea());
        }
    };

    /**
     * @brief Implemented child acceptance criterion
     *        Accepts a child only if the candidate parent's area is, at least, twice as big as the
     *        child's and the candidate parent's width is, at least, twice as big as the child's.
     */
    public ChildAcceptanceCriterion sizeWidthChildAcceptanceCriterion =
        new ChildAcceptanceCriterion() {
            public boolean accept (Symbol symbol, Symbol child, ArgumentPosition relativePosition) {
                return ((symbol.getTraceGroup().getArea() > 2 * child.getTraceGroup().getArea()) &&
                    (symbol.getTraceGroup().getWidth() > 2 * child.getTraceGroup().getWidth()));
            }
        };

    /**
     * @brief Implemented child acceptance criterion
     *        Accepts a child only if the candidate parent's width is, at least, twice as big as the
     *        child's
     */
    public ChildAcceptanceCriterion widthChildAcceptanceCriterion = new ChildAcceptanceCriterion() {
        public boolean accept (Symbol symbol, Symbol child, ArgumentPosition relativePosition) {
            return (symbol.getTraceGroup().getWidth() > 2 * child.getTraceGroup().getWidth());
        }
    };

    /**
     * @brief Implemented child acceptance criterion
     *        Accepts any child no matter what.
     */
    public ChildAcceptanceCriterion allChildAcceptanceCriterion = new ChildAcceptanceCriterion() {
        public boolean accept (Symbol symbol, Symbol child, ArgumentPosition relativePosition) {
            return (true);
        }
    };

    /**
     * @brief Implemented child acceptance criterion
     *        Accepts a child only if the candidate parent's area is, at least, twice as big as the
     *        child's and the candidate parent's width is, at least, twice as big as the child's. If
     *        the child is a square root Symbol or a fraction line Symbol, it is accepted no matter
     *        what.
     */
    public ChildAcceptanceCriterion widthSizeExceptSQRTFractionLine =
        new ChildAcceptanceCriterion() {
            public boolean accept (Symbol symbol, Symbol child, ArgumentPosition relativePosition) {
                if (child.getLabel() == Labels.SQUARE_ROOT ||
                    child.getLabel() == Labels.FRACTION_LINE) {
                    return true;
                }
                else {
                    return ((symbol.getTraceGroup().getArea() > 2 * child.getTraceGroup().getArea())
                    && (symbol.getTraceGroup().getWidth() > 2 * child.getTraceGroup().getWidth()));
                }
            }
        };

    protected double confidence_ = 0; //!< The confidence that this Symbol is the right Symbol for
                                      //!< its TraceGroup

    // Make these private to force access from inheriting classes
    // only through the setters and getters
    private Symbol parent_ = null; //!< The parent of this Symbol
    private Symbol previousSymbol_ = null; //!< The Symbol before this Symbol in an equation
    private Symbol nextSymbol_ = null; //!< The Symbol after this Symbol in an equation
    private final TraceGroup traceGroup_; //!< The TraceGroup of this Symbol

    protected List<List<Symbol>> children_ = null; //!< The children Symbols of this Symbol

    protected final ArgumentPosition[] nextSymbolPositions_ = new ArgumentPosition[] {
        ArgumentPosition.RIGHT
    }; //!< The relative positions where this Symbol accepts the next Symbol

    protected ArgumentPosition[] childrenPositions_; //!< The positions where this Symbol accepts
                                                     //!< children

    protected Classes[][] childrenClasses_; //!< The classes of the children that this Symbol
                                            //!< accepts in each position it accepts children

    protected ChildAcceptanceCriterion[][] childrenAcceptanceCriteria_; //!< The criteria that each
                                                                        //!< child should meet in
                                                                        //!< order to be accepted by
                                                                        //!< this Symbol at a
                                                                        //!< specific position

}
