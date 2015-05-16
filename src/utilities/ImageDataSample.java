package utilities;

public class ImageDataSample extends DataSample{
  public ImageDataSample(){
    super();
  }
  
  public ImageDataSample(double[] data){
    super(data);
  }
  
  public ImageDataSample(double[] data, int sampleRows, int sampleColumns){
    super(data);
    
    sampleRows_ = sampleRows;
    sampleColumns_ = sampleColumns;
  }

  public int sampleRows_;
  public int sampleColumns_;
}
