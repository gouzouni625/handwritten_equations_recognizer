package org.hwer.engine.utilities.image_processing.image;


/**
 * @class Image
 * @brief Implements a two-dimensional Image
 *        This Image is created for internal use by the engine and is not meant to be used as part
 *        of the implemented classifier. Pixels begin counting from the bottom left with the first
 *        dimensional being the horizontal.
 */
public class Image {
    /**
     * @brief Constructor
     *
     * @param width
     *     The width of the image
     * @param height
     *     The height of the image
     */
    public Image (int width, int height) {
        width_ = width;
        height_ = height;

        pixels_ = new byte[width_][];
        for (int i = 0; i < width_; i++) {
            pixels_[i] = new byte[height_];

            for (int j = 0; j < height_; j++) {
                pixels_[i][j] = 0;
            }
        }
    }

    /**
     * @brief Constructor
     *        This constructor can be used to copy an Image. The new Image will have the same pixel
     *        values with the given one but will be a different object.
     *
     * @param image
     *     The Image to be copied
     */
    public Image (Image image) {
        width_ = image.getWidth();
        height_ = image.getHeight();

        pixels_ = new byte[width_][height_];
        for (int i = 0; i < width_; i++) {
            for (int j = 0; j < height_; j++) {
                pixels_[i][j] = image.getPixel(i, j);
            }
        }
    }

    /**
     * @brief Sets a specified pixel to a specified value
     *
     * @param x
     *     The abscissa of the pixel
     * @param y
     *     The ordinate of the pixel
     * @param value
     *     The value to be assigned to the pixel
     */
    public void setPixel (int x, int y, byte value) {
        if (x >= 0 && x < width_ && y >= 0 && y < height_) {
            pixels_[x][y] = value;
        }
    }

    /**
     * @brief Returns a string representation of this Image
     *
     * @return A string representation of this Image
     */
    @Override
    public String toString () {
        StringBuilder stringBuilder = new StringBuilder();

        for (int j = height_ - 1; j >= 0; j--) {
            for (int i = 0; i < width_; i++) {
                if ((pixels_[i][j] & 0xFF) > 0) {
                    stringBuilder.append("1");
                }
                else {
                    stringBuilder.append("0");
                }
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    /**
     * @brief Returns the value of a specified pixel
     *
     * @param x
     *     The abscissa of the pixel
     * @param y
     *     The ordinate of the pixel
     *
     * @return The value of the specified pixel
     */
    public byte getPixel (int x, int y) {
        return pixels_[x][y];
    }

    /**
     * @brief Returns the width of this Image
     *
     * @return The width of this Image
     */
    public int getWidth () {
        return width_;
    }

    /**
     * @brief Returns the height of this Image
     *
     * @return The height of this Image
     */
    public int getHeight () {
        return height_;
    }

    private int width_; //!< The width of this Image
    private int height_; //!< The height of this Image

    private byte[][] pixels_; //!< The pixel values of this Image

}
