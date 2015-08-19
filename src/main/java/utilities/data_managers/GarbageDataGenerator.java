package main.java.utilities.data_managers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import main.java.utilities.Utilities;
import main.java.utilities.data.DataSample;
import main.java.utilities.data.DataSet;
import main.java.utilities.traces.TraceGroup;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

public class GarbageDataGenerator{
  public static void main(String[] args) throws SQLException, IOException{

    /* ***** My data ***** */
    MySQLDataManager dataManager = new MySQLDataManager(MY_DATABASE, MY_DATABASE_USERNAME, MY_DATABASE_PASSWORD, MY_DATABASE_TABLE, MY_DATABASE_TABLE_COLUMN, null);
    dataManager.loadFromDatabase();

    System.out.println("My data were loaded!");

    ArrayList<TraceGroup> symbols = dataManager.getDatabaseData();

    DataSet dataSet = new DataSet();
    Mat image;
    int sampleCounter = 0;
    while(sampleCounter < MY_NUMBER_OF_SAMPLES){
      int i = (int)(Math.random() * symbols.size());
      int j = (int)(Math.random() * symbols.size());

      image = (new TraceGroup(symbols.get(i))).add(symbols.get(j)).print(MY_IMAGES_SIZE);

      dataSet.add(new DataSample(Utilities.imageToByteArray(image), MY_GARBAGE_LABEL));

      if(MY_SAVE_IMAGES){
        Highgui.imwrite(MY_IMAGES_OUTPUT_PATH + "/image" + i + "-" + j + ".tiff", image);
      }

      sampleCounter++;
    }

    dataSet.saveIDXFormat(MY_DATA_OUTPUT_PATH + "/data", MY_DATA_OUTPUT_PATH + "/labels");
    /* ***** My training data ***** */

    /* ***** Website data ***** */
    dataManager = new MySQLDataManager(WEBSITE_DATABASE, WEBSITE_DATABASE_USERNAME, WEBSITE_DATABASE_PASSWORD, WEBSITE_DATABASE_TABLE, WEBSITE_DATABASE_TABLE_COLUMN, null);
    dataManager.loadFromDatabase();

    symbols = dataManager.getDatabaseData();

    System.out.println("Website data were loaded!");

    dataSet = new DataSet();
    sampleCounter = 0;
    while(sampleCounter < WEBSITE_NUMBER_OF_SAMPLES){
      int i = (int)(Math.random() * symbols.size());
      int j = (int)(Math.random() * symbols.size());

      image = (new TraceGroup(symbols.get(i))).add(symbols.get(j)).print(WEBSITE_IMAGES_SIZE);

      dataSet.add(new DataSample(Utilities.imageToByteArray(image), WEBSITE_GARBAGE_LABEL));

      if(WEBSITE_SAVE_IMAGES){
        Highgui.imwrite(WEBSITE_IMAGES_OUTPUT_PATH + "/image" + i + "-" + j + ".tiff", image);
      }

      sampleCounter++;
    }

    dataSet.saveIDXFormat(WEBSITE_DATA_OUTPUT_PATH + "/data", WEBSITE_DATA_OUTPUT_PATH + "/labels");
    /* ***** Website data ***** */
  }

  /* ***** My training data ***** */
  public static final String MY_DATA_OUTPUT_PATH = "data/datasets/garbage_dataset/my_data/";
  public static final String MY_IMAGES_OUTPUT_PATH = "data/my_images/my_data/";
  public static final String MY_DATABASE = "jdbc:mysql://localhost/sub_module1_db";
  public static final String MY_DATABASE_USERNAME = "sub_module1_user";
  public static final String MY_DATABASE_PASSWORD = "ValmadiaN";
  public static final String MY_DATABASE_TABLE = "inkml_data";
  public static final String MY_DATABASE_TABLE_COLUMN = "trace_groups";
  public static final Size MY_IMAGES_SIZE = new Size(50, 50);
  public static final byte MY_GARBAGE_LABEL = 18;
  public static final boolean MY_SAVE_IMAGES = true;
  public static final int MY_NUMBER_OF_SAMPLES = 50000;
  /* ***** My training data ***** */

  /* ***** Website data ***** */
  public static final String WEBSITE_DATA_OUTPUT_PATH = "data/datasets/garbage_dataset/website_data/";
  public static final String WEBSITE_IMAGES_OUTPUT_PATH = "data/my_images/website/";
  public static final String WEBSITE_DATABASE = "jdbc:mysql://localhost/website_db";
  public static final String WEBSITE_DATABASE_USERNAME = "website_user";
  public static final String WEBSITE_DATABASE_PASSWORD = "ValmadiaN";
  public static final String WEBSITE_DATABASE_TABLE = "xml_data";
  public static final String WEBSITE_DATABASE_TABLE_COLUMN = "traces";
  public static final Size WEBSITE_IMAGES_SIZE = new Size(50, 50);
  public static final byte WEBSITE_GARBAGE_LABEL = 18;
  public static final boolean WEBSITE_SAVE_IMAGES = true;
  public static final int WEBSITE_NUMBER_OF_SAMPLES = 50000;
  /* ***** Website data ***** */

}
