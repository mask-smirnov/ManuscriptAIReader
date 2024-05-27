package io.github;

import io.github.utils.ImageViewer;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaperColorFinderTest
{
    @Test
    void testPaperColorSearch() throws IOException, ImageIsNotAManuscriptException
    {
        //This test analyzes random parts of image. So there's a small probability that it can fail
        //Just rerun it then. I've run 1000 cycles at the developement stage, and then I've reduced them to 30
        //to save overall unit test run time

        BufferedImage image = new InitTestData().testImage("GAVO-1067-1-396-1.JPG");

        PaperColorFinder paperColorFinder = new PaperColorFinder(image);

        assertEquals(164, paperColorFinder.getMostFrequentLuminance());
        assertEquals(144, paperColorFinder.getPaperInkBorderApprox());
        assertEquals(172, paperColorFinder.getPaperColorLighterBorderApprox());

        //paperInkBorder is calculated by selecting random areas. So it can be different test to test. That's why
        //we run test many times and allow slight fluctuations
        int paperInkBorder = 0;

        for (int i = 0; i < 10; i ++) //after the developement I've run 1000 cycles to be sure it's stable
        {
            paperColorFinder = new PaperColorFinder(image);

            paperInkBorder = paperColorFinder.getPaperInkBorder();

            System.out.printf("Test number %d; paper-ink border: %d\n", i, paperInkBorder);
            assertTrue(paperInkBorder >= 151 && paperInkBorder <= 154);
        }

        paintPaperWhite(144);

    }

    private void paintPaperWhite(int paperInkBorder) throws IOException
    {
        BufferedImage image = new InitTestData().testImage("GAVO-1067-1-396-1.JPG");

        ImageViewer viewer = new ImageViewer(image);

        for (int x = 0; x < image.getWidth(); x ++)
        {
            for (int y = 0; y < image.getHeight(); y ++)
            {
                if (ColorModel.int255Luminance(image.getRGB(x, y)) > paperInkBorder)
                    viewer.drawWhitePixel(x, y);
            }
        }

        viewer.open();

    }
}