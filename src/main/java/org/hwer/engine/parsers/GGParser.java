package org.hwer.engine.parsers;

import org.hwer.engine.utilities.grammars.GeometricalGrammar;

/** @class GGParser
 *
 *  @brief Implement a GrammarParser that uses a main.java.utilities.grammars.GeometricalGrammar.
 */
public class GGParser extends GrammarParser{
  /**
   *  @brief Default constructor.
   */
  public GGParser(){
    grammar_ = new GeometricalGrammar();
  }

}
