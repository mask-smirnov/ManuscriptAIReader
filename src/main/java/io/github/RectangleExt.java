package io.github;

import java.awt.*;

public class RectangleExt
{
    public static boolean fitsIntoImageBoundaries(Rectangle rectangle, Rectangle image)
    {
        if (check(rectangle.x, image.width) &&
            check(rectangle.x + rectangle.width - 1, image.width) &&
            check(rectangle.y, image.height) &&
            check(rectangle.y + rectangle.height - 1, image.height))
            return true;

        return false;

    }

    private static Boolean check(int value, int imageSize)
    {
        if (value < 0 || value >= imageSize)
            return false;

        return true;
    }
}
