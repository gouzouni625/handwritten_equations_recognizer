package org.hwer.engine.utilities.image_processing.drawing;


import org.hwer.engine.utilities.image_processing.image.Image;


/**
 * @class Drawer
 * @brief Provides methods to draw shapes on an Image
 */
public class Drawer {
    /**
     * @brief Draws a straight line on a given Image
     *
     * @param image
     *     The Image to draw the line on
     * @param xStart
     *     The abscissa of the starting point of the line
     * @param yStart
     *     The ordinate of the starting point of the line
     * @param xEnd
     *     The abscissa of the ending point of the line
     * @param yEnd
     *     The ordinate of the ending point of the line
     *
     * @return The Image with the drawn line
     */
    public static Image drawLine (Image image, int xStart, int yStart, int xEnd, int yEnd) {
        if (xStart == xEnd) {
            if (yStart < yEnd) {
                drawLineBresenham2Vertical(image, xStart, yStart, xEnd, yEnd, WHITE);
                return image;
            }
            else if (yStart == yEnd) {
                image.setPixel(xStart, yStart, WHITE);
                return image;
            }
            else {
                drawLineBresenham6Vertical(image, xStart, yStart, xEnd, yEnd, WHITE);
                return image;
            }
        }

        double slope = (double) (yEnd - yStart) / ((double) (xEnd - xStart));

        if (slope < - 1) {
            if (xStart < xEnd) {
                drawLineBresenham7(image, xStart, yStart, xEnd, yEnd, WHITE);
                return image;
            }
            else {
                drawLineBresenham3(image, xStart, yStart, xEnd, yEnd, WHITE);
                return image;
            }
        }
        else if (- 1 <= slope && slope < 0) {
            if (xStart < xEnd) {
                drawLineBresenham8(image, xStart, yStart, xEnd, yEnd, WHITE);
                return image;
            }
            else {
                drawLineBresenham4(image, xStart, yStart, xEnd, yEnd, WHITE);
                return image;
            }
        }
        else if (0 <= slope && slope <= 1) {
            if (xStart < xEnd) {
                drawLineBresenham1(image, xStart, yStart, xEnd, yEnd, WHITE);
                return image;
            }
            else {
                drawLineBresenham5(image, xStart, yStart, xEnd, yEnd, WHITE);
                return image;
            }
        }
        else { // if(slope > 1)
            if (xStart < xEnd) {
                drawLineBresenham2(image, xStart, yStart, xEnd, yEnd, WHITE);
                return image;
            }
            else {
                drawLineBresenham6(image, xStart, yStart, xEnd, yEnd, WHITE);
                return image;
            }
        }
    }

    /**
     * @brief Draws a line on a given Image using the Bresenham algorithm
     *        This method is used for drawing a line with a slope from 0 to pi / 4.
     *
     * @param image
     *     The Image to draw the line on
     * @param xStart
     *     The abscissa of the starting point of the line
     * @param yStart
     *     The ordinate of the starting point of the line
     * @param xEnd
     *     The abscissa of the ending point of the line
     * @param yEnd
     *     The ordinate of the ending point of the line
     * @param color
     *     The color of the line
     *
     * @return The Image with the drawn line
     */
    private static void drawLineBresenham1 (Image image, int xStart, int yStart, int xEnd, int yEnd,
                                           byte color) {
        int dx = xEnd - xStart;
        int dy = yEnd - yStart;
        int e = - (dx >> 1);
        int x = xStart;
        int y = yStart;

        while (x <= xEnd) {
            image.setPixel(x, y, color);

            x++;

            e = e + dy;
            if (e >= 0) {
                y++;
                e = e - dx;
            }
        }
    }

    /**
     * @brief Draws a line on a given Image using the Bresenham algorithm
     *        This method is used for drawing a line with a slope from pi / 4 to pi / 2.
     *
     * @param image
     *     The Image to draw the line on
     * @param xStart
     *     The abscissa of the starting point of the line
     * @param yStart
     *     The ordinate of the starting point of the line
     * @param xEnd
     *     The abscissa of the ending point of the line
     * @param yEnd
     *     The ordinate of the ending point of the line
     * @param color
     *     The color of the line
     *
     * @return The Image with the drawn line
     */
    private static void drawLineBresenham2 (Image image, int xStart, int yStart, int xEnd, int yEnd,
                                           byte color) {
        int dx = xEnd - xStart;
        int dy = yEnd - yStart;
        int e = - (dy >> 1);
        int x = xStart;
        int y = yStart;

        while (y <= yEnd) {
            image.setPixel(x, y, color);

            y++;

            e = e + dx;
            if (e >= 0) {
                x++;
                e = e - dy;
            }
        }
    }

    /**
     * @brief Draws a line on a given Image using the Bresenham algorithm
     *        This method is used for drawing a line with a slope equal to pi / 2.
     *
     * @param image
     *     The Image to draw the line on
     * @param xStart
     *     The abscissa of the starting point of the line
     * @param yStart
     *     The ordinate of the starting point of the line
     * @param xEnd
     *     The abscissa of the ending point of the line
     * @param yEnd
     *     The ordinate of the ending point of the line
     * @param color
     *     The color of the line
     *
     * @return The Image with the drawn line
     */
    private static void drawLineBresenham2Vertical (Image image, int xStart, int yStart, int xEnd,
                                                   int yEnd, byte color) {
        int dx = xEnd - xStart;
        int dy = yEnd - yStart;
        int e = - (dy >> 1);
        int x = xStart;
        int y = yStart;

        while (y <= yEnd) {
            image.setPixel(x, y, color);

            y++;

            e = e + dx;
            if (e >= 0) {
                x++;
                e = e - dy;
            }
        }
    }

    /**
     * @brief Draws a line on a given Image using the Bresenham algorithm
     *        This method is used for drawing a line with a slope from pi / 2 to 3pi / 4.
     *
     * @param image
     *     The Image to draw the line on
     * @param xStart
     *     The abscissa of the starting point of the line
     * @param yStart
     *     The ordinate of the starting point of the line
     * @param xEnd
     *     The abscissa of the ending point of the line
     * @param yEnd
     *     The ordinate of the ending point of the line
     * @param color
     *     The color of the line
     *
     * @return The Image with the drawn line
     */
    private static void drawLineBresenham3 (Image image, int xStart, int yStart, int xEnd, int yEnd,
                                           byte color) {
        int dx = xStart - xEnd;
        int dy = yEnd - yStart;
        int e = - (dy >> 1);
        int x = xStart;
        int y = yStart;

        while (y <= yEnd) {
            image.setPixel(x, y, color);

            y++;

            e = e + dx;
            if (e >= 0) {
                x--;
                e = e - dy;
            }
        }
    }

    /**
     * @brief Draws a line on a given Image using the Bresenham algorithm
     *        This method is used for drawing a line with a slope from 3pi / 4 to pi.
     *
     * @param image
     *     The Image to draw the line on
     * @param xStart
     *     The abscissa of the starting point of the line
     * @param yStart
     *     The ordinate of the starting point of the line
     * @param xEnd
     *     The abscissa of the ending point of the line
     * @param yEnd
     *     The ordinate of the ending point of the line
     * @param color
     *     The color of the line
     *
     * @return The Image with the drawn line
     */
    private static void drawLineBresenham4 (Image image, int xStart, int yStart, int xEnd, int yEnd,
                                           byte color) {
        drawLineBresenham8(image, xEnd, yEnd, xStart, yStart, color);
    }

    /**
     * @brief Draws a line on a given Image using the Bresenham algorithm
     *        This method is used for drawing a line with a slope from pi to 5pi / 4.
     *
     * @param image
     *     The Image to draw the line on
     * @param xStart
     *     The abscissa of the starting point of the line
     * @param yStart
     *     The ordinate of the starting point of the line
     * @param xEnd
     *     The abscissa of the ending point of the line
     * @param yEnd
     *     The ordinate of the ending point of the line
     * @param color
     *     The color of the line
     *
     * @return The Image with the drawn line
     */
    private static void drawLineBresenham5 (Image image, int xStart, int yStart, int xEnd, int yEnd,
                                           byte color) {
        drawLineBresenham1(image, xEnd, yEnd, xStart, yStart, color);
    }

    /**
     * @brief Draws a line on a given Image using the Bresenham algorithm
     *        This method is used for drawing a line with a slope from 5pi / 4 to 3pi / 2.
     *
     * @param image
     *     The Image to draw the line on
     * @param xStart
     *     The abscissa of the starting point of the line
     * @param yStart
     *     The ordinate of the starting point of the line
     * @param xEnd
     *     The abscissa of the ending point of the line
     * @param yEnd
     *     The ordinate of the ending point of the line
     * @param color
     *     The color of the line
     *
     * @return The Image with the drawn line
     */
    private static void drawLineBresenham6 (Image image, int xStart, int yStart, int xEnd, int yEnd,
                                           byte color) {
        drawLineBresenham2(image, xEnd, yEnd, xStart, yStart, color);
    }

    /**
     * @brief Draws a line on a given Image using the Bresenham algorithm
     *        This method is used for drawing a line with a slope equal to 3pi / 2.
     *
     * @param image
     *     The Image to draw the line on
     * @param xStart
     *     The abscissa of the starting point of the line
     * @param yStart
     *     The ordinate of the starting point of the line
     * @param xEnd
     *     The abscissa of the ending point of the line
     * @param yEnd
     *     The ordinate of the ending point of the line
     * @param color
     *     The color of the line
     *
     * @return The Image with the drawn line
     */
    private static void drawLineBresenham6Vertical (Image image, int xStart, int yStart, int xEnd,
                                                   int yEnd, byte color) {
        drawLineBresenham2Vertical(image, xEnd, yEnd, xStart, yStart, color);
    }

    /**
     * @brief Draws a line on a given Image using the Bresenham algorithm
     *        This method is used for drawing a line with a slope from 3pi / 2 to 7pi / 4.
     *
     * @param image
     *     The Image to draw the line on
     * @param xStart
     *     The abscissa of the starting point of the line
     * @param yStart
     *     The ordinate of the starting point of the line
     * @param xEnd
     *     The abscissa of the ending point of the line
     * @param yEnd
     *     The ordinate of the ending point of the line
     * @param color
     *     The color of the line
     *
     * @return The Image with the drawn line
     */
    private static void drawLineBresenham7 (Image image, int xStart, int yStart, int xEnd, int yEnd,
                                           byte color) {
        drawLineBresenham3(image, xEnd, yEnd, xStart, yStart, color);
    }

    /**
     * @brief Draws a line on a given Image using the Bresenham algorithm
     *        This method is used for drawing a line with a slope from 7pi / 4 to 2pi.
     *
     * @param image
     *     The Image to draw the line on
     * @param xStart
     *     The abscissa of the starting point of the line
     * @param yStart
     *     The ordinate of the starting point of the line
     * @param xEnd
     *     The abscissa of the ending point of the line
     * @param yEnd
     *     The ordinate of the ending point of the line
     * @param color
     *     The color of the line
     *
     * @return The Image with the drawn line
     */
    private static void drawLineBresenham8 (Image image, int xStart, int yStart, int xEnd, int yEnd,
                                           byte color) {
        int dx = xEnd - xStart;
        int dy = yStart - yEnd;
        int e = - (dx >> 1);
        int x = xStart;
        int y = yStart;

        while (x <= xEnd) {
            image.setPixel(x, y, color);

            x++;

            e = e + dy;
            if (e >= 0) {
                y--;
                e = e - dx;
            }
        }
    }

    /**
     * @brief Draws a circle on a given Image using the Hanrahan algorithm
     *
     * @param image
     *     The Image to draw the circle on
     * @param centerX
     *     The abscissa of the center of the circle
     * @param centerY
     *     The ordinate of the center of the circle
     * @param radius
     *     The radius of the circle
     * @param color
     *     The color of the circle
     *
     * @return The Image with the drawn circle
     */
    public static Image drawCircle (Image image, int centerX, int centerY, int radius, byte color) {
        int x = 0;
        int y = radius;
        int e = - radius;

        while (x <= y) {
            image.setPixel(centerX + x, centerY + y, color); //  x,  y
            image.setPixel(centerX + y, centerY + x, color); //  y,  x
            image.setPixel(centerX + x, centerY - y, color); //  x, -y
            image.setPixel(centerX + y, centerY - x, color); //  y, -x
            image.setPixel(centerX - x, centerY + y, color); // -x,  y
            image.setPixel(centerX - y, centerY + x, color); // -y,  x
            image.setPixel(centerX - x, centerY - y, color); // -x, -y
            image.setPixel(centerX - y, centerY - x, color); // -y, -x

            e = e + (x << 1) + 1;
            x++;

            if (e >= 0) {
                e = e - (y << 1) + 2;
                y--;
            }
        }

        return image;
    }

    public static final byte WHITE = (byte) 255;
    public static final byte BLACK = (byte) 0;

}
