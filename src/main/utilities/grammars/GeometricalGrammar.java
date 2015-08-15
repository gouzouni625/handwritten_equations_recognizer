package main.utilities.grammars;

import main.utilities.symbols.Symbol;

/** @class GeometricalGrammar
 *
 *  @brief Implements a GeometricalGrammar.
 */
public class GeometricalGrammar extends Grammar{
  /**
   *  @brief Parses two Symbol objects.
   *
   *  This method parses two Symbol objects and sets the relations between them.
   */
  public void parse(Symbol primary, Symbol secondary){
    // Find the relative position of the two symbols.
    Symbol.ArgumentPosition relativePosition = primary.relativePosition(secondary);

    /* ===== Logs ===== */
    if(!quiet_){
      outputStream_.println("Log: relative position... ===== Start =====");

      outputStream_.println("Relative position between: " + primary + ", " + secondary + " : " + relativePosition);

      outputStream_.println("Log: relative position... ===== End =======");
    }
    /* ===== Logs ===== */

    Symbol.ArgumentType argumentType = primary.setArgument(relativePosition, secondary);
    switch(argumentType){
      case CHILD:
        // If secondary Symbol is a child to the primary, then set primary to be the parent of secondary.
        secondary.setParent(primary);
        break;
      case NEXT_SYMBOL:
        // If secondary is nextSymbol of primary, check the relationship between primary's parent and secondary.
        if(primary.parent_ != null){
          relativePosition = primary.parent_.relativePosition(secondary);
          argumentType = primary.parent_.setArgument(relativePosition, secondary);

          switch(argumentType){
            case CHILD:
              // If secondary is child to primary's parent, then set primary's parent to be secondary's parent too.
              // The fact that secondary is nextSymbol to the primary and that secondary is child to primary's parent
              // is an accepted situation.
              // TODO Maybe the GeometricalGrammar should assert that secondary is child to primary's parent at the same
              // childrenPosition as primary.
              secondary.setParent(primary.parent_);
            case NONE:
              break;
            case NEXT_SYMBOL:
              // If seconday is nextSymbol to primary's parent, then it is not nextSymbol to primary.
              // An example would be sqrt{3x + 6} + 2. + Symbol is nextSymbol to 6 but is also next Symbol to sqrt, that
              // is why it is accepted as nextSymbol only to sqrt.
              primary.nextSymbol_ = null;
              break;
          }
        }
        break;
      case NONE:
        // If secondary is nothing to primary, check the relationship between secondary and primary's parent. If
        // secondary is nothing to primary's parent also, check the relationship between secondary and primary's parent's
        // parent. The loop goes on, up until the first parent of all...
        // If this while loop goes to the last parent, and no relation is found between the parent
        // and secondary, then, the input is unrecognizable and the result produced is garbage.
        if(primary.parent_ != null){
          /* ===== Logs ===== */
          if(!quiet_){
            outputStream_.println("Log: Checking relative position with parent... ===== Start - Stop =====");
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
