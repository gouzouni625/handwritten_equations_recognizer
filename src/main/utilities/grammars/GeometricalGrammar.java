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

    primary.setArgument(relativePosition, secondary);
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
        return Symbol.ArgumentPosition.ABOVE_LEFT;
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
        return Symbol.ArgumentPosition.LEFT;
      }
      else{
        return Symbol.ArgumentPosition.RIGHT;
      }

    }
    else{

      if(xPosition == -1){
        return Symbol.ArgumentPosition.BELOW_LEFT;
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
