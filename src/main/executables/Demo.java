package main.executables;

import java.io.IOException;

import main.evaluators.SimpleEvaluator;

public class Demo{

  public static void main(String[] args) throws IOException{
    SimpleEvaluator evaluator = new SimpleEvaluator(args[0], args[1]);

    evaluator.evaluate();

    System.out.println(evaluator.toString());
  }

}
