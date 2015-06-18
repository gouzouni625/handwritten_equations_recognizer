package tests.utilities.data;

import static org.junit.Assert.*;

import org.junit.Test;

import main.utilities.Utilities;
import main.utilities.data.DataSample;

public class DataSampleTest{

  @Test
  public void testDataSample(){
    DataSample dataSample = new DataSample();

    assertEquals(Utilities.UNKNOWN_LABEL, dataSample.label_, 0);
  }

  @Test
  public void testDataSample2(){
    byte[] array = new byte[] {0x01, 0x02, 0x03};

    DataSample dataSample = new DataSample(array);

    for(int i = 0;i < array.length;i++){
      assertEquals(array[i], dataSample.data_[i], 0);
    }
    assertEquals(Utilities.UNKNOWN_LABEL, dataSample.label_, 0);
  }

  @Test
  public void testDataSample3(){
    byte[] array = new byte[] {0x01, 0x02, 0x03};
    byte label = 32;

    DataSample dataSample = new DataSample(array, label);

    for(int i = 0;i < array.length;i++){
      assertEquals(array[i], dataSample.data_[i], 0);
    }
    assertEquals(label, dataSample.label_, 0);
  }

  @Test
  public void testDataSample4(){
    int size = 103;

    DataSample dataSample = new DataSample(size);

    assertEquals(size, dataSample.data_.length, 0);
    assertEquals(Utilities.UNKNOWN_LABEL, dataSample.label_, 0);
  }

  @Test
  public void testDataSample5(){
    byte[] array = new byte[] {0x01, 0x02, 0x03};
    byte label = 32;

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
