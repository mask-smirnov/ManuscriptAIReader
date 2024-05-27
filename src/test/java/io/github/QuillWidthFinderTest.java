package io.github;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class QuillWidthFinderTest
{

    @Test
    void getInkAreaWidths() throws IOException, ImageIsNotAManuscriptException
    {
        BufferedImage image = new InitTestData().testImage("GAVO-1067-1-396-1.JPG");

        PaperColorFinder paperColorFinder = new PaperColorFinder(image);

        QuillWidthFinder quillWidthFinder = new QuillWidthFinder(image, 142 /*paperColorFinder.getPaperInkBorder()*/);

        quillWidthFinder.getInkAreaWidths().forEach((k, v) -> System.out.printf("%d - %d\n", k, v));
    }
}