package org.hwer.engine.utilities.image_processing.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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

  public void setPixel(int x, int y, byte value){
    if (x >= 0 && x < width_ && y >= 0 && y < height_) {
      pixels_[x][y] = value;
    }
  }

  public byte getPixel(int x, int y){
    return pixels_[x][y];
  }

  public void save(String outputDirectory, String outputFileName, String outputExtension)
      throws IOException {
    BufferedImage bufferedImage = new BufferedImage(width_, height_, BufferedImage.TYPE_BYTE_GRAY);
    for(int x = 0;x < width_;x++){
      for(int y = 0;y < height_;y++){
        bufferedImage.setRGB(x, height_ - y - 1, pixels_[x][y]);
      }
    }

    File outputFile = new File(outputDirectory + outputFileName + "." + outputExtension);
    ImageIO.write(bufferedImage, outputExtension, outputFile);
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
