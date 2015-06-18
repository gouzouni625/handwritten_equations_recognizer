package main.utilities.grammars;


public class Variable extends Symbol{

  public boolean isOperator(){
    return false;
  }

  public enum Types{
    X("x"),
    Y("y");

    private Types(String stringValue){
      stringValue_ = stringValue;
    }

    public String stringValue_;
  }

  public Types type_;

}
