package main.utilities.grammars;

public class GeometricalGrammar extends Grammar{

  public void parse(Symbol symbol1, Symbol symbol2){
    // Find the relative position of the two symbols.
    Symbol.ArgumentPosition relativePosition = this.relativePosition(symbol1, symbol2);

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: relative position... ===== Start =====");

      System.out.println("Relative position between: " + symbol1 + ", " + symbol2 + " : " + relativePosition);

      System.out.println("Log: relative position... ===== End =======");
    }
    /* ===== Logs ===== */

    symbol1.setArgument(relativePosition, symbol2);
    symbol2.setArgument(this.opositePosition(relativePosition), symbol1);
  }

  public Symbol.ArgumentPosition relativePosition(Symbol symbol1, Symbol symbol2){
    symbol1.traceGroup_.calculateCorners();
    symbol2.traceGroup_.calculateCorners();

    int yPosition;
    if(symbol2.traceGroup_.getBottomRightCorner().y_ >= symbol1.traceGroup_.getBottomRightCorner().y_ + symbol1.traceGroup_.getHeight() / 2){
      yPosition = 1;
    }
    else if(symbol2.traceGroup_.getBottomRightCorner().y_ <= symbol1.traceGroup_.getBottomRightCorner().y_ + symbol1.traceGroup_.getHeight() / 2 &&
            symbol2.traceGroup_.getTopLeftCorner().y_ >= symbol1.traceGroup_.getBottomRightCorner().y_ + symbol1.traceGroup_.getHeight() / 2){
      yPosition = 0;
    }
    else{
      yPosition = -1;
    }

    int xPosition;
    if(symbol2.traceGroup_.getTopLeftCorner().x_ >= symbol1.traceGroup_.getTopLeftCorner().x_ + symbol1.traceGroup_.getWidth() / 2){
      xPosition = 1;
    }
    else if(symbol2.traceGroup_.getTopLeftCorner().x_ <= symbol1.traceGroup_.getTopLeftCorner().x_ + symbol1.traceGroup_.getWidth() / 2 &&
            symbol2.traceGroup_.getBottomRightCorner().x_ >= symbol1.traceGroup_.getTopLeftCorner().x_ + symbol1.traceGroup_.getWidth() / 2){
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

  public Symbol.ArgumentPosition opositePosition(Symbol.ArgumentPosition position){
    switch(position){
      case ABOVE:
        return Symbol.ArgumentPosition.BELOW;
      case ABOVE_RIGHT:
        return Symbol.ArgumentPosition.BELOW_LEFT;
      case RIGHT:
        return Symbol.ArgumentPosition.LEFT;
      case BELOW_RIGHT:
        return Symbol.ArgumentPosition.ABOVE_LEFT;
      case BELOW:
        return Symbol.ArgumentPosition.ABOVE;
      case BELOW_LEFT:
        return Symbol.ArgumentPosition.ABOVE_RIGHT;
      case LEFT:
        return Symbol.ArgumentPosition.RIGHT;
      case ABOVE_LEFT:
        return Symbol.ArgumentPosition.BELOW_RIGHT;
      default:
        return null;
    }
  }
}
