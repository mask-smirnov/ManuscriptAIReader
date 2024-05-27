package io.github;

import java.awt.*;

public interface ExpandingPossibilityProvider
{
    boolean canExpand(Rectangle currentArea, Rectangle newArea);
}
