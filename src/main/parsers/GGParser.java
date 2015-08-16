package main.parsers;

import main.utilities.grammars.GeometricalGrammar;

/** @class GGParser
 *
 *  @brief Implement a GrammarParser that uses a main.utilities.grammars.GeometricalGrammar.
 */
public class GGParser extends GrammarParser{
  /**
   *  @brief Default constructor.
   */
  public GGParser(){
    grammar_ = new GeometricalGrammar();
  }

}
