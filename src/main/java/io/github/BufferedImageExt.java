package io.github;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BufferedImageExt
{
    static public BufferedImage CreateFromFile(String filename) throws IOException
    {
        return ImageIO.read(new FileInputStream(filename));
    }

    public static Rectangle calcRectangleByMarginsPct(BufferedImage image, double marginsPercent)
    {
        int xMargin = (int) (image.getWidth() * marginsPercent / 100);
        int yMargin = (int) (image.getHeight() * marginsPercent / 100);

        return new Rectangle(
            xMargin,
            yMargin,
            image.getWidth() - 2 * xMargin,
            image.getHeight() - 2 * yMargin
        );
    }
}
