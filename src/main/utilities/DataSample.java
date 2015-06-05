package main.utilities;

import main.utilities.Utilities;

public class DataSample{
  public DataSample(){
    label_ = Utilities.UNKNOWN_LABEL;
  }

  public DataSample(byte[] data){
    data_ = data.clone();
    label_ = Utilities.UNKNOWN_LABEL;
  }

  public DataSample(byte[] data, byte label){
    data_ = data.clone();
    label_ = label;
  }

  public DataSample(int size){
    data_ = new byte[size];
    label_ = Utilities.UNKNOWN_LABEL;
  }

  public DataSample(DataSample dataSample){
    data_ = dataSample.data_.clone();
    label_ = dataSample.label_;
  }

  public byte[] data_;
  public byte label_; // <! The labels should comply with the main.utilities.Utilities
                     //    indexing.

}
