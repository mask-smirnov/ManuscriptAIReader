package io.github.utils;


import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.ImageOutputStreamImpl;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Launches OS's default image viewer
 */
public class ImageViewer
{
    private final BufferedImage image;
    private Graphics2D graphics;

    public ImageViewer(BufferedImage image) throws IOException
    {
        this.image = image;
    }

    //draws rectangle around (1 pixel larger to each side)
    public void drawRectangleAround(Rectangle rectangle)
    {
        initGraphics();

        graphics.setColor(Color.RED);
        graphics.drawRect(
            rectangle.x - 1,
            rectangle.y - 1,
            rectangle.width + 2,
            rectangle.height + 2);
    }

    public void drawWhitePixel(int x, int y)
    {
        image.setRGB(x, y, 0x00FFFFFF);
    }

    public void open() throws IOException
    {
        try {
            launchOSdefaultViewer(image);
        }
        finally
        {
            if (graphics != null)
                graphics.dispose();
        }
    }

    private void initGraphics()
    {
        if (graphics == null)
            graphics = image.createGraphics();
    }

    private void launchOSdefaultViewer(BufferedImage image) throws IOException
    {
        File tmpfile =
            Path.of(System.getProperty("java.io.tmpdir"), "testImage001.jpg").toFile();

        if (!tmpfile.exists())
            tmpfile.createNewFile();

        ImageIO.write(image, "png", tmpfile);

        Desktop dt = Desktop.getDesktop();
        dt.open(tmpfile);
    }
}
