package org.hwer.engine.symbols.letters;

import org.hwer.engine.symbols.Symbol;
import org.hwer.engine.symbols.SymbolFactory.Classes;
import org.hwer.engine.symbols.SymbolFactory.SymbolClass;
import org.hwer.engine.utilities.traces.TraceGroup;

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
 }

    public Classes getClazz(){
        return Classes.LETTER;
    }

    public String toString(String symbolString){
        return symbolString;
    }

}
