package org.hwer.engine.parsers;

import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.utilities.traces.TraceGroup;

/** @class Parser
 *
 *  @brief Processes a set of groups of ink traces to find their relative position an construct an equation in TeX format.
 */
public abstract class Parser{
  /**
   *  @brief Parses a given set of groups of ink traces along with its labels.
   *
   */
  public abstract void parse(Symbol[] symbols);

  /**
   *  @brief Returns a String with the equation parsed in TeX format.
   */
  public abstract String getEquation();
  public abstract void buildEquation();

  /**
   *  @brief Setter method for the silent mode of this Parser.
   *
   *  @param silent The value of the silent mode of this Parser.
   */
  public void setSilent(boolean silent){
    silent_ = silent;
  }

  /**
   *  @brief Getter method for the silent mode of this Parser.
   *
   *  @return Returns true if this Parser is in silent mode.
   */
  public boolean isSilent(){
    return silent_;
  }

  public abstract void reset();

  public synchronized Symbol[] getSymbols(){
    return symbols_;
  }

  protected boolean silent_ = true; //!< Flag for the silent mode of this Parser.

  protected Symbol[] symbols_; //!< All the symbols to be parsed by this GrammarParser.

}
