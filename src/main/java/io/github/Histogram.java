package io.github;

import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class Histogram
{
    protected Map<Integer, Integer> histogram;

    private List<Rectangle> calculationArea;

    private BufferedImage image;

    /**
     * Total number of pixels used to create the histogram (doesn't equal to number of pixels in the image because of margins)
     */
    @Getter
    private int totalPixels = 0;

    @Getter
    private final int size;//paper color peak
    private Integer mostFrequentLuminance = null;

    public static final int PROXIMITY_FOR_MAXIMUM_SEARCH = 2; //luminance +/-
    public final int PROXIMITY_FOR_INK_PAPER_BORDER_SEARCH = 2; //luminance +/-

    /*
    @image - Analyzed image
    @marginsPercent - margins of image that are not included in histogram (for tasks like determining paper or ink color)
     */
    public Histogram(BufferedImage image, double marginsPercent)
    {
        this(image, List.of(BufferedImageExt.calcRectangleByMarginsPct(image, marginsPercent)));
    }

    public Histogram(BufferedImage image, List<Rectangle> calculationArea)
    {
        this.calculationArea = calculationArea;
        this.image = image;

        size = ColorModel.COLORS_PER_CHANNEL;
        histogram = new HashMap<>(size);

        populateHistogramWithZeroes();

        buildHistogram();
    }



    private void populateHistogramWithZeroes()
    {
        for (int i = 0; i < size; i++)
            histogram.put(i, 0);
    }

    private void buildHistogram()
    {
        calculationArea.forEach((rect) -> calculateOneRectangle(rect));
    }

    private void calculateOneRectangle(Rectangle rectangle)
    {
        for (int x = rectangle.x; x < rectangle.x + rectangle.width - 1; x ++)
        {
            for (int y = rectangle.y; y < rectangle.y + rectangle.height - 1; y ++)
            {
                int luminance = ColorModel.int255Luminance(image.getRGB(x, y));

                histogram.compute(luminance,
                                  (k, v) -> v + 1); //map was pre-populated with zeroes

                totalPixels ++;
            }
        }
    }

    public int getCount(int luminance)
    {
        if (luminance >= size)
            throw new IllegalArgumentException("Luminance between 0 and 255 expected");

        return histogram.get(luminance);
    }

    public int calcSumOfNeighbours(int centralPoint, int proximity)
    {
        int ret = 0;

        int fromPoint = centralPoint - proximity;
        if (fromPoint < 0)
            fromPoint = 0;

        int toPoint = centralPoint + proximity;
        if (toPoint >= size)
            toPoint = size - 1; //i.e. 255

        for (int i = fromPoint; i <= toPoint; i ++)
        {
            ret += getCount(i);
        }

        return ret;
    }

    //Searches for a maximum on a histogram taking into account neighbours of each point (+|- proximityWidth)
    public int searchForPaperColorMaximum()
    {
        int maximumValue = 0;

        for (int i = 0; i < size; i ++)
        {
            int value = calcSumOfNeighbours(i, PROXIMITY_FOR_MAXIMUM_SEARCH);
            if (value > maximumValue) //"Greater", not "greater than", i.e. the first maximum, that we meet, when
            //we move from black to white. That's the maximum that we need because the
            //reason we search for paper color is to use it as a starting point when we
            //search for ink color (the maximum in the opposite direction, from white
            //to black
            {
                mostFrequentLuminance = i;
                maximumValue = value;
            }
        }

        return mostFrequentLuminance;
    }

    //searches for luminance threshold
    public int searchForLuminanceTreshold(int StartAtLuminance, int step, double threshold)
    {
        if (mostFrequentLuminance == null)
            searchForPaperColorMaximum();

        int valueAtPaperColorMaximum = calcSumOfNeighbours(mostFrequentLuminance, PROXIMITY_FOR_INK_PAPER_BORDER_SEARCH);

        //go from paper color maximum to darker colors
        for (int i = StartAtLuminance; i >= 0 && i < size; i += step)
        {
            if (calcSumOfNeighbours(i, PROXIMITY_FOR_INK_PAPER_BORDER_SEARCH) * threshold <
                valueAtPaperColorMaximum)
            {
                return i;
            }
        }

        return 0;
    }
}
