package main.utilities.data;

import main.utilities.grammars.SymbolFactory;

/** @class DataSample
 *
 *  @brief Implements a data sample.
 *
 *  Data samples are used along with a machine learning algorithm to train or evaluate it.
 */
public class DataSample{
  /**
   *  @brief Default constructor.
   */
  public DataSample(){
    label_ = SymbolFactory.UNKNOWN_LABEL;
  }

  /**
   *  @brief Constructor.
   *
   *  @param data Initial value for data.
   */
  public DataSample(byte[] data){
    data_ = data.clone();
    label_ = SymbolFactory.UNKNOWN_LABEL;
  }

  /**
   *  @brief Constructor.
   *
   *  The DataSample takes a clone of the given data. That is, if the given data array changes, the data inside the
   *  DataSample will not change.
   *
   *  @param data Initial value for data.
   *  @param label Initial value for label.
   */
  public DataSample(byte[] data, byte label){
    data_ = data.clone();
    label_ = label;
  }

  /**
   *  @brief Constructor.
   *
   *  @param size The number of bytes of the DataSample data.
   */
  public DataSample(int size){
    data_ = new byte[size];
    label_ = SymbolFactory.UNKNOWN_LABEL;
  }

  /**
   *  @brief Constructor.
   *
   *  This constructor is used to create an identical copy of a DataSample.
   *
   *  @param dataSample The DataSample to be copied.
   */
  public DataSample(DataSample dataSample){
    data_ = dataSample.data_.clone();
    label_ = dataSample.label_;
  }

  public byte[] data_; //!< The data of the DataSample.
  public byte label_; //!< The label of the DataSample.

}
