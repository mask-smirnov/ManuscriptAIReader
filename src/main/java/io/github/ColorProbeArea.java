package io.github;


import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Tries to find maximum rectangle around (x, y) that have no more than MAXIMUM_NONPAPER_PIXELS pixels
 * that have luminance outside [paperColorLuminanceFrom, paperColorLuminanceTo] range
 * If percentage of such pixels is more than MAXIMUM_NONPAPER_PIXELS_PCT or total number of pixels is
 * less than MINIMUM_NUMBER_OF_PIXELS, PaperColorAreaWasFound() returns false
 */
public class ColorProbeArea implements ExpandingPossibilityProvider
{
    final private BufferedImage image;
    final private int paperColorLuminanceFrom;
    final private int paperColorLuminanceTo;

    @Getter
    final private ExpandingRectangle rectangle;

    private final int MINIMUM_NUMBER_OF_PIXELS = 500;
    private final int MAXIMUM_NONPAPER_PIXELS = 5;
    private final double MAXIMUM_NONPAPER_PIXELS_PCT = 1.0; //maximum non-paper color pixels, controlled at the end of the process
    private int nonpaperPixelsMet = 0;

    private int darkerPixels = 0;
    private int lighterPixels = 0;

    private boolean printDebugMessagesToConsole;

    public ColorProbeArea(int x, int y, BufferedImage image, int paperColorLuminanceFrom, int paperColorLuminanceTo, boolean printDebugMessagesToConsole)
    {
        this.image = image;
        this.paperColorLuminanceFrom = paperColorLuminanceFrom;
        this.paperColorLuminanceTo   = paperColorLuminanceTo;
        this.printDebugMessagesToConsole = printDebugMessagesToConsole;

        rectangle = new ExpandingRectangle(x, y, this);

        run();
    }

    public boolean paperColorAreaWasFound()  //result of probe, alongside with rectangle
    {
        if (printDebugMessagesToConsole)
            System.out.printf("Probe: darker pixels %d, lighter pixels %d\n", darkerPixels, lighterPixels);

        int numberOfPixels = rectangle.width * rectangle.height;

        if (numberOfPixels < MINIMUM_NUMBER_OF_PIXELS)
        {
            if (printDebugMessagesToConsole)
                System.out.printf("Probe failed, number of pixels (%d) is less than (%d)\n", numberOfPixels, MINIMUM_NUMBER_OF_PIXELS);

            return false;
        }

        if ((double)nonpaperPixelsMet > (numberOfPixels * MAXIMUM_NONPAPER_PIXELS_PCT) / 100)
        {
            if (printDebugMessagesToConsole)
                System.out.printf("Probe failed, number of non-paper pixels (%f) is more than (%f)\n", (double)nonpaperPixelsMet, numberOfPixels * MAXIMUM_NONPAPER_PIXELS_PCT / 100);

            return false;
        }

        return true;
    }

    private void run()
    {
        do
        {
            rectangle.expand();
        }
        while (rectangle.hasExpandedSuccesfully);

        if (printDebugMessagesToConsole)
            System.out.printf("Color probe: expanded to %d pixels\n", rectangle.width * rectangle.height);
    }

    @Override
    public boolean canExpand(Rectangle currentArea, Rectangle newArea)
    {
        return RectangleExt.fitsIntoImageBoundaries(newArea, new Rectangle(image.getWidth(), image.getHeight()))
               && checkColorsInRectangle(newArea);
    }

    //Checks for non-paper color pixels. If overall count is more than MAXIMUM_NONPAPER_COLOR_PIXELS, returns false
    //Otherwise returns true and increases overall non-paper pixels count
    private boolean checkColorsInRectangle(Rectangle area)
    {
        int nonpaperPixelsNew = 0;

        for (int i = area.x; i < area.x + area.width; i++)
        {
            for (int j = area.y; j < area.y + area.height; j++)
            {
                int luminance = ColorModel.int255Luminance(image.getRGB(i, j));

                if (luminance < paperColorLuminanceFrom ||
                    luminance > paperColorLuminanceTo)
                {
                    if (luminance < paperColorLuminanceFrom)
                        darkerPixels ++;
                    else if (luminance > paperColorLuminanceTo)
                        lighterPixels ++;

                    nonpaperPixelsNew ++;

                    if (nonpaperPixelsMet + nonpaperPixelsNew > MAXIMUM_NONPAPER_PIXELS)
                        return false;
                }
            }
        }

        nonpaperPixelsMet += nonpaperPixelsNew;
        return true;
    }
}
