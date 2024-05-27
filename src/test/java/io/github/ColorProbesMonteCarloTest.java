package io.github;

import org.junit.jupiter.api.Test;
import io.github.utils.ImageViewer;
import java.awt.image.BufferedImage;
import java.io.IOException;

class ColorProbesMonteCarloTest
{

    @Test
    void isExecutedSuccessfully() throws IOException, ImageIsNotAManuscriptException
    {
        boolean debugMode = false;

        BufferedImage image = new InitTestData().testImage("GAVO-1067-1-396-1.JPG");

        PaperColorFinder paperColorFinder = new PaperColorFinder(image);

        ColorProbesMonteCarlo probes = new ColorProbesMonteCarlo(
            image,
            paperColorFinder.getPaperInkBorderApprox(),
            paperColorFinder.getPaperColorLighterBorderApprox(),
            debugMode
        );

        if (probes.isSuccess())
        {
            if (debugMode)
            {
                ImageViewer viewer = new ImageViewer(new InitTestData().testImage("GAVO-1067-1-396-1.JPG"));

                probes.getSuccesfullProbes().forEach((rect) -> viewer.drawRectangleAround(rect));
                viewer.open();
            }
        }

        Histogram histogram = new Histogram(image, probes.getSuccesfullProbes());
        HistogramTest.printHistogram(histogram);
        System.out.printf("Maximum at %d\n", histogram.searchForPaperColorMaximum());


    }
}