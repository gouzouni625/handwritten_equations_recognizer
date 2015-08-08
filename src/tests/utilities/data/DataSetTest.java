package tests.utilities.data;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import main.utilities.data.DataSample;
import main.utilities.data.DataSet;
import main.utilities.symbols.SymbolFactory;

/** @class DataSetTest
 *
 *  @brief Class that contains tests for DataSet class.
 */
public class DataSetTest{
  /**
   *  @brief Tests the constructors, size, get and add methods of DataSet class.
   */
  @Test
  public void testDataSet(){
    // Create a data set and assert that it is initialized correctly.
    DataSet dataSet1 = new DataSet();
    assertEquals(0, dataSet1.size(), 0);

    // Add two data samples and assert that they are added correctly.
    DataSample dataSample1 = new DataSample();
    DataSample dataSample2 = new DataSample();
    dataSet1.add(dataSample1).add(dataSample2);
    assertEquals(2, dataSet1.size(), 0);

    // Create a copy of the data set and assert that they are identical.
    DataSet dataSet2 = new DataSet(dataSet1);
    assertEquals(2, dataSet2.size(), 0);

    // Assert that get method returns the actual data sample that exists inside the data set.
    DataSample dataSample = dataSet2.get(0);
    dataSample.label_ = 0x01;
    assertEquals(0x01, dataSet2.get(0).label_, 0);

    // Assert that the original data set didn't change when the copy changed.
    assertEquals(SymbolFactory.UNKNOWN_LABEL, dataSet1.get(0).label_, 0);
  }

  /**
   *  @brief Tests saveIDXFormat and loadIDXFormat methods of DataSet class.
   *
   *  @throws IOException When saveIDXFormat or loadIDXFormat throws an exception.
   */
  @Test
  public void testIDXFormat() throws IOException{
    DataSet dataSet1 = new DataSet();

    byte[] array = new byte[] {0x01, 0x02, 0x03, 0x04};
    byte label = 58;
    DataSample dataSample = new DataSample(array, label);

    dataSet1.add(dataSample);

    String dataFile = "data/tests/utilities/data/DataSet/testIDXFormat_data";
    String labelsFile = "data/tests/utilities/data/DataSet/testIDXFormat_labels";

    dataSet1.saveIDXFormat(dataFile, labelsFile);

    DataSet dataSet2 = DataSet.loadIDXFormat(dataFile, labelsFile);

    for(int i = 0;i < dataSet2.size();i++){
      for(int j = 0;j < dataSet2.get(i).data_.length;j++){
        assertEquals(dataSet1.get(i).data_[j], dataSet2.get(i).data_[j], 0);
      }

      assertEquals(dataSet1.get(i).label_, dataSet2.get(i).label_, 0);
    }
  }

  /**
   *  @brief Tests shuffle method of DataSet class.
   */
  @Test
  public void testShuffle(){
    DataSet dataSet = new DataSet();

    int numberOfSamples = 10;
    DataSample[] dataSamples = new DataSample[10];
    for(int i = 0;i < numberOfSamples;i++){
      dataSamples[i] = new DataSample();

      dataSet.add(dataSamples[i]);
    }

    dataSet.shuffle();

    int numberOfUnChanged = 0;
    for(int i = 0;i < numberOfSamples;i++){
      if(dataSet.get(i) == dataSamples[i]){
        numberOfUnChanged++;
      }
    }

    assertNotEquals(numberOfSamples, numberOfUnChanged);
  }

  /**
   *  @brief Tests static add method of DataSet class.
   */
  @Test
  public void testStaticAdd(){
    DataSet dataSet1 = new DataSet();
    DataSample dataSample11 = new DataSample(1);
    DataSample dataSample12 = new DataSample(2);
    DataSample dataSample13 = new DataSample(3);
    dataSet1.add(dataSample11).add(dataSample12).add(dataSample13);

    DataSet dataSet2 = new DataSet();
    DataSample dataSample21 = new DataSample(4);
    DataSample dataSample22 = new DataSample(5);
    DataSample dataSample23 = new DataSample(6);
    dataSet2.add(dataSample21).add(dataSample22).add(dataSample23);

    DataSet dataSet3 = DataSet.add(dataSet1, dataSet2);

    assertEquals(dataSet1.size() + dataSet2.size(), dataSet3.size(), 0);

    assertNotEquals(dataSample11, dataSet3.get(0));
    assertNotEquals(dataSample12, dataSet3.get(1));
    assertNotEquals(dataSample13, dataSet3.get(2));
    assertNotEquals(dataSample21, dataSet3.get(3));
    assertNotEquals(dataSample22, dataSet3.get(4));
    assertNotEquals(dataSample23, dataSet3.get(5));

    assertEquals(dataSample11.data_.length, dataSet3.get(0).data_.length, 0);
    assertEquals(dataSample12.data_.length, dataSet3.get(1).data_.length, 0);
    assertEquals(dataSample13.data_.length, dataSet3.get(2).data_.length, 0);
    assertEquals(dataSample21.data_.length, dataSet3.get(3).data_.length, 0);
    assertEquals(dataSample22.data_.length, dataSet3.get(4).data_.length, 0);
    assertEquals(dataSample23.data_.length, dataSet3.get(5).data_.length, 0);
  }

  /**
   *  @brief Tests subDataSet method of DataSet class.
   */
  @Test
  public void testSubDataSet(){
    DataSet dataSet1 = new DataSet();

    int numberOfSamples = 10;
    DataSample[] dataSamples = new DataSample[numberOfSamples];
    for(int i = 0;i < numberOfSamples;i++){
      dataSamples[i] = new DataSample(i);

      dataSet1.add(dataSamples[i]);
    }

    DataSet dataSet2 = dataSet1.subDataSet(2, 5);

    assertNotEquals(dataSamples[2], dataSet2.get(0));
    assertNotEquals(dataSamples[3], dataSet2.get(1));
    assertNotEquals(dataSamples[4], dataSet2.get(2));

    assertEquals(dataSamples[2].data_.length, dataSet2.get(0).data_.length, 0);
    assertEquals(dataSamples[3].data_.length, dataSet2.get(1).data_.length, 0);
    assertEquals(dataSamples[4].data_.length, dataSet2.get(2).data_.length, 0);
  }

}
