package tests.utilities;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import main.utilities.DataSet;
import main.utilities.DataSample;

public class DataSetTest{

  // Also tests size method.
  @Test
  public void testDataSet(){
    DataSet dataSet = new DataSet();

    assertEquals(0, dataSet.size(), 0);
  }

  @Test
  public void testAdd(){
    DataSet dataSet = new DataSet();

    byte[] array = new byte[] {0x01, 0x02, 0x03};
    DataSample dataSample = new DataSample(array);

    dataSet.add(dataSample);

    assertEquals(1, dataSet.size(), 0);
  }

  @Test
  public void testGet(){
   DataSet dataSet = new DataSet();

    byte[] array = new byte[] {0x01, 0x02, 0x03};
    DataSample dataSample = new DataSample(array);

    dataSet.add(dataSample);

    for(int i = 0;i < array.length;i++){
      assertEquals(array[i], dataSet.get(0).data_[i], 0);
    }
  }

  @Test
  public void testDataSet2(){
    DataSet dataSet1 = new DataSet();

    byte[] array = new byte[] {0x01, 0x02, 0x03};
    DataSample dataSample = new DataSample(array);

    dataSet1.add(dataSample);

    DataSet dataSet2 = new DataSet(dataSet1);

    // Check that dataSet2 is equal to dataSet1.
    for(int i = 0;i < dataSet1.size();i++){
      for(int j = 0;j < dataSet1.get(i).data_.length;j++){
        assertEquals(dataSet1.get(i).data_[j], dataSet2.get(i).data_[j], 0);
      }
    }

    // Make a change on dataSet2.
    for(int i = 0;i < dataSet2.size();i++){
      for(int j = 0;j < dataSet2.get(i).data_.length;j++){
        dataSet2.get(i).data_[j] |= 0x10;
      }
    }

    // Check that the data of dataSet1 have not been changed,
    // and that they are different from the data of dataSet2.
    for(int i = 0;i < array.length;i++){
      assertEquals(array[i], dataSet1.get(0).data_[i], 0);
      assertEquals(dataSet2.get(0).data_[i], dataSet1.get(0).data_[i] | 0x10, 0);
    }
  }

  @Test
  public void testIDXFormat() throws IOException{
    DataSet dataSet1 = new DataSet();

    byte[] array = new byte[] {0x01, 0x02, 0x03, 0x04};
    int label = 58;
    DataSample dataSample = new DataSample(array, label);

    dataSet1.add(dataSample);

    String dataFile = "data/tests/utilities/DataSet/testMNISTLike_data";
    String labelsFile = "data/tests/utilities/DataSet/testMNISTLike_labels";

    dataSet1.saveIDXFormat(dataFile, labelsFile);

    DataSet dataSet2 = DataSet.loadIDXFormat(dataFile, labelsFile);

    for(int i = 0;i < dataSet2.size();i++){
      for(int j = 0;j < dataSet2.get(i).data_.length;j++){

        assertEquals(dataSet1.get(i).data_[j], dataSet2.get(i).data_[j], 0);
      }

      assertEquals(dataSet1.get(i).label_, dataSet2.get(i).label_, 0);
    }

  }

}
