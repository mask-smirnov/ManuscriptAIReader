package io.github;

import lombok.Getter;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * Rectangular area that can expand in 4 directions until it meets pixels with unacceptable color
 */
public class ExpandingRectangle extends Rectangle
{
    enum Directions { LEFT, RIGHT, UP, DOWN }

    //key: direction, value: can expand further to this direction
    final List<Directions> directions = List.of(
        Directions.LEFT,
        Directions.RIGHT,
        Directions.UP,
        Directions.DOWN);

    Iterator directionsIterator;

    @Getter
    Rectangle newArea;
    @Getter
    Boolean hasExpandedSuccesfully;

    private final ExpandingPossibilityProvider expandingPossibilityProvider; //class that tells if we can expand to this area

    public ExpandingRectangle(int x, int y, ExpandingPossibilityProvider expandingPossibilityProvider)
    {
        super(x, y, 1, 1);

        this.expandingPossibilityProvider = expandingPossibilityProvider;
    }

    public void expand()
    {
        for (int i = 1; i <= 4; i ++) //4 tries. If all 4 fail, it means that it cannot expand anymore
        {
            if (directionsIterator == null || !directionsIterator.hasNext())
                directionsIterator = directions.iterator();;

            Directions newDirection = (Directions) directionsIterator.next();

            if (expandingPossibilityProvider.canExpand(this, calcNewArea(newDirection))) //checks image boundaries & colors acceptability
            {
                newArea = calcNewArea(newDirection);
                this.add(newArea);
                hasExpandedSuccesfully = true;
                return;
            }
        }

        newArea = null;
        hasExpandedSuccesfully = false;
    }

    private Rectangle calcNewArea(Directions direction)
    {
        switch (direction)
        {
            case LEFT:
                return new Rectangle(x - 1, y, 1, height);
            case RIGHT:
                return new Rectangle(x + width, y, 1, height);
            case UP:
                return new Rectangle(x, y - 1, width, 1);
            case DOWN:
                return new Rectangle(x, y + height, width, 1);
        }

        throw new IllegalArgumentException("No direction was specified");
    }
}

