package io.github;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ExpandingRectangleTest
{
    //Dummy class that just checks for image boundaries
    class TestExpandingPossibilityProvider implements ExpandingPossibilityProvider
    {
        private final Rectangle imageRectangle = new Rectangle(100, 100);

        @Override
        public boolean canExpand(Rectangle currentArea, Rectangle newArea)
        {
            return RectangleExt.fitsIntoImageBoundaries(newArea, imageRectangle);
        }
    }

    @Test
    void testExpansion()
    {
        TestExpandingPossibilityProvider testExpandingPossibilityProvider = new TestExpandingPossibilityProvider();

        ExpandingRectangle expandingRectangle = new ExpandingRectangle(50, 50, testExpandingPossibilityProvider);

        expandingRectangle.expand(); //1st step - 1x1 rectangle from the left
        assertAll(
            () -> assertTrue(expandingRectangle.hasExpandedSuccesfully),
            () -> assertEquals(1, expandingRectangle.getNewArea().width),
            () -> assertEquals(1, expandingRectangle.getNewArea().height)
        );

        expandingRectangle.expand(); //2nd step - 1x1 rectangle from the right
        assertAll(
            () -> assertTrue(expandingRectangle.hasExpandedSuccesfully),
            () -> assertEquals(1, expandingRectangle.getNewArea().width * expandingRectangle.getNewArea().height)
            );

        expandingRectangle.expand(); //3rd step - 1x3 rectangle from the top
        assertAll(
            () -> assertTrue(expandingRectangle.hasExpandedSuccesfully),
            () -> assertEquals(3, expandingRectangle.getNewArea().width * expandingRectangle.getNewArea().height)
        );

        expandingRectangle.expand(); //4th step - 1x3 rectangle from the bottom
        assertAll(
            () -> assertTrue(expandingRectangle.hasExpandedSuccesfully),
            () -> assertEquals(3, expandingRectangle.getNewArea().width * expandingRectangle.getNewArea().height)
        );

        expandingRectangle.expand(); //5th step - 1x3 rectangle from the left
        assertAll(
            () -> assertTrue(expandingRectangle.hasExpandedSuccesfully),
            () -> assertEquals(3, expandingRectangle.getNewArea().width * expandingRectangle.getNewArea().height)
        );

        expandingRectangle.expand(); //6th step - 1x3 rectangle from the right,  total 5x3
        expandingRectangle.expand(); //6th step - 5x1 rectangle from the top,    total 5x4
        expandingRectangle.expand(); //7th step - 5x1 rectangle from the bottom, total 5x5
        expandingRectangle.expand(); //8th step - 1x5 rectangle from the left,   total 6x5

        assertAll(
            () -> assertTrue(expandingRectangle.hasExpandedSuccesfully),
            () -> assertEquals(5, expandingRectangle.getNewArea().width * expandingRectangle.getNewArea().height),
            () -> assertEquals(30, expandingRectangle.width * expandingRectangle.height)
        );
    }

    @Test
    void testExpansion_RectangleHitsBoundary()
    {
        TestExpandingPossibilityProvider testExpandingPossibilityProvider = new TestExpandingPossibilityProvider();

        ExpandingRectangle expandingRectangle = new ExpandingRectangle(98, 50, testExpandingPossibilityProvider);

        //At imageWidth = 100, start from 98 (zero-based), should hit boundary on the 6th iteration (L-R-U-D-L-R), having 4x3 size

        expandingRectangle.expand(); //L
        expandingRectangle.expand(); //R
        expandingRectangle.expand(); //U
        expandingRectangle.expand(); //D
        expandingRectangle.expand(); //L
        expandingRectangle.expand(); //Right expansion fails, expands up instead, gaining 4x4 size

        assertAll(
            () -> assertTrue(expandingRectangle.hasExpandedSuccesfully),
            () -> assertEquals(4, expandingRectangle.getNewArea().width * expandingRectangle.getNewArea().height),
            () -> assertEquals(16, expandingRectangle.width * expandingRectangle.height)
        );
    }

    @Test
    void testExpansion_expandsToAllAvailableSpace()
    {
        TestExpandingPossibilityProvider testExpandingPossibilityProvider = new TestExpandingPossibilityProvider();

        ExpandingRectangle expandingRectangle = new ExpandingRectangle(98, 50, testExpandingPossibilityProvider);

        //With no color constraints, limited just by image boundaries, the rectangle expands to all the image area
        do
        {
            expandingRectangle.expand();
        }
        while (expandingRectangle.hasExpandedSuccesfully);

        assertAll(
            () -> assertFalse(expandingRectangle.hasExpandedSuccesfully),
            () -> assertEquals(100*100, expandingRectangle.width * expandingRectangle.height)
        );
    }
}