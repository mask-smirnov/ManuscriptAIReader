package io.github;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class InitTestData
{
    public BufferedImage testImage(String filename) throws IOException
    {
        return BufferedImageExt.CreateFromFile(
            getClass().getClassLoader().getResource("GAVO-1067-1-396-1.JPG").getFile());
    }
}
