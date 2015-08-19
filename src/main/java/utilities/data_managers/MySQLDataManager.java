package main.java.utilities.data_managers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import main.java.utilities.Utilities;
import main.java.utilities.data.DataSample;
import main.java.utilities.data.DataSet;
import main.java.utilities.inkml.InkMLParser;
import main.java.utilities.traces.TraceGroup;

/** @class MySQLDataManager
 *
 *  @brief Connects to a MySQL database, retrieves data saved in inkML format, creates images of any size and saves
 *         the data to IDX formatted files. To connect to MySQL, jdbc is used. For more information, please visit
 *         http://www.oracle.com/technetwork/java/javase/jdbc/index.html
 */
public class MySQLDataManager{
  /**
   *  @brief Default Constructor.
   */
  public MySQLDataManager(){
  }

  /**
   *  @brief Constructor.
   *
   *  @param database The database to connect to.
   *  @param databaseUsername The user name to be used for the database.
   *  @param databasePassword The password to be used for the database.
   *  @param databaseTable The table of the database to read.
   *  @param databaseTableDataColumn The column of the table to read the data.
   *  @param databaseTableLabelsColumn The column of the table to read the labels of the data.
   */
  public MySQLDataManager(String database, String databaseUsername, String databasePassword, String databaseTable,
                          String databaseTableDataColumn, String databaseTableLabelsColumn){
    database_ = database;
    databaseUsername_ = databaseUsername;
    databasePassword_ = databasePassword;
    databaseTable_ = databaseTable;
    databaseTableDataColumn_ = databaseTableDataColumn;
    databaseTableLabelsColumn_ = databaseTableLabelsColumn;
  }

  /**
   *  @brief Loads the data from the database.
   *
   *  @param database The database to connect to.
   *  @param databaseUsername The user name to be used for the database.
   *  @param databasePassword The password to be used for the database.
   *  @param databaseTable The table of the database to read.
   *  @param databaseTableDataColumn The column of the table to read the data.
   *  @param databaseTableLabelsColumn The column of the table to read the labels of the data.
   *
   *  @throws SQLException Thrown from jdbc.
   *  @throws UnsupportedEncodingException Thrown from jdbc.
   */
  public void loadFromDatabase(String database, String databaseUsername, String databasePassword, String databaseTable,
                               String databaseTableDataColumn, String databaseTableLabelsColumn)
                              throws SQLException, UnsupportedEncodingException{
    database_ = database;
    databaseUsername_ = databaseUsername;
    databasePassword_ = databasePassword;
    databaseTable_ = databaseTable;
    databaseTableDataColumn_ = databaseTableDataColumn;
    databaseTableLabelsColumn_ = databaseTableLabelsColumn;

    loadFromDatabase();
  }

  /**
   *  @brief Loads the data from the database.
   *
   *  @throws SQLException Thrown from jdbc.
   *  @throws UnsupportedEncodingException Thrown from jdbc.
   */
  public void loadFromDatabase() throws SQLException, UnsupportedEncodingException{
    databaseData_ = new ArrayList<TraceGroup>();

    // Connect to the database and get the data. ==============================
    Connection connection = (Connection) DriverManager.getConnection(database_, databaseUsername_, databasePassword_);
    Statement statement = (Statement) connection.createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT " + databaseTableDataColumn_ + " FROM " + databaseTable_);

    // Process the data. ======================================================
    InkMLParser inkMLParser = new InkMLParser();
    while(resultSet.next()){
      inkMLParser.setXMLData(resultSet.getString(1));

      databaseData_.add(inkMLParser.traceGroup_);
    }

    // Retrieve labels.
    if(databaseTableLabelsColumn_ != null){
      labels_ = new ArrayList<Byte>();

      resultSet = statement.executeQuery("SELECT " + databaseTableLabelsColumn_ + " FROM " + databaseTable_);

      while(resultSet.next()){
        labels_.add(Byte.parseByte(resultSet.getString(1)));
      }
    }

    connection.close();
  }

  /**
   *  @brief Saves a set of data to the database.
   *
   *  @param data The array of the data to save on the database.
   *
   *  @throws SQLException Thrown from jdbc.
   */
  public void saveToDatabase(String[] data) throws SQLException{
    Connection connection = (Connection) DriverManager.getConnection(database_, databaseUsername_, databasePassword_);
    Statement statement = (Statement) connection.createStatement();

    String values = new String("");
    for(String value : data){
      values += value + ", ";
    }
    // Remove last comma from values.
    values = values.substring(0, values.length() - 2);

    String query = "INSERT INTO " + databaseTable_ + " VALUES (" + values + ")";
    System.out.println(query);
    statement.executeUpdate(query);

    connection.close();
  }

  /**
   *  @brief Saves the data retrieved from the database to IDX format.
   *
   *  @param imageSize The size of the images to transform the data to.
   *  @param dataFile The file to save the data.
   *  @param labelsFile The file to save the labels.
   *  @param saveImages Flag to decide whether to save .tiff images of the data.
   *  @param imagesPath The file to save .tiff images of the data.
   *
   *  @throws IOException If there is problem when writing to the file system.
   */
  public void saveToIDX(Size imageSize, String dataFile, String labelsFile, boolean saveImages, String imagesPath)
                       throws IOException{
    byte[] labels = new byte[labels_.size()];
    for(int i = 0;i < labels_.size();i++){
      labels[i] = labels_.get(i);
    }

    this.saveToIDX(imageSize, dataFile, labels, labelsFile, saveImages, imagesPath);
  }

  /**
   *  @brief Saves the data retrieved from the database to IDX format.
   *
   *  @param imageSize The size of the images to transform the data to.
   *  @param dataFile The file to save the data.
   *  @param labels The labels of the data.
   *  @param labelsFile The file to save the labels.
   *  @param saveImages Flag to decide whether to save .tiff images of the data.
   *  @param imagesPath The file to save .tiff images of the data.
   *
   *  @throws IOException If there is problem when writing to the file system.
   */
  public void saveToIDX(Size imageSize, String dataFile, byte[] labels, String labelsFile, boolean saveImages,
                        String imagesPath) throws IOException{
    Mat[] images = new Mat[databaseData_.size()];
    DataSet dataSet = new DataSet();

    for(int i = 0;i < databaseData_.size();i++){
      images[i] = databaseData_.get(i).print(imageSize);

      dataSet.add(new DataSample(Utilities.imageToByteArray(images[i]), labels[i]));

      if(saveImages){
        Highgui.imwrite(imagesPath + "/image" + i + ".tiff", images[i]);
      }
    }

    dataSet.saveIDXFormat(dataFile, labelsFile);
  }

  /**
   *  @brief Getter method for the database data.
   *
   *  @return Returns the data retrieved from the database.
   */
  public ArrayList<TraceGroup> getDatabaseData(){
    return databaseData_;
  }

  /**
   *  @brief Getter method for the database labels.
   *
   *  @return Returns the labels retrieved from the database.
   */
  public ArrayList<Byte> getLabels(){
    return labels_;
  }

  private String database_; //!< The database that this MySQLDataManager will connect to.
  private String databaseUsername_; //!< The user name that this MySQLDataManager will use.
  private String databasePassword_; //!< The password that this MySQLDataManager will use.
  private String databaseTable_; //!< The table that this MySQLDataManager will read from.
  private String databaseTableDataColumn_; //!< The column that this MySQLDataManager will read the data from.
  private String databaseTableLabelsColumn_; //!< The column that this MySQLDataManager will read the labels from.

  private ArrayList<TraceGroup> databaseData_; //!< The data retrieved from the database.
  private ArrayList<Byte> labels_; //!< The labels retrieved by the database.

}
