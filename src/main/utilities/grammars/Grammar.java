package main.utilities.grammars;

import main.utilities.symbols.Symbol;

public abstract class Grammar{

  public abstract void parse(Symbol symbol1, Symbol symbol2);

  public void setSilent(boolean silent){
    silent_ = silent;
  }

  public boolean isSilent(){
    return silent_;
  }

  protected boolean silent_ = true;

}
