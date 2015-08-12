package tests.utilities.data;

import static org.junit.Assert.*;

import org.junit.Test;

import main.utilities.data.DataSample;
import main.utilities.symbols.SymbolFactory;

/** @class DataSampleTest
 *
 *  @brief Class that contains tests for main.utilities.data.DataSample class.
 */
public class DataSampleTest{
  /**
   *  @brief Tests the constructors of main.utilities.data.DataSample class.
   */
  @Test
  public void testDataSample(){
    /* Test default constructor. */
    DataSample dataSample = new DataSample();

    assertEquals(SymbolFactory.UNKNOWN_LABEL, dataSample.label_, 0);

    /* Test second constructor. */
    byte[] array = new byte[] {0x01, 0x02, 0x03};

    dataSample = new DataSample(array);

    for(int i = 0;i < array.length;i++){
      assertEquals(array[i], dataSample.data_[i], 0);
    }
    assertEquals(SymbolFactory.UNKNOWN_LABEL, dataSample.label_, 0);

    // Make sure the given array is cloned.
    array[0] = 0x04;

    assertEquals(0x01, dataSample.data_[0], 0);

    /* Test third constructor. */
    array = new byte[] {0x01, 0x02, 0x03};
    byte label = 32;

    dataSample = new DataSample(array, label);

    for(int i = 0;i < array.length;i++){
      assertEquals(array[i], dataSample.data_[i], 0);
    }
    assertEquals(label, dataSample.label_, 0);

    // Make sure the given array is cloned.
    array[0] = 0x04;

    assertEquals(0x01, dataSample.data_[0], 0);

    /* Test fourth constructor. */
    int size = 103;

    dataSample = new DataSample(size);

    assertEquals(size, dataSample.data_.length, 0);
    assertEquals(SymbolFactory.UNKNOWN_LABEL, dataSample.label_, 0);

    /* Test fifth constructor. */
    array = new byte[] {0x01, 0x02, 0x03};
    label = 32;

    DataSample dataSample1 = new DataSample(array, label);
    DataSample dataSample2 = new DataSample(dataSample1);

    // Check that dataSample2 has the same data with dataSample1.
    for(int i = 0;i < dataSample1.data_.length;i++){
      assertEquals(dataSample1.data_[i], dataSample2.data_[i], 0);
    }

    // Change the data of dataSample2.
    for(int i = 0;i < dataSample2.data_.length;i++){
      dataSample2.data_[i] |= 0x10;
    }

    // Check that the data of dataSample1 have not been changed
    // and that they are different from the data of dataSample2.
    for(int i = 0;i < array.length;i++){
      assertEquals(array[i], dataSample1.data_[i], 0);
      assertEquals(dataSample2.data_[i], dataSample1.data_[i] | 0x10, 0);
    }
  }

}
