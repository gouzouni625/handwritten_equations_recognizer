package main.utilities.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.nio.ByteBuffer;

/** @class DataSet
 *
 *  @brief Implements a data set.
 *
 *  A data set is an array of DataSample objects with some helpful methods included.
 */
public class DataSet{
  /**
   *  @brief Default constructor.
   */
  public DataSet(){
    samples_ = new ArrayList<DataSample>();
  }

  /**
   *  @brief This constructor is used to create and identical copy of a DataSet.
   *
   *  @param dataSet The DataSet to be copied.
   */
  public DataSet(DataSet dataSet){
    samples_ = new ArrayList<DataSample>();

    for(int i = 0;i < dataSet.size();i++){
      this.add(dataSet.get(i));
    }
  }

  /**
   *  @brief Adds a DataSample to this DataSet.
   *
   *  It is not the actual DataSample that is added but a copy of it. That is, if the given DataSample changes, the
   *  DataSample inside the DataSet will not change.
   *
   *  @param dataSample The DataSample to be added to this DataSet.
   *
   *  @return Returns this DataSet in order for chain commands to be possible(e.g.
   *  @code
   *  DataSet dataSet = new DataSet();
   *
   *  DataSample dataSample1 = new DataSample();
   *  DataSample dataSample2 = new DataSample();
   *
   *  dataSet.add(dataSample1).add(dataSample2);
   *  @endcode
   *  ).
   */
  public DataSet add(DataSample dataSample){
    samples_.add(new DataSample(dataSample));

    return this;
  }

  /**
   *  @brief Returns the DataSample at a specific position in this DataSet.
   *
   *  The DataSample returned is the actual DataSample that exists inside this DataSet. That is, if the returned
   *  DataSample changes, then, the DataSample inside this DataSet will also change.
   *
   *  @param index The position of the DataSample to be returned.
   *
   *  @return Returns the DataSample at the specified position in this DataSet.
   */
  public DataSample get(int index){
    return samples_.get(index);
  }

  /**
   *  @brief Returns the number of DataSample objects in this DataSet.
   *
   *  @return Returns the number of DataSample objects in this DataSet.
   */
  public int size(){
    return samples_.size();
  }

  /**
   *  @brief Loads a DataSet saved on the file system with IDX format.
   *
   *  Copying from http://yann.lecun.com/exdb/mnist/ : \n\n
   *  The IDX file format is a simple format for vectors and multidimensional matrices of various numerical types. \n
   *  \n
   *  The basic format is: \n
   *  magic number \n
   *  size in dimension 0 \n
   *  size in dimension 1 \n
   *  size in dimension 2 \n
   *  ... \n
   *  size in dimension N \n
   *  data \n\n
   *
   *  The magic number is an integer of 4 bytes. The first 2 bytes are always 0. \n\n
   *  The third byte, codes the type of the data: \n
   *  0x08: unsigned byte \n
   *  0x09: signed byte \n
   *  0x0B: short(2 bytes) \n
   *  0x0C: int(4 bytes) \n
   *  0x0D: float(4 bytes) \n
   *  0x0E: double(8 bytes) \n
   *  \n
   *  The fourth byte codes the number of dimensions of the vector/matrix: 1 for vectors, 2 for matrices, ... \n\n
   *  The sizes in each dimension are 4-byte integers. \n\n
   *  The data is stored like in a C array, i.e. the index in the last dimension changes the fastest. \n
   *
   *  @param dataFile The full path of the data file.
   *  @param labelsFile The full path of the label file.
   *
   *  @return Returns the loaded DataSet.
   *
   *  @throws IOException In case Java FileInputStream throws an exception.
   *
   *  @sa saveIDXFormat
   */
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

  /**
   *  @brief Saves this DataSet on the file system with IDX format.
   *
   *  Copying from http://yann.lecun.com/exdb/mnist/ : \n\n
   *  The IDX file format is a simple format for vectors and multidimensional matrices of various numerical types. \n
   *  \n
   *  The basic format is: \n
   *  magic number \n
   *  size in dimension 0 \n
   *  size in dimension 1 \n
   *  size in dimension 2 \n
   *  ... \n
   *  size in dimension N \n
   *  data \n\n
   *
   *  The magic number is an integer of 4 bytes. The first 2 bytes are always 0. \n\n
   *  The third byte, codes the type of the data: \n
   *  0x08: unsigned byte \n
   *  0x09: signed byte \n
   *  0x0B: short(2 bytes) \n
   *  0x0C: int(4 bytes) \n
   *  0x0D: float(4 bytes) \n
   *  0x0E: double(8 bytes) \n
   *  \n
   *  The fourth byte codes the number of dimensions of the vector/matrix: 1 for vectors, 2 for matrices, ... \n\n
   *  The sizes in each dimension are 4-byte integers. \n\n
   *  The data is stored like in a C array, i.e. the index in the last dimension changes the fastest. \n
   *
   *  @param dataFile The full path of the file to save the data.
   *  @param labelsFile The full path of the file to save the labels.
   *
   *  @throws IOException In case Java FileOutputStream throws an exception.
   *
   *  @sa loadIDXFormat
   */
  public void saveIDXFormat(String dataFile, String labelsFile) throws IOException{

    // Save the data. =========================================================
    FileOutputStream fileOutputStream = new FileOutputStream(dataFile);

    int magicNumber = DataSet.DATA_MAGIC_NUMBER;

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

    magicNumber = DataSet.LABELS_MAGIC_NUMBER;

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

  /**
   *  @brief Shuffles this dataSet.
   */
  public void shuffle(){
    Collections.shuffle(samples_);
  }

  /**
   *  @brief Adds two DataSet objects.
   *
   *  The second DataSet is appended at the end of the first. The returned DataSet is a copy of the given ones. That is,
   *  if the given DataSet objects change, the returned DataSet will not change.
   *
   *  @param dataSet1 The first DataSet.
   *  @param dataSet2 The second DataSet.
   *
   *  @return Returns the new DataSet.
   */
  public static DataSet add(DataSet dataSet1, DataSet dataSet2){
    DataSet result = new DataSet();

    for(int i = 0;i < dataSet1.size();i++){
      result.add(dataSet1.get(i));
    }

    for(int i = 0;i < dataSet2.size();i++){
      result.add(dataSet2.get(i));
    }

    return result;
  }

  /**
   *  @brief Returns a sub-group of DataSample objects from this DataSet.
   *
   *  The DataSample objects returned are not the actual DataSample objects but copies of them. That is, if the returned
   *  DataSample objects change, the ones inside the DataSet will not change.
   *
   *  @param start The beginning index, inclusive.
   *  @param end The ending index, exclusive.
   *
   *  @return Returns the specified sub DataSet.
   */
  public DataSet subDataSet(int start, int end){
    List<DataSample> samples = samples_.subList(start, end);

    DataSet dataSet = new DataSet();
    for(DataSample sample : samples){
      dataSet.add(sample);
    }

    return dataSet;
  }

  private ArrayList<DataSample> samples_; //!< The DataSample objects of this DataSet.

  public static final int DATA_MAGIC_NUMBER = 0x00000803; //!< The magic number to be used for data.
  public static final int LABELS_MAGIC_NUMBER = 0x00000801; //!< The magic number to be used for labels.

}
