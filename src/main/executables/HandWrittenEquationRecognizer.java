package main.executables;

import java.io.IOException;

import main.distorters.ImageDistorter;
import main.evaluators.SimpleEvaluator;
import main.partitioners.NNMSTPartitioner;

public class HandWrittenEquationRecognizer{

  public static void main(String[] args) throws IOException{
    ImageDistorter imageDistorter = new ImageDistorter();
    imageDistorter.setSampleRows(50);
    imageDistorter.setSampleColumns(50);

    int[] sizesOfLayers = new int[] {2500, 100, 100, 18};

    SimpleEvaluator evaluator = new SimpleEvaluator(args[0], args[1], sizesOfLayers, imageDistorter);

    evaluator.evaluate();

    System.out.println(evaluator.toString());
  }

}
