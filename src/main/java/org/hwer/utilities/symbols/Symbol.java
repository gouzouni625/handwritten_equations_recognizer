package org.hwer.utilities.symbols;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.hwer.utilities.traces.TraceGroup;

/** @class Symbol
 *
 *  @brief Implements an abstract Symbol.
 *
 *  A Symbol can be though as a wrapper for a TraceGroup that implements a suitable set of methods for easier parsing
 *  of equations.
 */
public abstract class Symbol{
  /**
   *  @brief Constructor.
   *
   *  @param traceGroup The TraceGroup of this Symbol.
   *  @param symbolClass The SymbolClass of this Symbol.
   */
  public Symbol(TraceGroup traceGroup, SymbolClass symbolClass){
    traceGroup_ = traceGroup;

    symbolClass_ = symbolClass;
  }

  /**
   *  @brief Processes an argument at a given position for this Symbol.
   *
   *  This method concludes on the relation between this Symbol and a given Symbol.
   *
   *  @param relativePosition The relative position of this Symbol and the given Symbol.
   *  @param symbol The given Symbol.
   *
   *  @return Returns the ArgumentType of the given Symbol for this Symbol.
   */
  public ArgumentType setArgument(ArgumentPosition relativePosition, Symbol symbol){
    // Check if this Symbol accepts a child at the given relative position.
    if(Arrays.asList(childrenPositions_).contains(relativePosition)){
      int index = Arrays.asList(childrenPositions_).indexOf(relativePosition);

      // Check if this Symbol accepts a child of the given Symbol's class at the given relative position.
      if(Arrays.asList(childrenClass_[index]).contains(symbol.symbolClass_)){
        int index2 = Arrays.asList(childrenClass_[index]).indexOf(symbol.symbolClass_);

        // Evaluate all the child acceptance criteria of this Symbol for the given relative position.
        if(childrenAcceptanceCriteria_[index][index2].accept(this, symbol, relativePosition)){
          if(!children_.get(index).contains(symbol)){
            children_.get(index).add(symbol);
          }

          // If the child acceptance criterion is met, this Symbol accepts the given Symbol as a child at the given
          // relative position.
          return ArgumentType.CHILD;
        }
        else{
          // If the child acceptance criterion is not met, then, this Symbol has no relation with the given Symbol at
          // the given relative position.
          return ArgumentType.NONE;
        }
      }
      else{
        // If this symbol doesn't accept a child of the given Symbol's class at the given relative position, then, this
        // Symbol has no relation with the given Symbol at the given relative position.
        return ArgumentType.NONE;
      }
    }
    // If this Symbol doesn't accept a child at the given relative position, check the next Symbol position(s).
    else if(Arrays.asList(nextSymbolPositions_).contains(relativePosition)){
      nextSymbol_ = symbol;

      return ArgumentType.NEXT_SYMBOL;
    }

    return ArgumentType.NONE;
  }

  /**
   *  @brief Removes a child from this Symbol.
   *
   *  @param symbol The child to be removed.
   */
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

  /**
   *  @brief Sets the parent of this Symbol.
   *
   *  @param symbol The Symbol to be set as parent for this Symbol.
   */
  public void setParent(Symbol symbol){
    // If this Symbol has already a parent and the new parent is different that the current one(meaning that the parent is
    // being changed), then, the old parent should remove this Symbol from being a child.
    if(parent_ != null && parent_ != symbol){
      parent_.removeChild(this);
    }

    parent_ = symbol;
  }

  /**
   *  @brief Returns the String representation of this Symbol.
   *
   *  @return Returns the String representation of this Symbol.
   */
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

  /**
   *  @brief Clears a String that represents a Symbol from unneeded characters.
   *
   *  @param string The String to be cleared.
   *
   *  @return Returns the cleared String.
   */
  public String clearString(String string){
    String result = new String(string);

    for(ArgumentPosition argumentPosition : childrenPositions_){
      result = result.replaceAll(Pattern.quote("^{") + argumentPosition + Pattern.quote("}"), "");
      result = result.replaceAll(Pattern.quote("_{") + argumentPosition + Pattern.quote("}"), "");
      result = result.replaceAll(Pattern.quote("{") + argumentPosition + Pattern.quote("}"), "");
      result = result.replaceAll(Pattern.quote("{") + argumentPosition + Pattern.quote("}"), "");
    }

    return result;
  }

  /** @class SymbolClass
   *
   *  @brief Holds all the possible types of Symbols. Concretely, for each class extending Symbol class, a new entry
   *  should be added in SymbolClass.
   */
  public enum SymbolClass{
    NUMBER, //!< Number class entry.
    LETTER, //!< Letter class entry.
    OPERATOR, //!< Operator class entry.
    UNRECOGNIZED; //!< UnrecognizedSymbol class entry.
  }

  /** @class ArgumentType
   *
   *  @brief Holds all the possible types of argument that a Symbol can be to another.
   */
  public enum ArgumentType{
    NONE, //!< A Symbol has no relation with another Symbol.
    CHILD, //!< A Symbol is a child of another Symbol.
    NEXT_SYMBOL; //!< A Symbol is the next Symbol of another Symbol.
  }

  /** @class ArgumentPosition
   *
   *  @brief Holds all the possible relative positions between two Symbol objects.
   */
  public enum ArgumentPosition{
    ABOVE, //!< A Symbol is above another Symbol.
    ABOVE_RIGHT, //!< A Symbol is above and right of another Symbol.
    RIGHT, //!< A Symbol is right of another Symbol.
    BELOW_RIGHT, //!< A Symbol is below and right of another Symbol.
    BELOW, //!< A Symbol is below another Symbol.
    BELOW_LEFT, //!< A Symbol is below and left of another Symbol.
    LEFT, //!< A Symbol is left of another Symbol.
    ABOVE_LEFT, //!< A Symbol is above and left of another Symbol.
    INSIDE, //!< A Symbol is inside another Symbol.
    OUTSIDE; //!< A Symbol is outside of another Symbol.

    /**
     *  @brief Returns the opposite of a given ArgumentPosition.
     *
     *  @param position The given ArgumentPosition.
     *
     *  @return Returns the opposite of the given ArgumentPosition.
     */
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

  /**
   *  @brief Finds the relative position between this Symbol and a given Symbol.
   *
   *  @param symbol The given Symbol.
   *
   *  @return Returns the relative position between this Symbol and the given one.
   */
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

  /**
   *  @brief Re evaluates the type of this Symbol.
   *
   *  This method is used by the UnrecognizedSymbol class to determine the type of the Symbol after some children have
   *  been found.
   */
  public void reEvaluate(){}

  /** @class ChildAcceptanceCriterion
   *
   *  @brief An Interface that describes a criterion on whether to accept a child or not.
   *
   *  This interface should be implemented to describe a criterion for accepting a child at a specified position.
   */
  public interface ChildAcceptanceCriterion{
    /**
     *  @brief Returns true if the given Symbol should be accepted as a Child.
     *
     *  @param symbol The Symbol that accepts a child.
     *  @param child The Symbol to be accepted as a Child.
     *  @param relativePosition The relative position between symbol and child.
     *
     *  @return Returns true if symbol should accept child as a child.
     */
    public boolean accept(Symbol symbol, Symbol child, ArgumentPosition relativePosition);
  };

  /**
   *  @brief Implemented child acceptance criterion.
   *
   *  Accepts a child only if the candidate parent's area is, at least, twice as big as the child's.
   */
  public ChildAcceptanceCriterion sizeChildAcceptanceCriterion = new ChildAcceptanceCriterion(){
    public boolean accept(Symbol symbol, Symbol child, ArgumentPosition relativePosition){
      return (symbol.traceGroup_.getArea() > 2 * child.traceGroup_.getArea());
    }
  };

  /**
   *  @brief Implemented child acceptance criterion.
   *
   *  Accepts a child only if the candidate parent's area is, at least, twice as big as the child's and the candidate
   *  parent's width is, at least, twice as big as the child's.
   */
  public ChildAcceptanceCriterion sizeWidthChildAcceptanceCriterion = new ChildAcceptanceCriterion() {
    public boolean accept(Symbol symbol, Symbol child, ArgumentPosition relativePosition){
      return ((symbol.traceGroup_.getArea() > 2 * child.traceGroup_.getArea()) &&
              (symbol.traceGroup_.getWidth() > 2 * child.traceGroup_.getWidth()));
    }
  };

  /**
   *  @brief Implemented child acceptance criterion.
   *
   *  Accepts a child only if the candidate parent's width is, at least, twice as big as the child's.
   */
  public ChildAcceptanceCriterion widthChildAcceptanceCriterion = new ChildAcceptanceCriterion() {
    public boolean accept(Symbol symbol, Symbol child, ArgumentPosition relativePosition){
      return (symbol.traceGroup_.getWidth() > 2 * child.traceGroup_.getWidth());
    }
  };

  /**
   *  @brief Implemented child acceptance criterion.
   *
   *  Accepts any child no matter what.
   */
  public ChildAcceptanceCriterion allChildAcceptanceCriterion = new ChildAcceptanceCriterion(){
    public boolean accept(Symbol symbol, Symbol child, ArgumentPosition relativePosition){
      return (true);
    }
  };

  /**
   *  @brief Implemented child acceptance criterion.
   *
   *  Accepts a child only if the candidate parent's area is, at least, twice as big as the child's and the candidate
   *  parent's width is, at least, twice as big as the child's. If the child is a square root Symbol or a fraction line
   *  Symbol, it is accepted no matter what.
   */
  public ChildAcceptanceCriterion widthSizeExceptSQRTFractionLine = new ChildAcceptanceCriterion(){
    public boolean accept(Symbol symbol, Symbol child, ArgumentPosition relativePosition){
      if(child.type_ == Operator.Types.SQRT || child.type_ == Operator.Types.FRACTION_LINE){
        return true;
      }
      else{
        return ((symbol.traceGroup_.getArea() > 2 * child.traceGroup_.getArea()) &&
                (symbol.traceGroup_.getWidth() > 2 * child.traceGroup_.getWidth()));
      }
    }
  };

  public SymbolClass symbolClass_; //!< The class of this Symbol. Is set by every class that extends Symbol class.

  public TraceGroup traceGroup_; //!< The TraceGroup of this Symbol.

  public Symbol parent_; //!< The parent of this Symbol.

  public List<List<Symbol>> children_; //!< The children of this Symbol. A List of children for every children Position.
  public ArgumentPosition[] childrenPositions_; //!< The positions where this Symbol accepts children.
  public SymbolClass[][] childrenClass_; /**< The accepted class for children of this Symbol.
                                              Each children position can have multiple accepted classes.*/
  public ChildAcceptanceCriterion[][] childrenAcceptanceCriteria_; /**< Acceptance criteria for children. One acceptance
                                                                        criterion for each accepted children class at a
                                                                        specific children position. */

  public Symbol nextSymbol_; //!< The next Symbol after this Symbol. This is used when transforming the equation in TeX.
  public ArgumentPosition[] nextSymbolPositions_; //!< The positions where this Symbol accepts a next Symbol.

  public Enum<?> type_; /**< The type of this Symbol. This variable is here to force each class that extends Symbol class
                             to have an enumeration of all types of symbols it implements. For example, Number class
                             implements symbols ZERO, ONE, TWO, ... */

}
