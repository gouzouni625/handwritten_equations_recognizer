package main.utilities.grammars;


public class GeometricalGrammar{

  public static void parse(Symbol symbol1, Symbol symbol2){

    symbol1.traceGroup_.calculateCorners();
    symbol2.traceGroup_.calculateCorners();

    double symbol1Area = symbol1.traceGroup_.getWidth() * symbol1.traceGroup_.getHeight();
    double symbol2Area = symbol2.traceGroup_.getWidth() * symbol2.traceGroup_.getHeight();

    Symbol big;
    Symbol small;

    if(symbol1Area >= symbol2Area){
      big = symbol1;
      small = symbol2;
    }
    else{
      big = symbol2;
      small = symbol1;
    }

    if(big.getClass() == Number.class && small.getClass() == Number.class){
      Symbol.ArgumentPosition relativePosition = GeometricalGrammar.relativePosition(big, small);
      switch(relativePosition){
      case ABOVE_RIGHT:
        big.arguments_[0] = small;
        break;
      case BELOW_RIGHT:
        big.arguments_[1] = small;
        break;
      default:
        break;
      }
    }
  }

  public static Symbol.ArgumentPosition relativePosition(Symbol big, Symbol small){
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
