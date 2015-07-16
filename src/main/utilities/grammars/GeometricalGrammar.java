package main.utilities.grammars;

public class GeometricalGrammar extends Grammar{

  public void parse(Symbol primary, Symbol secondary){
    // Find the relative position of the two symbols.
    Symbol.ArgumentPosition relativePosition = this.relativePosition(primary, secondary);

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: relative position... ===== Start =====");

      System.out.println("Relative position between: " + primary + ", " + secondary + " : " + relativePosition);

      System.out.println("Log: relative position... ===== End =======");
    }
    /* ===== Logs ===== */

    Symbol.ArgumentType argumentType = primary.setArgument(relativePosition, secondary);
    switch(argumentType){
      case CHILD:
        secondary.setParent(primary);
        break;
      case NEXT_SYMBOL:
        if(primary.parent_ != null){
          // The relative position should be found such, so that secondary becomes the child of
          // primary's parent. That is because, secondary is the next symbol of primary and thus
          // they have the same parent(if any).
          relativePosition = this.relativePosition(primary.parent_, secondary);
          primary.parent_.setArgument(relativePosition, secondary);
          secondary.setParent(primary.parent_);
        }
        break;
      case NONE:
        // If this while loop goes to the last parent, and no relation is found between the parent
        // and secondary, then, the input is unrecognizable and the result produced is garbage.
        while(primary.parent_ != null){
          relativePosition = this.relativePosition(primary.parent_, secondary);

          /* ===== Logs ===== */
          if(!silent_){
            System.out.println("Log: parent relative position... ===== Start =====");

            System.out.println("Relative position between: " + primary.parent_ + ", " + secondary + " : " + relativePosition);

            System.out.println("Log: parent relative position... ===== End =======");
          }
          /* ===== Logs ===== */

          argumentType = primary.parent_.setArgument(relativePosition, secondary);
          if(argumentType == Symbol.ArgumentType.NONE){
            primary = primary.parent_;
          }
          else if(argumentType == Symbol.ArgumentType.CHILD){
            secondary.setParent(primary.parent_);
            break;
          }
          else{
            break;
          }
        }
        break;
      default:
        break;
    }
  }

  public Symbol.ArgumentPosition relativePosition(Symbol primary, Symbol secondary){
    primary.traceGroup_.calculateCorners();
    secondary.traceGroup_.calculateCorners();

    int yPosition;
    if(secondary.traceGroup_.getBottomRightCorner().y_ >= primary.traceGroup_.getBottomRightCorner().y_ + primary.traceGroup_.getHeight() / 2){
      yPosition = 1;
    }
    else if(secondary.traceGroup_.getBottomRightCorner().y_ <= primary.traceGroup_.getBottomRightCorner().y_ + primary.traceGroup_.getHeight() / 2 &&
            secondary.traceGroup_.getTopLeftCorner().y_ >= primary.traceGroup_.getBottomRightCorner().y_ + primary.traceGroup_.getHeight() / 2){
      yPosition = 0;
    }
    else{
      yPosition = -1;
    }

    int xPosition;
    if(secondary.traceGroup_.getTopLeftCorner().x_ >= primary.traceGroup_.getTopLeftCorner().x_ + primary.traceGroup_.getWidth() / 2){
      xPosition = 1;
    }
    else if(secondary.traceGroup_.getTopLeftCorner().x_ <= primary.traceGroup_.getTopLeftCorner().x_ + primary.traceGroup_.getWidth() / 2 &&
            secondary.traceGroup_.getBottomRightCorner().x_ >= primary.traceGroup_.getTopLeftCorner().x_ + primary.traceGroup_.getWidth() / 2){
      xPosition = 0;
    }
    else{
      xPosition = -1;
    }

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: yPosition and xPosition... ===== Start =====");

      System.out.println("yPosition: " + yPosition);
      System.out.println("xPosition: " + xPosition);

      System.out.println("Log: yPosition and xPosition... ===== End =======");
    }
    /* ===== Logs ===== */

    if(yPosition == 1){

      if(xPosition == -1){
        return null; //Symbol.ArgumentPosition.ABOVE_LEFT;
      }
      else if(xPosition == 0){
        return Symbol.ArgumentPosition.ABOVE;
      }
      else{
        return Symbol.ArgumentPosition.ABOVE_RIGHT;
      }

    }
    else if(yPosition == 0){

      if(xPosition == -1){
        return null; //Symbol.ArgumentPosition.LEFT;
      }
      else{
        return Symbol.ArgumentPosition.RIGHT;
      }

    }
    else{

      if(xPosition == -1){
        return null; //Symbol.ArgumentPosition.BELOW_LEFT;
      }
      else if(xPosition == 0){
        return Symbol.ArgumentPosition.BELOW;
      }
      else{
        return Symbol.ArgumentPosition.BELOW_RIGHT;
      }

    }
  }

}
