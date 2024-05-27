package io.github;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HistogramTest
{

    @Test
    void testHistogramOnRealData() throws IOException
    {
        BufferedImage image = new InitTestData().testImage("GAVO-1067-1-396-1.JPG");

        double marginsPercent = 10;
        Histogram histogram = new Histogram(image, marginsPercent);

        System.out.printf("Total pixels: %d\n", histogram.getTotalPixels());

        int total = printHistogram(histogram);

        assertEquals(histogram.getTotalPixels(), total);
        assertEquals(164, histogram.searchForPaperColorMaximum());
    }

    //prints histogram and return number of pixels
    static public int printHistogram(Histogram histogram)
    {
        int total = 0;
        for (int i = 0; i < histogram.getSize(); i++)
        {
            total += histogram.getCount(i);
            System.out.printf("%d - %d\n", i, histogram.getCount(i));
        }

        return total;
    }
}