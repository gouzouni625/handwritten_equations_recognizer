package org.hwer.engine.utilities.image_processing.image;

public class Image{
  public Image(int width, int height){
    width_ = width;
    height_ = height;

    pixels_ = new byte[width_][];
    for(int i = 0;i < width_;i++){
      pixels_[i] = new byte[height_];

      for(int j = 0;j < height_;j++){
        pixels_[i][j] = 0;
      }
    }
  }

  public Image(Image image){
    width_ = image.getWidth();
    height_ = image.getHeight();

    pixels_ = new byte[width_][height_];
    for(int i = 0;i < width_;i++){
      for(int j = 0;j < height_;j++){
        pixels_[i][j] = image.getPixel(i, j);
      }
    }
  }

  public void setPixel(int x, int y, byte value){
    if (x >= 0 && x < width_ && y >= 0 && y < height_) {
      pixels_[x][y] = value;
    }
  }

  @Override
  public String toString(){
    StringBuilder stringBuilder = new StringBuilder();

    for(int j = height_ - 1;j >= 0;j--){
      for(int i = 0;i < width_;i++){
        if((pixels_[i][j] & 0xFF) > 0) {
          stringBuilder.append("1");
        }
        else{
          stringBuilder.append("0");
        }
      }
      stringBuilder.append("\n");
    }

    return stringBuilder.toString();
  }

  public byte getPixel(int x, int y){
    return pixels_[x][y];
  }

  public int getWidth(){
    return width_;
  }

  public int getHeight(){
    return height_;
  }

  private int width_;
  private int height_;

  // Pixels begin counting from the bottom left with the first dimensional being the horizontal.
  private byte[][] pixels_;

}
