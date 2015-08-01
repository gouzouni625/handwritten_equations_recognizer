package main.utilities.grammars;

public class GeometricalGrammar extends Grammar{

  public void parse(Symbol primary, Symbol secondary){
    // Find the relative position of the two symbols.
    Symbol.ArgumentPosition relativePosition = primary.relativePosition(secondary);

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
          relativePosition = primary.parent_.relativePosition(secondary);
          primary.parent_.setArgument(relativePosition, secondary);
          secondary.setParent(primary.parent_);
        }
        if(secondary.parent_ != null){
          relativePosition = secondary.parent_.relativePosition(primary);
          secondary.parent_.setArgument(relativePosition, primary);
          primary.setParent(secondary.parent_);
        }
        break;
      case NONE:
        // If this while loop goes to the last parent, and no relation is found between the parent
        // and secondary, then, the input is unrecognizable and the result produced is garbage.
        if(primary.parent_ != null){
          /* ===== Logs ===== */
          if(!silent_){
            System.out.println("Log: Checking relative position with parent... ===== Start - Stop =====");
          }
          /* ===== Logs ===== */

          this.parse(primary.parent_, secondary);
        }
        break;
      default:
        break;
    }
  }

}
