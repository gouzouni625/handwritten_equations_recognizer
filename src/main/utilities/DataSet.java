package main.utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.nio.ByteBuffer;

import main.utilities.DataSample;

public class DataSet{
  public DataSet(){
    samples_ = new ArrayList<DataSample>();
  }

  public DataSet(DataSet dataSet){
    samples_ = new ArrayList<DataSample>();

    for(int i = 0;i < dataSet.size();i++){
      this.add(dataSet.get(i));
    }
  }


  public void add(DataSample dataSample){
    samples_.add(new DataSample(dataSample));
  }

  public DataSample get(int index){
    return samples_.get(index);
  }

  public int size(){
    return samples_.size();
  }


  public static DataSet loadIDXFormat(String dataFile, String labelsFile) throws IOException{
    DataSet dataSet = new DataSet();

    // Load the data. =========================================================
    FileInputStream fileInputStream = new FileInputStream(dataFile);

    // Skip the magic number at the beginning of the file.
    fileInputStream.skip(4);

    // Read the number of items in the file.
    byte[] buffer = new byte[4];
    fileInputStream.read(buffer);
    ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);

    int numberOfItems = byteBuffer.getInt();

    // Read the rows and the columns of each image.
    fileInputStream.read(buffer);
    byteBuffer = ByteBuffer.wrap(buffer);

    int numberOfRows = byteBuffer.getInt();

    fileInputStream.read(buffer);
    byteBuffer = ByteBuffer.wrap(buffer);

    int numberOfColumns = byteBuffer.getInt();

    // Read the data.
    buffer = new byte[numberOfRows * numberOfColumns];
    for(int item = 0;item < numberOfItems;item++){
      fileInputStream.read(buffer);

      dataSet.add(new DataSample(buffer));
    }

    // Close the input stream.
    fileInputStream.close();

    // Load the labels. =======================================================
    fileInputStream = new FileInputStream(labelsFile);

    // Skip the magic number at the beginning of the file.
    fileInputStream.skip(4);

    // Read the numberOfItems in the file.
    buffer = new byte[4];
    fileInputStream.read(buffer);
    byteBuffer = ByteBuffer.wrap(buffer);

    numberOfItems = byteBuffer.getInt();

    // Read labels.
    buffer = new byte[1];
    for(int i = 0;i < numberOfItems;i++){
      fileInputStream.read(buffer);

      // Labels must always be positive numbers so convert the byte that is read
      // to unsigned byte.
      dataSet.get(i).label_ = buffer[0];
    }

    // Close the input stream.
    fileInputStream.close();

    return dataSet;
  }

  public void saveIDXFormat(String dataFile, String labelsFile) throws IOException{

    // Save the data. =========================================================
    FileOutputStream fileOutputStream = new FileOutputStream(dataFile);

    int magicNumber = Utilities.DATA_MAGIC_NUMBER;

    // Save the magic number.
    fileOutputStream.write(ByteBuffer.allocate(4).putInt(magicNumber).array());

    // Save the number of items.
    int numberOfItems = samples_.size();
    fileOutputStream.write(ByteBuffer.allocate(4).putInt(numberOfItems).array());

    // Save the number of rows. The samples are considered to have the same
    // number of rows and columns. Moreover, all the samples are considered to be
    // of equal length with the first.
    int sampleLength = samples_.get(0).data_.length;
    int numberOfRows = (int)(Math.sqrt(sampleLength));
    int numberOfColumns = numberOfRows;

    fileOutputStream.write(ByteBuffer.allocate(4).putInt(numberOfRows).array());
    fileOutputStream.write(ByteBuffer.allocate(4).putInt(numberOfColumns).array());

    // Save the actual data.
    for(int i = 0;i < numberOfItems;i++){
      fileOutputStream.write(samples_.get(i).data_);
    }

    // Close the output stream.
    fileOutputStream.close();

    // Save the labels. =======================================================
    fileOutputStream = new FileOutputStream(labelsFile);

    magicNumber = Utilities.LABELS_MAGIC_NUMBER;

    // Save the magic number.
    fileOutputStream.write(ByteBuffer.allocate(4).putInt(magicNumber).array());

    // Save the number of items.
    fileOutputStream.write(ByteBuffer.allocate(4).putInt(numberOfItems).array());

    // Save the labels.
    for(int i = 0;i < numberOfItems;i++){
      fileOutputStream.write(samples_.get(i).label_);
    }

    // Close the output stream.
    fileOutputStream.close();
  }


  private ArrayList<DataSample> samples_;

}
