package io.github;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class StrokeAnalyzer
{
    BufferedImage image;

    public int getImageWidth()  { return image.getWidth();  }
    public int getImageHeight() { return image.getHeight(); }

    public void loadImage(String filename) throws IOException
    {
        image = BufferedImageExt.CreateFromFile(filename);


    }
}
