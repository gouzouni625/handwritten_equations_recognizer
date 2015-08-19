package main.java.utilities.data_managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import main.java.utilities.inkml.InkMLParser;

public class FillDatabase{
  public static void main(String[] args) throws FileNotFoundException, SQLException{
    MySQLDataManager dataManager = new MySQLDataManager(DATABASE, DATABASE_USERNAME, DATABASE_PASSWORD, DATABASE_TABLE, null, null);

    InkMLParser inkMLParser = new InkMLParser();
    PartitionParser partitionParser = new PartitionParser();

    File dataFile = new File(DATA_PATH);
    String[] equationDirectoriesName = dataFile.list();
    int equationProcessedCounter = 0;
    for(String equationDirectoryName : equationDirectoriesName){
      System.out.println("************************* PROCESSING EQUATION: " + equationDirectoryName + " *****************************");
      System.out.println("************************* EQUATIONS PROCESSED: " + equationProcessedCounter + " *************************");

      File equationDirectory = new File(DATA_PATH + "/" + equationDirectoryName);

      if(!equationDirectory.isDirectory()){
        continue;
      }

      inkMLParser.parse(new File(DATA_PATH + "/" + equationDirectory.getName() + "/" + equationDirectory.getName() + ".xml"));
      partitionParser.parse(new File(DATA_PATH + "/" + equationDirectory.getName() + "/" + equationDirectory.getName() + ".xml.partition"));

      String[] databaseValues = new String[5];
      databaseValues[0] = new String("null"); // set id field.
      databaseValues[2] = equationDirectoryName.replaceAll("equation", ""); // set equation_number field.
      for(int i = 0;i < partitionParser.traceGroups_.size();i++){
        databaseValues[1] = new String("\"");
        databaseValues[3] = new String("");
        databaseValues[4] = new String("");

        for(int j = 0;j < partitionParser.traceGroups_.get(i).size();j++){
          databaseValues[1] += inkMLParser.traceGroup_.get(partitionParser.traceGroups_.get(i).get(j)).toInkMLFormat();
        }
        databaseValues[1] += "\"";

        databaseValues[3] = partitionParser.labels_.get(i) + "";
        databaseValues[4] = "\"" + Symbol.intValueToString(partitionParser.labels_.get(i)) + "\"";

        dataManager.saveToDatabase(databaseValues);
      }

      equationProcessedCounter++;
    }
  }

  private static class PartitionParser{
    public PartitionParser(){
      traceGroups_ = new ArrayList<ArrayList<Integer>>();

      labels_ = new ArrayList<Integer>();
    }

    public void parse(File file) throws FileNotFoundException{
      this.reset();

      Scanner scanner = new Scanner(file);
      scanner.useDelimiter("\n");

      String line = new String("");

      // Read the number of traces.
      if(scanner.hasNextLine()){
        scanner.nextLine();
      }

      // Read the rest of the data.
      while(scanner.hasNextLine()){
        line = scanner.nextLine();

        ArrayList<Integer> lineList = new ArrayList<Integer>();

        String[] lineData = line.split(", ");

        for(int i = 0;i < lineData.length - 2;i++){
          lineList.add(Integer.parseInt(lineData[i]));
        }
        traceGroups_.add(lineList);

        int label = Symbol.stringValueToIntValue(lineData[lineData.length - 1]);
        labels_.add(label);
      }

      scanner.close();
    }

    private void reset(){
      traceGroups_ = new ArrayList<ArrayList<Integer>>();
      labels_ = new ArrayList<Integer>();
    }

    public ArrayList<ArrayList<Integer>> traceGroups_;
    public ArrayList<Integer> labels_;
  }

  public static final String DATABASE = "jdbc:mysql://localhost/website_db";
  public static final String DATABASE_USERNAME = "website_user";
  public static final String DATABASE_PASSWORD = "ValmadiaN";
  public static final String DATABASE_TABLE = "xml_data";

  public static final String DATA_PATH = "/home/george/Desktop/workspace/github/repositories/symbol_extractor/data/final_data/";

  public enum Symbol{
    ZERO(0, "0"),
    ONE(1, "1"),
    TWO(2, "2"),
    THREE(3, "3"),
    FOUR(4, "4"),
    FIVE(5, "5"),
    SIX(6, "6"),
    SEVEN(7, "7"),
    EIGHT(8, "8"),
    NINE(9, "9"),
    PLUS(10, "+"),
    EQUALS(11, "="),
    LOWER_X(12, "x"),
    LOWER_Y(13, "y"),
    HORIZONTAL_LINE(14, "_"),
    SQUARE_ROOT(15, "sqrt"),
    LEFT_PARENTHESIS(16, "left_par"),
    RIGHT_PARENTHESIS(17, "right_par"),
    CAPITAL_A(18, "A"),
    CAPITAL_B(19, "B"),
    CAPITAL_C(20, "C"),
    CAPITAL_D(21, "D"),
    CAPITAL_E(22, "E"),
    CAPITAL_F(23, "F"),
    CAPITAL_G(24, "G"),
    CAPITAL_H(25, "H"),
    CAPITAL_I(26, "I"),
    CAPITAL_J(27, "J"),
    CAPITAL_K(28, "K"),
    CAPITAL_L(29, "L"),
    CAPITAL_M(30, "M"),
    CAPITAL_N(31, "N"),
    CAPITAL_O(32, "O"),
    CAPITAL_P(33, "P"),
    CAPITAL_Q(34, "Q"),
    CAPITAL_R(35, "R"),
    CAPITAL_S(36, "S"),
    CAPITAL_T(37, "T"),
    CAPITAL_U(38, "U"),
    CAPITAL_V(39, "V"),
    CAPITAL_W(40, "W"),
    CAPITAL_X(41, "X"),
    CAPITAL_Y(42, "Y"),
    CAPITAL_Z(43, "Z"),
    LOWER_A(44, "a"),
    LOWER_C(45, "c"),
    LOWER_E(46, "e"),
    LOWER_I(47, "i"),
    LOWER_L(48, "l"),
    LOWER_N(49, "n"),
    LOWER_O(50, "o"),
    LOWER_P(51, "p"),
    LOWER_S(52, "s"),
    LOWER_T(53, "t"),
    COMMA(54, ",");

    public static int stringValueToIntValue(String stringValue){
      for(Symbol symbol : Symbol.values()){
        if(symbol.stringValue().equals(stringValue)){
          return symbol.intValue();
        }
      }

      return -1;
    }

    public static String stringValueToString(String stringValue){
      for(Symbol symbol : Symbol.values()){
        if(symbol.stringValue().equals(stringValue)){
          return symbol.toString();
        }
      }

      return (new String(""));
    }

    public static String intValueToString(int intValue){
      for(Symbol symbol : Symbol.values()){
        if(symbol.intValue() == intValue){
          return symbol.toString();
        }
      }

      return (new String(""));
    }

    private Symbol(int intValue, String stringValue){
      intValue_ = intValue;
      stringValue_ = stringValue;
    }

    public int intValue(){
      return intValue_;
    }

    public String stringValue(){
      return stringValue_;
    }

    private int intValue_;
    private String stringValue_;
  }
}
