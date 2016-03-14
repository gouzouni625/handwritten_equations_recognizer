package org.hwer.engine.symbols.letters;

import org.hwer.engine.parsers.grammars.GeometricalGrammar.ArgumentPosition;
import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.utilities.traces.TraceGroup;

import java.util.ArrayList;
import java.util.List;

/** @class Letter
 *
 *  @brief Implements a Letter as a Symbol.
 */
public abstract class Letter extends Symbol {
  /**
   *  @brief Constructor.
   *
   *  @param traceGroup The TraceGroup of this Letter.
   */
  public Letter(TraceGroup traceGroup){
    super(traceGroup);

      children_ = new ArrayList<List<Symbol>>();
      childrenPositions_ = new ArgumentPosition[] {};
      childrenClasses_ = new Classes[][] {};
      childrenAcceptanceCriteria_ = new ChildAcceptanceCriterion[][] {};
 }

    public Classes getClazz(){
        return Classes.LETTER;
    }

    public String toString(String symbolString){
        return symbolString;
    }

}
