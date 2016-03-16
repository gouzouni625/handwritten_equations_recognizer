package org.hwer.engine.symbols;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.hwer.engine.symbols.SymbolFactory.Labels;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.symbols.SymbolFactory.SymbolClass;
import org.hwer.engine.utilities.traces.TraceGroup;
import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;

/** @class Symbol
 *
 *  @brief Implements an abstract Symbol.
 *
 *  A Symbol can be though as a wrapper for a TraceGroup that implements a suitable set of methods for easier parsing
 *  of equations.
 */
public abstract class Symbol implements SymbolClass {
  /**
   *  @brief Constructor.
   *
   *  @param traceGroup The TraceGroup of this Symbol.
   */
  public Symbol(TraceGroup traceGroup){
    traceGroup_ = traceGroup;

    reset();
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
      if(Arrays.asList(childrenClasses_[index]).contains(symbol.getClazz())){
        int index2 = Arrays.asList(childrenClasses_[index]).indexOf(symbol.getClazz());

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
      setNextSymbol(symbol);

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

          if(child > 0 && childrenList.get(child - 1).getNextSymbol() == symbol){
            childrenList.get(child - 1).setNextSymbol(null);
          }

          break;
        }
      }
    }

  }

  /**
   *  @brief Returns the String representation of this Symbol.
   *
   *  @return Returns the String representation of this Symbol.
   */
  public String buildExpression(){
    String stringValue = this.toString();

    for(int i = 0;i < childrenPositions_.length;i++){
      if(children_.get(i).size() == 0){
        continue;
      }

      String childrenValue = children_.get(i).get(0).buildExpression();

      for(int j = 0;j < children_.get(i).size() - 1;j++){
        if(children_.get(i).get(j).getNextSymbol() != null){
          childrenValue += children_.get(i).get(j).getNextSymbol().buildExpression();
        }
        else{
          if(j <= children_.get(i).size() - 2){
            childrenValue += children_.get(i).get(j + 1).buildExpression();
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
    String result = string;

    for(ArgumentPosition argumentPosition : childrenPositions_){
      result = result.replaceAll(Pattern.quote("^{") + argumentPosition + Pattern.quote("}"), "");
      result = result.replaceAll(Pattern.quote("_{") + argumentPosition + Pattern.quote("}"), "");
      result = result.replaceAll(Pattern.quote("{") + argumentPosition + Pattern.quote("}"), "");
    }

    return result;
  }

  /** @class ArgumentType
   *
   *  @brief Holds all the possible types of argument that a Symbol can be to another.
   */
  public enum ArgumentType{
    NONE, //!< A Symbol has no relation with another Symbol.
    CHILD, //!< A Symbol is a child of another Symbol.
    NEXT_SYMBOL //!< A Symbol is the next Symbol of another Symbol.
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
          return ArgumentPosition.OUTSIDE;
        }
        else{
          return ArgumentPosition.INSIDE;
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
     boolean accept(Symbol symbol, Symbol child, ArgumentPosition relativePosition);
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
      if(child.getLabel() == Labels.SQUARE_ROOT || child.getLabel() == Labels.FRACTION_LINE){
        return true;
      }
      else{
        return ((symbol.traceGroup_.getArea() > 2 * child.traceGroup_.getArea()) &&
                (symbol.traceGroup_.getWidth() > 2 * child.traceGroup_.getWidth()));
      }
    }
  };

  public abstract Labels getLabel();

  public abstract String toString();

  public abstract void reset();

  // Use this method to give the ability to symbols for internal changes.
  public abstract void reEvaluate(boolean force);

  public void setConfidence(double confidence){
    confidence_ = confidence;
  }

  public double getConfidence(){
    return confidence_;
  }

  public TraceGroup getTraceGroup(){
    return traceGroup_;
  }

  /**
   *  @brief Sets the parent of this Symbol.
   *
   *  @param parent The Symbol to be set as parent for this Symbol.
   */
  public void setParent(Symbol parent){
    // If this Symbol has already a parent and the new parent is different that the current one(meaning that the parent is
    // being changed), then, the old parent should remove this Symbol from being a child.
    if(parent_ != null && parent_ != parent){
      parent_.removeChild(this);
    }

    parent_ = parent;
  }

  public Symbol getParent(){
    return parent_;
  }

  public void setPreviousSymbol(Symbol previousSymbol){
    previousSymbol_ = previousSymbol;

    if(previousSymbol != null && previousSymbol.getNextSymbol() != this){
      previousSymbol.setNextSymbol(this);
    }
  }

  public Symbol getPreviousSymbol(){
    return previousSymbol_;
  }

  public void setNextSymbol(Symbol nextSymbol){
    nextSymbol_ = nextSymbol;

    if(nextSymbol != null && nextSymbol.getPreviousSymbol() != this){
      nextSymbol.setPreviousSymbol(this);
    }
  }

  public Symbol getNextSymbol(){
    return nextSymbol_;
  }

  public List<List<Symbol>> getChildren(){
    return children_;
  }


  protected double confidence_ = 0; // The confidence that this symbol is the symbol it says it is.

  protected TraceGroup traceGroup_ = null; //!< The TraceGroup of this Symbol.

  // Make these private to force access only through the setters and getters in inheriting classes.
  private Symbol parent_ = null; //!< The parent of this Symbol.
  private Symbol previousSymbol_ = null;
  private Symbol nextSymbol_ = null; //!< The next Symbol after this Symbol. This is used when transforming the equation in TeX.

  protected final ArgumentPosition[] nextSymbolPositions_ = new ArgumentPosition[] {ArgumentPosition.RIGHT};

  protected List<List<Symbol>> children_ = null; //!< The children of this Symbol. A List of children for every children Position.

  protected ArgumentPosition[] childrenPositions_; //!< The positions where this Symbol accepts children.
  protected Classes[][] childrenClasses_; /**< The accepted class for children of this Symbol.
                                              Each children position can have multiple accepted classes.*/
  protected ChildAcceptanceCriterion[][] childrenAcceptanceCriteria_; /**< Acceptance criteria for children. One acceptance
                                                                        criterion for each accepted children class at a
                                                                        specific children position. */

}
