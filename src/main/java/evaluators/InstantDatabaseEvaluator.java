package main.java.evaluators;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;

import main.java.base.NeuralNetwork;
import main.java.distorters.ImageDistorter;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class InstantDatabaseEvaluator{

  public static void main(String[] args) throws IOException{

    String xmlData = new String();
    try{
      Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/instant_equations_db", "instant_eq_user", "ValmadiaN");
      Statement statement = (Statement) connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT trace_groups FROM inkml_data WHERE id = (SELECT MAX(id) FROM inkml_data)");

      if(resultSet.next()){
        xmlData = new String(resultSet.getString(1));
      }
      else{
        throw (new IOException());
      }

      connection.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }

    NeuralNetwork neuralNetwork = NeuralNetwork.createFromBinary("/home/george/Desktop/workspace/resources/data/github/repositories/neural_network/trained_networks/neural_network.binary");

    ImageDistorter imageDistorter = new ImageDistorter();
    int sampleDimentionSize = (int)Math.sqrt(neuralNetwork.getSizesOfLayers()[0]);
    imageDistorter.setSampleRows(sampleDimentionSize);
    imageDistorter.setSampleColumns(sampleDimentionSize);

    SimpleEvaluator evaluator = new SimpleEvaluator(neuralNetwork, imageDistorter);

    evaluator.setPartitionerSilent(false);
    evaluator.setGrammarSilent(false);
    evaluator.setParserSilent(false);

    evaluator.evaluate(xmlData);

    System.out.println(evaluator.toString());
  }

}
