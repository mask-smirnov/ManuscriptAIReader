package io.github;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StrokeAnalyzerTests {

    @Test
    void strokesCanBeFound() throws IOException
    {
        ClassLoader classLoader = getClass().getClassLoader();

        StrokeAnalyzer strokeAnalyzer = new StrokeAnalyzer();
        strokeAnalyzer.loadImage(classLoader.getResource("GAVO-1067-1-396-1.JPG").getFile());

        assertAll(
            () -> assertEquals(2420, strokeAnalyzer.getImageWidth()),
            () -> assertEquals(3879, strokeAnalyzer.getImageHeight())
        );
    }
}
