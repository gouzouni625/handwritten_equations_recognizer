package main.utilities.grammars;

import main.utilities.grammars.Symbol;

public class Number extends Symbol{

  public boolean isOperator(){
    return false;
  }

  public enum Types{
    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9");

    private Types(String stringValue){
      stringValue_ = stringValue;
    }

    public String stringValue_;
  }

  public Types type_;

}
