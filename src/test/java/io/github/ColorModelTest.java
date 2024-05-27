package io.github;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorModelTest
{
    @Test
    void testInt255Luminance()
    {
        assertEquals(0,   ColorModel.int255Luminance(0, 0, 0));
        assertEquals(255, ColorModel.int255Luminance(255, 255, 255));
        assertEquals(255, ColorModel.int255Luminance(0x00ffffff));
        assertEquals(0,   ColorModel.int255Luminance(0xff000000));
    }
}