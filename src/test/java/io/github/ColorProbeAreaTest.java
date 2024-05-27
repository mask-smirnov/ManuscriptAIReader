package io.github;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ColorProbeAreaTest
{

    @Test
    void TryToFindPaperColorArea() throws IOException, ImageIsNotAManuscriptException
    {
        BufferedImage image = new InitTestData().testImage("GAVO-1067-1-396-1.JPG");

        PaperColorFinder paperColorFinder = new PaperColorFinder(image);

        ColorProbeArea colorProbe;

        //ok
        colorProbe = new ColorProbeArea( 1300, 1100, image, paperColorFinder.getPaperInkBorderApprox(), paperColorFinder.getPaperColorLighterBorderApprox(), false);

//        ImageViewer viewer = new ImageViewer(new InitTestData().testImage("GAVO-1067-1-396-1.JPG"));
//        viewer.drawRectangleAround(colorProbe.getRectangle());
//        viewer.open();

        assertTrue(colorProbe.paperColorAreaWasFound());

        //hits ink color immidiately
        colorProbe = new ColorProbeArea( 1338, 1255, image, paperColorFinder.getPaperInkBorderApprox(), paperColorFinder.getPaperColorLighterBorderApprox(), false);
        assertFalse(colorProbe.paperColorAreaWasFound());

        //gets into a circle inside a letter (to small size)
        colorProbe = new ColorProbeArea( 350, 690, image, paperColorFinder.getPaperInkBorderApprox(), paperColorFinder.getPaperColorLighterBorderApprox(), false);
        assertFalse(colorProbe.paperColorAreaWasFound());

        //debugging output
        /*
        System.out.printf("paper found: %b\n", colorProbe.paperColorAreaWasFound());
        System.out.printf("rect %d %d\n", colorProbe.getRectangle().width, colorProbe.getRectangle().height);
        */


    }
}