package io.github;

import lombok.Getter;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

//Searches for typical quill width (
//Gathers statistics of horizontal cross-sections through the image
//For each cross-section it gathers length of ink-covered areas
//The most frequent length is the quill width
public class QuillWidthFinder
{
    private final int paperInkBorder;
    private final BufferedImage image;

    public static final double QUILL_SEARCH_AREA_MARGINS_PCT = 20; //Crop margins to avoid analyzing border artifacts
    public static final int PROBE_HEIGHT = 3; //height od cross-section probe

    @Getter
    private Map<Integer, Integer> inkAreaWidths = new HashMap<>(); //ink area width -> number of occurrences

    public QuillWidthFinder(BufferedImage image, int paperInkBorder)
    {
        this.image = image;
        this.paperInkBorder = paperInkBorder;

        run();
    }

    private void run()
    {
        int xFrom   = (int)(image.getWidth()  * QUILL_SEARCH_AREA_MARGINS_PCT / 100);
        int xTo     = image.getWidth() - xFrom;
        int yFrom   = (int)(image.getHeight() * QUILL_SEARCH_AREA_MARGINS_PCT / 100);
        int yTo     = image.getHeight() - yFrom;

        for (int y = yFrom; y <= yTo; y ++)
        {
            int inkAreaWidth = 0;
            boolean inkAreaStarted = false;

            for (int x = xFrom; x <= xTo; x ++)
            {
                if (isInk(x, y))
                {
                    inkAreaWidth ++;
                    inkAreaStarted = true;
                }
                else
                {
                    if (inkAreaStarted) //It means that paper area has just started
                    {
                        logInkArea(inkAreaWidth);
                    }

                    inkAreaWidth = 0;
                    inkAreaStarted = false;
                }
            }
        }

    }

    private boolean isInk(int x, int y)
    {
        int numberOfInkPixels = 0;

        for (int yAdd = 0; yAdd < PROBE_HEIGHT; yAdd ++)
        {
            if (ColorModel.int255Luminance(image.getRGB(x, y + yAdd)) <= paperInkBorder)
                numberOfInkPixels ++;
        }

        return numberOfInkPixels >= PROBE_HEIGHT - 1; //allow 1 non-ink pixel
    }

    private void logInkArea(int width)
    {
        inkAreaWidths.merge(width, 1, Integer::sum);
    }
}
