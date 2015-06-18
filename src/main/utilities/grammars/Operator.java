package main.utilities.grammars;


public class Operator extends Symbol{

  public Operator(boolean levelChanging){
    levelChanging_ = levelChanging;
  }

  public boolean isOperator(){
    return true;
  }

  public boolean isLevelChanging(){
    return levelChanging_;
  }

  private boolean levelChanging_;

  public enum Types{
    EQUALS("="),
    PLUS("+"),
    MINUS("-");

    private Types(String stringValue){
      stringValue_ = stringValue;
    }

    public String stringValue_;
  }

}
