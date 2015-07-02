package main.utilities.grammars;

public class GeometricalGrammar extends Grammar{

  public void parse(Symbol symbol1, Symbol symbol2){
    symbol1.traceGroup_.calculateCorners();
    symbol2.traceGroup_.calculateCorners();

    double symbol1Area = symbol1.traceGroup_.getWidth() * symbol1.traceGroup_.getHeight();
    double symbol2Area = symbol2.traceGroup_.getWidth() * symbol2.traceGroup_.getHeight();

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: symbols' areas... ===== Start =====");

      System.out.println("Symbol 1 area: " + symbol1Area);
      System.out.println("Symbol 2 area: " + symbol2Area);

      System.out.println("Log: symbols' areas... ===== End =======");
    }
    /* ===== Logs ===== */

    Symbol big;
    Symbol small;

    if(symbol1Area >= symbol2Area * 0.60){
      big = symbol1;
      small = symbol2;
    }
    else if(symbol2Area >= symbol1Area * 0.60){
      big = symbol2;
      small = symbol1;
    }
    else{
      // There is no distinction between the big and the small symbol.
      big = symbol1;
      small = symbol2;
    }

    /* ===== Logs ===== */
    if(!silent_){
      System.out.println("Log: big and small symbols... ===== Start =====");

      System.out.println("Big: " + big);
      System.out.println("Small: " + small);

      System.out.println("Log: big and small symbols... ===== End =======");
    }
    /* ===== Logs ===== */

    // Find the relative position of the two symbols.
    Symbol.ArgumentPosition relativePosition = this.relativePosition(big, small);

    big.setArgument(relativePosition, small);
  }

  public Symbol.ArgumentPosition relativePosition(Symbol big, Symbol small){
    big.traceGroup_.calculateCorners();
    small.traceGroup_.calculateCorners();

    int yPosition;
    if(small.traceGroup_.getBottomRightCorner().y_ >= big.traceGroup_.getBottomRightCorner().y_ + big.traceGroup_.getHeight() / 2){
      yPosition = 1;
    }
    else if(small.traceGroup_.getBottomRightCorner().y_ <= big.traceGroup_.getBottomRightCorner().y_ + big.traceGroup_.getHeight() / 2 &&
            small.traceGroup_.getTopLeftCorner().y_ >= big.traceGroup_.getBottomRightCorner().y_ + big.traceGroup_.getHeight() / 2){
      yPosition = 0;
    }
    else{
      yPosition = -1;
    }

    int xPosition;
    if(small.traceGroup_.getTopLeftCorner().x_ >= big.traceGroup_.getTopLeftCorner().x_ + big.traceGroup_.getWidth() / 2){
      xPosition = 1;
    }
    else if(small.traceGroup_.getTopLeftCorner().x_ <= big.traceGroup_.getTopLeftCorner().x_ + big.traceGroup_.getWidth() / 2 &&
            small.traceGroup_.getBottomRightCorner().x_ >= big.traceGroup_.getTopLeftCorner().x_ + big.traceGroup_.getWidth() / 2){
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
