package io.github;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Makes color probes in randomly selected areas of the image to determine paper luminance
 */
public class ColorProbesMonteCarlo
{
    final private BufferedImage image;

    private int paperColorLuminanceFrom; //can be changed
    private int paperColorLuminanceTo;

    private final int NUMBER_OF_SUCCESSFUL_PROBES = 500; //number of successful probes that we need
    private final int MAXIMUM_NUMBER_OF_PROBES = 3000; //number of tries we make before we decide that luminance range is wrong

    @Getter
    private List<Rectangle> succesfullProbes = new ArrayList<>(NUMBER_OF_SUCCESSFUL_PROBES);
    @Getter
    private boolean success = false;

    private boolean printDebugMessagesToConsole;

    public ColorProbesMonteCarlo(BufferedImage image, int paperColorLuminanceFrom, int paperColorLuminanceTo, boolean printDebugMessagesToConsole)
    {
        this.image = image;
        this.paperColorLuminanceFrom = paperColorLuminanceFrom;
        this.paperColorLuminanceTo   = paperColorLuminanceTo;
        this.printDebugMessagesToConsole = printDebugMessagesToConsole;

        makeProbes();
    }

    private void makeProbes()
    {
        success = makeProbesAtRandomCoordinates();
    }

    private boolean makeProbesAtRandomCoordinates()
    {
        int xFrom   = (int)(image.getWidth()  * PaperColorFinder.CROPPING_MARGINS / 100);
        int xTo     = image.getWidth() - xFrom;
        int yFrom   = (int)(image.getHeight() * PaperColorFinder.CROPPING_MARGINS / 100);
        int yTo     = image.getHeight() - yFrom;

        int numberOfSuccessfulProbes = 0;

        for (int i = 0; i < MAXIMUM_NUMBER_OF_PROBES; i ++)
        {
            int randomX = ThreadLocalRandom.current().nextInt(xFrom, xTo);
            int randomY = ThreadLocalRandom.current().nextInt(yFrom, yTo);

            ColorProbeArea colorProbe = new ColorProbeArea(randomX, randomY, image, paperColorLuminanceFrom, paperColorLuminanceTo, printDebugMessagesToConsole);

            if (colorProbe.paperColorAreaWasFound())
            {
                succesfullProbes.add(colorProbe.getRectangle());

                numberOfSuccessfulProbes ++;

                if (printDebugMessagesToConsole)
                    System.out.printf("Successful probe. Total number of successful probes is: %d\n", numberOfSuccessfulProbes);

                if (numberOfSuccessfulProbes >= NUMBER_OF_SUCCESSFUL_PROBES)
                {
                    return true;
                }
            }
        }

        if (printDebugMessagesToConsole)
            System.out.printf("Fail. Total number of successful probes (%d) is less than required\n", numberOfSuccessfulProbes);

        return false;
    }
}
