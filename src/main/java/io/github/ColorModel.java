package io.github;

/**
 * Color model related functions
 */
public class ColorModel
{
    static final int COLORS_PER_CHANNEL = 256;
    /**
     * @return Calculates ITU BT.601 luminance. Return value is between 0 and 255
     */
    static public double double255Luminance(int r, int g, int b)
    {
        return 0.299f * r + 0.587f * g + 0.114f * b;
    }

    /**
     * @return Calculates ITU BT.601 luminance. Return value is between 0 and 255
     */
    static public int int255Luminance(int r, int g, int b)
    {
        return (int) double255Luminance(r, g, b);
    }

    /**
     * @return Calculates ITU BT.601 luminance. Return value is between 0 and 255
     */
    static public int int255Luminance(int rgb)
    {
        int r = (rgb & 0x00ff0000) >> 16;
        int g = (rgb & 0x0000ff00) >> 8;
        int b =  rgb & 0x000000ff;

        return int255Luminance(r, g, b);
    }
}
