package main.parsers;

import main.utilities.grammars.GeometricalGrammar;

/** Geometrical Grammar Parser.
 *
 * @author Georgios Ouzounis
 *
 */
public class GGParser extends GrammarParser{

  public GGParser(){
    grammar_ = new GeometricalGrammar();
  }

  public GGParser(GeometricalGrammar grammar){
    grammar_ = grammar;
  }

}
